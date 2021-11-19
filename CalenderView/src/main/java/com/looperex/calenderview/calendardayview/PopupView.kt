package com.looperex.calenderview.calendardayview

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.looperex.calenderview.R
import com.looperex.calenderview.calendardayview.data.IPopup
import java.lang.UnsupportedOperationException

class PopupView : FrameLayout {
    private var mPopup: IPopup? = null
    private var mPopupClickListener: OnEventPopupClickListener? = null
    private var mShowDuration = 3000
    private var mQuote: TextView? = null
    private var mCardView: CardView? = null
    private var mImvStart: ImageView? = null
    private var mImvEnd: ImageView? = null
    private var mTitle: TextView? = null
    private var mDescription: TextView? = null
    private var mFadeOutAnim: AlphaAnimation = AlphaAnimation(1.0f, 0.0f)
    private var mFadeOutListener: Animation.AnimationListener =
        object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {}
            override fun onAnimationEnd(animation: Animation) {
                alpha = 1f
                visibility = View.INVISIBLE
            }

            override fun onAnimationRepeat(animation: Animation) {}
        }
    private val mHidePopup = Runnable { hide() }

    constructor(context: Context?) : super(context!!) {
        init(null)
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context!!, attrs) {
        init(attrs)
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context!!,
        attrs,
        defStyleAttr
    ) {
        init(attrs)
    }

    private fun init(attrs: AttributeSet?) {
        LayoutInflater.from(context).inflate(R.layout.view_event_popup, this, true)
        mDescription = findViewById<View>(R.id.desc) as TextView?
        mTitle = findViewById<View>(R.id.title) as TextView?
//        mImvEnd = findViewById<View>(R.id.imv_end) as ImageView?
//        mImvStart = findViewById<View>(R.id.image_start) as ImageView?
//        mQuote = findViewById<View>(R.id.quote) as TextView?
        mCardView = findViewById<View>(R.id.cardview) as CardView?
        mQuote?.setOnClickListener {
            if (mPopupClickListener != null) {
                mPopupClickListener!!.onQuoteClick(this@PopupView, mPopup)
            }
        }
        mCardView!!.setOnClickListener {
            if (mPopupClickListener != null) {
                mPopupClickListener!!.onPopupClick(this@PopupView, mPopup)
            }
        }
    }

    fun show() {
        visibility = View.VISIBLE
        if (mPopup!!.isAutohide == true) {
            removeCallbacks(mHidePopup)
            postDelayed(mHidePopup, mShowDuration.toLong())
        }
    }

    private fun hide() {
        if (mFadeOutAnim.hasStarted() && !mFadeOutAnim.hasEnded()) {
            return
        }
        mFadeOutAnim = AlphaAnimation(alpha, 0.0f)
        visibility = View.VISIBLE
        mFadeOutAnim.setAnimationListener(mFadeOutListener)
        mFadeOutAnim.duration = 200
        startAnimation(mFadeOutAnim)
    }

    fun setPosition(rect: Rect, topMargin: Int, bottomMargin: Int) {
        val params = LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        params.topMargin = rect.top + topMargin
        params.leftMargin = rect.left
        mCardView!!.layoutParams.height = rect.height() + bottomMargin
        layoutParams = params
    }

    fun setPopup(popup: IPopup) {
        mPopup = popup
        mDescription?.text = popup.description.toString()
        mTitle?.text = popup.title
        if (mPopupClickListener != null) {
            mPopupClickListener!!.onLoadData(this@PopupView, mImvStart, mImvEnd, popup)
        }
    }

    override fun setOnClickListener(l: OnClickListener?) {
        throw UnsupportedOperationException("you should use setOnEventClickListener()")
    }

    fun setOnPopupClickListener(listener: OnEventPopupClickListener?) {
        mPopupClickListener = listener
    }

    interface OnEventPopupClickListener {
        fun onPopupClick(view: PopupView?, data: IPopup?)
        fun onQuoteClick(view: PopupView?, data: IPopup?)
        fun onLoadData(view: PopupView?, start: ImageView?, end: ImageView?, data: IPopup?)
    }
}