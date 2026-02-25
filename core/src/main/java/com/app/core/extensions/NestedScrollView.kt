package com.app.core.extensions

import android.graphics.Point
import android.view.View
import android.view.ViewGroup
import android.view.ViewParent
import androidx.core.widget.NestedScrollView


fun NestedScrollView.scrollToView(_view: View?) {
    _view?.let { view ->
        val childOffset = Point()
        getDeepChildOffset(this, view.parent, view, childOffset)
        // Scroll to child.
        this.smoothScrollTo(0, childOffset.y)
    }
}

private fun getDeepChildOffset(mainParent: ViewGroup, parent: ViewParent, child: View, accumulatedOffset: Point) {
    val parentGroup = parent as ViewGroup
    accumulatedOffset.x += child.left
    accumulatedOffset.y += child.top
    if (parentGroup == mainParent) {
        return
    }
    getDeepChildOffset(mainParent, parentGroup.parent, parentGroup, accumulatedOffset)
}

fun NestedScrollView.scrollToBottom() {
    kotlin.runCatching {
        this.smoothScrollTo(0, this.bottom)
    }
}

/*fun RecyclerView.scrollToBottom(){
    kotlin.runCatching {
        this.scrollToBottom()
    }
}*/
