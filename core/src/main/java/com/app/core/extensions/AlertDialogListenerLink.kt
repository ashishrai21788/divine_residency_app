package com.app.core.extensions

interface AlertDialogListenerLink {
    fun <T> onOkClick(type: String, obj: T)
    fun <T> onViewClick(type: String, obj: T)
    fun <T> onOkClickWithExtraParam(type: String, obj: T, extraParam: T) {}
}
