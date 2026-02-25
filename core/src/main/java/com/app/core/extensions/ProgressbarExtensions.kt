package com.app.core.extensions

import android.widget.ProgressBar

fun ProgressBar.changeColor(color: String?) {
    this.indeterminateDrawable?.setColorFilter(color.getColor(), android.graphics.PorterDuff.Mode.SRC_IN)
}

fun ProgressBar.changeColor(color: Int) {
    this.indeterminateDrawable?.setColorFilter(color, android.graphics.PorterDuff.Mode.SRC_IN)
}

