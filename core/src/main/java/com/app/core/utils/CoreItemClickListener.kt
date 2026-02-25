package com.app.core.utils

interface CoreItemClickListener {
    fun <T> onItemClicked(position: Int, type: String?, data: T?)
}