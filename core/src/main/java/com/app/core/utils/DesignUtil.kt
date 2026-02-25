package com.app.core.utils

import android.graphics.Point
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.InsetDrawable

object DesignUtil {

    @JvmStatic
    fun getRectangularShape(
        radius: Float,
        strokeColor: Int,
        bgColor: Int,
        storkeSize: Int
    ): Drawable {
        val gradientDrawable = GradientDrawable()
        gradientDrawable.shape = GradientDrawable.RECTANGLE
        gradientDrawable.setStroke(storkeSize, strokeColor)
        gradientDrawable.gradientType = GradientDrawable.LINEAR_GRADIENT
        gradientDrawable.cornerRadius = radius
        gradientDrawable.setColor(bgColor)
        gradientDrawable.setGradientCenter(0.0f, 0.0f)
        return gradientDrawable
    }

    @JvmStatic
    fun getRectangularMoreShape(
        radius: Array<Float>,
        strokeColor: Int,
        bgColor: Int,
        storkeSize: Int
    ): Drawable {
        val gradientDrawable = GradientDrawable()
        gradientDrawable.shape = GradientDrawable.RECTANGLE
        gradientDrawable.setStroke(storkeSize, strokeColor)
        gradientDrawable.gradientType = GradientDrawable.LINEAR_GRADIENT
        gradientDrawable.cornerRadii = floatArrayOf(
            radius[0],
            radius[1],
            radius[2],
            radius[3],
            radius[4],
            radius[5],
            radius[6],
            radius[7]
        )
        gradientDrawable.setColor(bgColor)
        gradientDrawable.setGradientCenter(0.0f, 0.0f)
        return gradientDrawable
    }

    @JvmStatic
    fun getRectangularShapeWithTopCorner(
        radius: Float,
        strokeColor: Int,
        bgColor: Int,
        storkeSize: Int
    ): Drawable {
        val gradientDrawable = GradientDrawable()
        gradientDrawable.shape = GradientDrawable.RECTANGLE
        gradientDrawable.setStroke(storkeSize, strokeColor)
        gradientDrawable.gradientType = GradientDrawable.LINEAR_GRADIENT
        gradientDrawable.cornerRadii = floatArrayOf(radius, radius, radius, radius, 0f, 0f, 0f, 0f)
        gradientDrawable.setColor(bgColor)
        gradientDrawable.setGradientCenter(0.0f, 0.0f)
        return gradientDrawable
    }

    @JvmStatic
    fun getRectangularShapeWithRightCorner(
        radius: Float,
        strokeColor: Int,
        bgColor: Int,
        storkeSize: Int
    ): Drawable {
        val gradientDrawable = GradientDrawable()
        gradientDrawable.shape = GradientDrawable.RECTANGLE
        gradientDrawable.setStroke(storkeSize, strokeColor)
        gradientDrawable.gradientType = GradientDrawable.LINEAR_GRADIENT
        gradientDrawable.cornerRadii = floatArrayOf(0f, 0f, radius, radius, radius, radius, 0f, 0f)
        gradientDrawable.setColor(bgColor)
        gradientDrawable.setGradientCenter(0.0f, 0.0f)
        return gradientDrawable
    }
    @JvmStatic
    fun getRectangularShapeWithBottomCorner(
        radius: Float,
        strokeColor: Int,
        bgColor: Int,
        storkeSize: Int
    ): Drawable {
        val gradientDrawable = GradientDrawable()
        gradientDrawable.shape = GradientDrawable.RECTANGLE
        gradientDrawable.setStroke(storkeSize, strokeColor)
        gradientDrawable.gradientType = GradientDrawable.LINEAR_GRADIENT
        gradientDrawable.cornerRadii = floatArrayOf(0f, 0f, 0f, 0f, radius, radius, radius, radius)
        gradientDrawable.setColor(bgColor)
        gradientDrawable.setGradientCenter(0.0f, 0.0f)
        return InsetDrawable(gradientDrawable, 0, radius.toInt() * -1, radius.toInt() * -1, 5)
    }

    @JvmStatic
    fun getRectangularWithRoundCorner(
        radius: Float,
        strokeColor: Int,
        bgColor: Int,
        storkeSize: Int
    ): Drawable {
        val gradientDrawable = GradientDrawable()
        gradientDrawable.shape = GradientDrawable.RECTANGLE
        gradientDrawable.setStroke(storkeSize, strokeColor)
        gradientDrawable.gradientType = GradientDrawable.LINEAR_GRADIENT
        gradientDrawable.cornerRadii = floatArrayOf(0f, 0f, 0f, 0f, radius, radius, radius, radius)
        gradientDrawable.setColor(bgColor)
        gradientDrawable.setGradientCenter(0.0f, 0.0f)
        return InsetDrawable(gradientDrawable, 0, radius.toInt() * -1, radius.toInt() * -1, 5)
    }

    @JvmStatic
    fun getCircularBg(
        strokeColor: Int,
        bgColor: Int,
        strokeSize: Int,
        size: Point? = null
    ): Drawable {
        val gradientDrawable = GradientDrawable()
        gradientDrawable.shape = GradientDrawable.OVAL
        size?.let {
            gradientDrawable.setSize(it.x, it.y)
        }
        gradientDrawable.setStroke(strokeSize, strokeColor)
        gradientDrawable.setColor(bgColor)
        return gradientDrawable
    }

    @JvmStatic
    fun getShadowBackground(startColor: Int, endColor: Int): Drawable {
        val colors = intArrayOf(startColor, endColor)
        return GradientDrawable(
            GradientDrawable.Orientation.TOP_BOTTOM, colors
        ).also {
            it.gradientType = GradientDrawable.LINEAR_GRADIENT
        }
    }
}