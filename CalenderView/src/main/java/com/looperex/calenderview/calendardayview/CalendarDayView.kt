package com.looperex.calenderview.calendardayview

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.annotation.NonNull
import com.looperex.calenderview.R
import com.looperex.calenderview.calendardayview.data.IEvent
import com.looperex.calenderview.calendardayview.data.IPopup
import com.looperex.calenderview.calendardayview.data.ITimeDuration
import com.looperex.calenderview.calendardayview.decoration.CdvDecoration
import com.looperex.calenderview.calendardayview.decoration.CdvDecorationDefault
import java.util.*


class CalendarDayView : FrameLayout {
    private var mDayHeight = 0
    private var mEventMarginLeft = 0
    private var mHourWidth = 120
    private var mTimeHeight = 120
    private var mSeparateHourHeight = 0
    private var mStartHour = 0
    private var mEndHour = 24
    private var mLayoutDayView: LinearLayout? = null
    private var mLayoutEvent: FrameLayout? = null
    private var mLayoutPopup: FrameLayout? = null
    var decoration: CdvDecoration? = null
        private set
    private var mEvents: List<IEvent>? = null
    private var mPopups: List<IPopup>? = null

    constructor(context: Context?) : super(context!!) {
        init(null)
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(
        context!!, attrs
    ) {
        init(attrs)
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context!!, attrs, defStyleAttr
    ) {
        init(attrs)
    }

    private fun init(attrs: AttributeSet?) {
        LayoutInflater.from(context).inflate(R.layout.view_day_calendar, this, true)
        mLayoutDayView = findViewById<View>(R.id.dayview_container) as LinearLayout
        mLayoutEvent = findViewById<View>(R.id.event_container) as FrameLayout
        mLayoutPopup = findViewById<View>(R.id.popup_container) as FrameLayout
        mDayHeight = resources.getDimensionPixelSize(R.dimen.dayHeight)
        if (attrs != null) {
            val a = context.obtainStyledAttributes(attrs, R.styleable.CalendarDayView)
            try {
                mEventMarginLeft = a.getDimensionPixelSize(
                    R.styleable.CalendarDayView_eventMarginLeft,
                    mEventMarginLeft
                )
                mDayHeight =
                    a.getDimensionPixelSize(R.styleable.CalendarDayView_dayHeight, mDayHeight)
                mStartHour = a.getInt(R.styleable.CalendarDayView_startHour, mStartHour)
                mEndHour = a.getInt(R.styleable.CalendarDayView_endHour, mEndHour)
            } finally {
                a.recycle()
            }
        }
        mEvents = ArrayList()
        mPopups = ArrayList()
        decoration = CdvDecorationDefault(
            context
        )
        refresh()
    }

    fun refresh() {
        drawDayViews()
        drawEvents()
        drawPopups()
    }

    private fun drawDayViews() {
        mLayoutDayView!!.removeAllViews()
        var dayView: DayView? = null
        for (i in mStartHour..mEndHour) {
            dayView = decoration!!.getDayView(i)
            mLayoutDayView!!.addView(dayView)
        }
        mHourWidth = dayView!!.hourTextWidth.toInt()
        mTimeHeight = dayView.hourTextHeight.toInt()
        mSeparateHourHeight = dayView.separateHeight.toInt()
    }

    private fun drawEvents() {
        mLayoutEvent!!.removeAllViews()
        for (event in mEvents!!) {
            val rect = getTimeBound(event)

            // add event view
            val eventView = decoration!!.getEventView(event, rect, mTimeHeight, mSeparateHourHeight)
            if (eventView != null) {
                mLayoutEvent!!.addView(eventView, eventView.layoutParams)
            }
        }
    }

    private fun drawPopups() {
        mLayoutPopup!!.removeAllViews()
        for (popup in mPopups!!) {
            val rect = getTimeBound(popup)

            // add popup views
            val view = decoration!!.getPopupView(popup, rect, mTimeHeight, mSeparateHourHeight)
            if (view != null) {
                mLayoutPopup!!.addView(view, view.layoutParams)
            }
        }
    }

    private fun getTimeBound(event: ITimeDuration?): Rect {
        val rect = Rect()
        (getPositionOfTime(event!!.startTime) + mTimeHeight / 2 + mSeparateHourHeight).also { rect.top = it }
        rect.bottom = getPositionOfTime(event.endTime) + mTimeHeight / 2 + mSeparateHourHeight
        rect.left = mHourWidth + mEventMarginLeft
        rect.right = width
        return rect
    }

    private fun getPositionOfTime(calendar: Calendar): Int {
        val hour = calendar[Calendar.HOUR_OF_DAY] - mStartHour
        val minute = calendar[Calendar.MINUTE]
        return hour * mDayHeight + minute * mDayHeight / 60
    }

    fun setEvents(events: List<IEvent>?) {
        mEvents = events
        refresh()
    }

    fun setPopups(popups: List<IPopup>?) {
        mPopups = popups
        refresh()
    }

    fun setLimitTime(startHour: Int, endHour: Int) {
        require(startHour < endHour) { "start hour must before end hour" }
        mStartHour = startHour
        mEndHour = endHour
        refresh()
    }

    /**
     * @param decorator decoration for draw event, popup, time
     */
    fun setDecorator(@NonNull decorator: CdvDecoration?) {
        decoration = decorator
        refresh()
    }
}