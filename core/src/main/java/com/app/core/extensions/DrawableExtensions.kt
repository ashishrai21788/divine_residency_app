package com.app.core.extensions

import android.graphics.*
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.RectShape
import android.os.Build
import androidx.annotation.RequiresApi

fun Drawable.changeDrawableColor(newColor: Int): Drawable {
    this.colorFilter = PorterDuffColorFilter(newColor, PorterDuff.Mode.SRC_IN)
    return this
}

fun Drawable.changeDrawableColor(newColor: String?): Drawable {
    return changeDrawableColor(newColor.getColor())
}

fun getRectangularShapeWithStroke(radius: Float, strokeWidth: Int?, strokeColor: Int?, bgColor: Int?): Drawable {
    val gradientDrawable = GradientDrawable()
    gradientDrawable.shape = GradientDrawable.RECTANGLE
    gradientDrawable.setStroke(strokeWidth ?: 1, strokeColor ?: "#00000000".getColor())
    gradientDrawable.gradientType = GradientDrawable.LINEAR_GRADIENT
    gradientDrawable.cornerRadius = radius
    gradientDrawable.setColor(bgColor ?: "#00000000".getColor())
    gradientDrawable.setGradientCenter(0.0f, 0.0f)
    return gradientDrawable
}

fun getRoundedShape(bgColor: Int?): Drawable {
    val gradientDrawable = GradientDrawable()
    gradientDrawable.shape = GradientDrawable.OVAL
    gradientDrawable.gradientType = GradientDrawable.LINEAR_GRADIENT
    gradientDrawable.cornerRadius = 60f
    gradientDrawable.setColor(bgColor ?: "#00000000".getColor())
    gradientDrawable.setGradientCenter(0.0f, 0.0f)
    return gradientDrawable
}

fun getRectangularShape(bgColor: Int?): Drawable {
    val gradientDrawable = GradientDrawable()
    gradientDrawable.shape = GradientDrawable.RECTANGLE
    gradientDrawable.gradientType = GradientDrawable.LINEAR_GRADIENT
    gradientDrawable.cornerRadius = 5f
    gradientDrawable.setColor(bgColor ?: "#00000000".getColor())
    gradientDrawable.setGradientCenter(0.0f, 0.0f)
    return gradientDrawable
}

fun getRectangularShapeWithStroke(radius: Float, strokeWidth: Int, strokeColor: Int, bgColor: Int, isDotRequired: Boolean): Drawable {
    val gradientDrawable = GradientDrawable()
    gradientDrawable.shape = GradientDrawable.RECTANGLE
    if (isDotRequired) {
        gradientDrawable.setStroke(strokeWidth, strokeColor, 10f, 10f) //black border with full opacity
    } else {
        gradientDrawable.setStroke(strokeWidth, strokeColor) //black border with full opacity
    }
    gradientDrawable.gradientType = GradientDrawable.LINEAR_GRADIENT
    gradientDrawable.cornerRadius = radius
    gradientDrawable.setColor(bgColor)
    gradientDrawable.setGradientCenter(0.0f, 0.0f)
    return gradientDrawable
}

fun getLineWithDottedShape(strokeColor: Int, strokeWidth: Float? = 0f): Drawable {
    val float = FloatArray(2)
    float[0] = 15f
    float[1] = 20f
    val sd = ShapeDrawable(RectShape())
    val fgPaintSel = sd.paint
    fgPaintSel.color = strokeColor
    fgPaintSel.style = Paint.Style.STROKE
    fgPaintSel.strokeWidth = strokeWidth ?: 0f
    fgPaintSel.pathEffect = DashPathEffect(float, 2f)
    return sd
}


//var sd = ShapeDrawable(RectShape())
//var fgPaintSel = sd.getPaint()
//fgPaintSel.setColor(Color.BLACK)
//fgPaintSel.setStyle(Paint.Style.STROKE)
//fgPaintSel.setPathEffect(DashPathEffect(float[]{ 5, 10 }, 0))
//return sd


fun getRectangularShape(radius: Float, strokeColor: Int?, bgColor: Int?): Drawable {
    val gradientDrawable = GradientDrawable()
    gradientDrawable.shape = GradientDrawable.RECTANGLE
    gradientDrawable.setStroke(3, strokeColor ?: "#00000000".getColor())
    gradientDrawable.gradientType = GradientDrawable.LINEAR_GRADIENT
    gradientDrawable.cornerRadius = radius
    gradientDrawable.setColor(bgColor ?: "#00000000".getColor())
    return gradientDrawable
}

