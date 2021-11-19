package com.looperex.calenderview.calendardayview.data


interface IPopup : ITimeDuration {
    val title: String?
    val description: String?
    val isAutohide: Boolean?
}