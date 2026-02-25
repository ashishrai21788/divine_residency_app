package com.app.core.extensions

import android.graphics.Color
import androidx.core.graphics.ColorUtils

fun Int.darker(factor: Float): Int {
    val a = Color.alpha(this)
    val r = Color.red(this)
    val g = Color.green(this)
    val b = Color.blue(this)

    return Color.argb(
        a,
        Math.max((r * factor).toInt(), 0),
        Math.max((g * factor).toInt(), 0),
        Math.max((b * factor).toInt(), 0)
    )
}

fun Int.transparent(factor: Float?): Int {
    val validFactor = factor ?: return this
    if (this == Color.TRANSPARENT) return this
    return ColorUtils.setAlphaComponent(this, (255 * validFactor).toInt())
}

fun Int.lighter(factor: Float): Int {
    val red = ((Color.red(this) * (1 - factor) / 255 + factor) * 255).toInt()
    val green = ((Color.green(this) * (1 - factor) / 255 + factor) * 255).toInt()
    val blue = ((Color.blue(this) * (1 - factor) / 255 + factor) * 255).toInt()
    return Color.argb(Color.alpha(this), red, green, blue)
}