fun getRoundedShape(radius: Float, strokeColor: Int?, bgColor: Int?): Drawable {
    val gradientDrawable = GradientDrawable()
    gradientDrawable.shape = GradientDrawable.OVAL
    gradientDrawable.setStroke(3, strokeColor ?: "#00000000".getColor())
    gradientDrawable.gradientType = GradientDrawable.LINEAR_GRADIENT
    gradientDrawable.cornerRadius = radius
    gradientDrawable.setColor(bgColor ?: "#00000000".getColor())
    gradientDrawable.setGradientCenter(0.0f, 0.0f)
    return gradientDrawable
}

fun getRoundedShapeWithCustomStroke(radius: Float, strokeWidth: Int, strokeColor: Int?, bgColor: Int?): Drawable {
    val gradientDrawable = GradientDrawable()
    gradientDrawable.shape = GradientDrawable.OVAL
    gradientDrawable.setStroke(strokeWidth, strokeColor ?: "#00000000".getColor())
    gradientDrawable.gradientType = GradientDrawable.LINEAR_GRADIENT
    gradientDrawable.cornerRadius = radius
    gradientDrawable.setColor(bgColor ?: "#00000000".getColor())
    gradientDrawable.setGradientCenter(0.0f, 0.0f)
    return gradientDrawable
}

fun getPartialRoundedShape(
    topLeftRadius: Float,
    topRightRadius: Float,
    bottomRightRadius: Float,
    bottomLeftRadius: Float,
    strokeColor: Int?,
    bgColor: Int?
): Drawable {
    val gradientDrawable = GradientDrawable()
    gradientDrawable.shape = GradientDrawable.RECTANGLE
    gradientDrawable.setStroke(3, strokeColor ?: "#00000000".getColor())
    gradientDrawable.gradientType = GradientDrawable.LINEAR_GRADIENT
    gradientDrawable.cornerRadii = floatArrayOf(
        topLeftRadius,
        topLeftRadius,
        topRightRadius,
        topRightRadius,
        bottomRightRadius,
        bottomRightRadius,
        bottomLeftRadius,
        bottomLeftRadius
    )
    gradientDrawable.setColor(bgColor ?: "#00000000".getColor())
//        gradientDrawable.setGradientCenter(0.0f, 0.0f)

    return gradientDrawable
}

fun getPartialStrokeRoundedShape(
    topLeftRadius: Float,
    topRightRadius: Float,
    bottomRightRadius: Float,
    bottomLeftRadius: Float,
    strokeColor: Int?,
    bgColor: Int?,
    strokeLessSide: String
): Drawable {

    val borderDrawable = GradientDrawable()
    borderDrawable.shape = GradientDrawable.RECTANGLE
    borderDrawable.gradientType = GradientDrawable.LINEAR_GRADIENT
    borderDrawable.cornerRadii = floatArrayOf(
        topLeftRadius,
        topLeftRadius,
        topRightRadius,
        topRightRadius,
        bottomRightRadius,
        bottomRightRadius,
        bottomLeftRadius,
        bottomLeftRadius
    )
    borderDrawable.setColor(strokeColor ?: "#00000000".getColor())

    val middleDrawable = GradientDrawable()
    middleDrawable.shape = GradientDrawable.RECTANGLE
    middleDrawable.gradientType = GradientDrawable.LINEAR_GRADIENT
    middleDrawable.cornerRadii = floatArrayOf(
        topLeftRadius,
        topLeftRadius,
        topRightRadius,
        topRightRadius,
        bottomRightRadius,
        bottomRightRadius,
        bottomLeftRadius,
        bottomLeftRadius
    )
    middleDrawable.setColor(bgColor ?: Color.TRANSPARENT)

    val backgroundDrawable = GradientDrawable()
    backgroundDrawable.shape = GradientDrawable.RECTANGLE
    backgroundDrawable.gradientType = GradientDrawable.LINEAR_GRADIENT
    backgroundDrawable.cornerRadii = floatArrayOf(
        topLeftRadius,
        topLeftRadius,
        topRightRadius,
        topRightRadius,
        bottomRightRadius,
        bottomRightRadius,
        bottomLeftRadius,
        bottomLeftRadius
    )
    backgroundDrawable.setColor(bgColor ?: "#00000000".getColor())

    val shapeDrawable = ShapeDrawable()
    shapeDrawable.paint.color = strokeColor ?: "#00000000".getColor()

    val layers = arrayOf(borderDrawable, middleDrawable, backgroundDrawable)
    val layerDrawable = LayerDrawable(layers)

    when (strokeLessSide) {
        "left" -> {
            layerDrawable.setLayerInset(0, 0, 0, 0, 0)
            layerDrawable.setLayerInset(1, 0, 3, 3, 3)
            layerDrawable.setLayerInset(2, 0, 3, 3, 3)
        }
        "right" -> {
            layerDrawable.setLayerInset(0, 0, 0, 0, 0)
            layerDrawable.setLayerInset(1, 3, 3, 0, 3)
            layerDrawable.setLayerInset(2, 3, 3, 0, 3)
        }
        "top" -> {
            layerDrawable.setLayerInset(0, 0, 0, 0, 0)
            layerDrawable.setLayerInset(1, 3, 0, 3, 3)
            layerDrawable.setLayerInset(2, 3, 0, 3, 3)
        }
        "bottom" -> {
            layerDrawable.setLayerInset(0, 0, 0, 0, 0)
            layerDrawable.setLayerInset(1, 3, 3, 3, 0)
            layerDrawable.setLayerInset(2, 3, 3, 3, 0)
        }
        "none" -> {     // four sided border
            layerDrawable.setLayerInset(0, 0, 0, 0, 0)
            layerDrawable.setLayerInset(1, 3, 3, 3, 3)
            layerDrawable.setLayerInset(2, 3, 3, 3, 3)
        }
        else -> {    //  four sided border
            layerDrawable.setLayerInset(0, 0, 0, 0, 0)
            layerDrawable.setLayerInset(1, 3, 3, 3, 3)
            layerDrawable.setLayerInset(2, 3, 3, 3, 3)
        }
    }

    return layerDrawable
}

