package com.app.core.extensions

interface AlertDialogListernerLink {
    fun <T> onOkClick(type: String, obj: T)
    fun <T> onViewClick(type: String, obj: T)
    fun <T> onOkClickWithExtraParam(type: String, obj: T, extraParam: T) {}
}