package com.looperex.calenderview.calendardayview.decoration

import android.content.Context

import android.graphics.Rect
import com.looperex.calenderview.calendardayview.DayView
import com.looperex.calenderview.calendardayview.EventView
import com.looperex.calenderview.calendardayview.PopupView
import com.looperex.calenderview.calendardayview.data.IEvent
import com.looperex.calenderview.calendardayview.data.IPopup



class CdvDecorationDefault(protected var mContext: Context) : CdvDecoration {
    protected var mEventClickListener: EventView.OnEventClickListener? = null
    protected var mPopupClickListener: PopupView.OnEventPopupClickListener? = null
    override fun getEventView(
        event: IEvent, eventBound: Rect, hourHeight: Int,
        separateHeight: Int
    ): EventView {
        val eventView = EventView(mContext)
        eventView.setEvent(event)
        eventView.setPosition(eventBound, -hourHeight, hourHeight - separateHeight * 2)
        eventView.setOnEventClickListener(mEventClickListener)
        return eventView
    }

    override fun getPopupView(
        popup: IPopup,
        eventBound: Rect,
        hourHeight: Int,
        seperateH: Int
    ): PopupView {
        val view = PopupView(mContext)
        view.setOnPopupClickListener(mPopupClickListener)
        view.setPopup(popup)
        view.setPosition(eventBound, -hourHeight / 4 + seperateH, hourHeight / 2 - seperateH * 2)
        return view
    }



    override fun getDayView(hour: Int): DayView {
        val dayView = DayView(mContext)
        dayView.setText(String.format("%1$2s:00", hour))
        return dayView
    }

    fun setOnEventClickListener(listener: EventView.OnEventClickListener?) {
        mEventClickListener = listener
    }

    fun setOnPopupClickListener(listener: PopupView.OnEventPopupClickListener?) {
        mPopupClickListener = listener
    }
}