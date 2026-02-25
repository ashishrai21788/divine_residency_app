package com.app.core.utils

interface AlertDialogListener {
    fun <T> onOkClick(type: String, obj: T)

}