fun Drawable.setColorFilter(color: Int, mode: Mode = Mode.SRC_ATOP) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        colorFilter = BlendModeColorFilter(color, mode.getBlendMode())
    } else {
        @Suppress("DEPRECATION")
        setColorFilter(color, mode.getPorterDuffMode())
    }
}

// This class is needed to call the setColorFilter with different BlendMode on older API (before 29).
enum class Mode {
    CLEAR,
    SRC,
    DST,
    SRC_OVER,
    DST_OVER,
    SRC_IN,
    DST_IN,
    SRC_OUT,
    DST_OUT,
    SRC_ATOP,
    DST_ATOP,
    XOR,
    DARKEN,
    LIGHTEN,
    MULTIPLY,
    SCREEN,
    ADD,
    OVERLAY;

    @RequiresApi(Build.VERSION_CODES.Q)
    fun getBlendMode(): BlendMode =
        when (this) {
            CLEAR -> BlendMode.CLEAR
            SRC -> BlendMode.SRC
            DST -> BlendMode.DST
            SRC_OVER -> BlendMode.SRC_OVER
            DST_OVER -> BlendMode.DST_OVER
            SRC_IN -> BlendMode.SRC_IN
            DST_IN -> BlendMode.DST_IN
            SRC_OUT -> BlendMode.SRC_OUT
            DST_OUT -> BlendMode.DST_OUT
            SRC_ATOP -> BlendMode.SRC_ATOP
            DST_ATOP -> BlendMode.DST_ATOP
            XOR -> BlendMode.XOR
            DARKEN -> BlendMode.DARKEN
            LIGHTEN -> BlendMode.LIGHTEN
            MULTIPLY -> BlendMode.MULTIPLY
            SCREEN -> BlendMode.SCREEN
            ADD -> BlendMode.PLUS
            OVERLAY -> BlendMode.OVERLAY
        }

    fun getPorterDuffMode(): PorterDuff.Mode =
        when (this) {
            CLEAR -> PorterDuff.Mode.CLEAR
            SRC -> PorterDuff.Mode.SRC
            DST -> PorterDuff.Mode.DST
            SRC_OVER -> PorterDuff.Mode.SRC_OVER
            DST_OVER -> PorterDuff.Mode.DST_OVER
            SRC_IN -> PorterDuff.Mode.SRC_IN
            DST_IN -> PorterDuff.Mode.DST_IN
            SRC_OUT -> PorterDuff.Mode.SRC_OUT
            DST_OUT -> PorterDuff.Mode.DST_OUT
            SRC_ATOP -> PorterDuff.Mode.SRC_ATOP
            DST_ATOP -> PorterDuff.Mode.DST_ATOP
            XOR -> PorterDuff.Mode.XOR
            DARKEN -> PorterDuff.Mode.DARKEN
            LIGHTEN -> PorterDuff.Mode.LIGHTEN
            MULTIPLY -> PorterDuff.Mode.MULTIPLY
            SCREEN -> PorterDuff.Mode.SCREEN
            ADD -> PorterDuff.Mode.ADD
            OVERLAY -> PorterDuff.Mode.OVERLAY
        }
}