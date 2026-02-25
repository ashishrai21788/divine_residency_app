package com.app.core.views.autoscroll


interface CoreCenterItemCallback {
    fun onScrollFinished(visibleItemPosition: Int)
    fun onScrolled(dx: Int)
}