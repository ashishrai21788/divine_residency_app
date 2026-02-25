package com.app.core.utils

interface DialogButtonClickListener<T> {

    fun onNegativeButtonClick(position: Int, type: String, data: T?)

    fun onPositiveButtonClick(position: Int, type: String, data: T?)
}