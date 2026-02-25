package com.app.core.views.autoscroll

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class CoreOnScrollListener(
    private val layoutManager: LinearLayoutManager,
    private val callback: CoreCenterItemCallback,
    private val controlState: Int
) : RecyclerView.OnScrollListener() {

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        callback.onScrolled(dx)
    }

    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        if (newState == controlState) {
            val visibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            callback.onScrollFinished(visibleItemPosition)
        }
    }
}