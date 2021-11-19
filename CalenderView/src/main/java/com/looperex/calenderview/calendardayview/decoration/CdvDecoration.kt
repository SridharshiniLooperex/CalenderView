package com.looperex.calenderview.calendardayview.decoration

import android.graphics.Rect
import com.looperex.calenderview.calendardayview.DayView
import com.looperex.calenderview.calendardayview.EventView
import com.looperex.calenderview.calendardayview.PopupView
import com.looperex.calenderview.calendardayview.data.IEvent
import com.looperex.calenderview.calendardayview.data.IPopup



interface CdvDecoration {
    fun getEventView(
        event: IEvent,
        eventBound: Rect,
        hourHeight: Int,
        seperateHeight: Int
    ): EventView?

    fun getPopupView(
        popup: IPopup,
        eventBound: Rect,
        hourHeight: Int,
        seperateHeight: Int
    ): PopupView?

    fun getDayView(hour: Int): DayView?
}