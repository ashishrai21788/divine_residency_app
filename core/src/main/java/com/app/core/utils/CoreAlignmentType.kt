package com.app.core.utils

import android.view.Gravity
import com.app.core.extensions.isRTLLocale

enum class CoreAlignmentType {

    LEFT {
        override fun value() = "left"
    },
    CENTER {
        override fun value() = "center"
    },
    MIDDLE {
        override fun value() = "middle"
    },
    RIGHT {
        override fun value() = "right"
    },
    JUSTIFY {
        override fun value() = "justify"
    },
    TOP {
        override fun value() = "top"
    },
    BOTTOM {
        override fun value() = "bottom"
    };


    abstract fun value(): String

    fun toGravity(): Int {
        return when (this) {
            LEFT -> Gravity.START
            RIGHT -> Gravity.END
            CENTER -> Gravity.CENTER
            MIDDLE -> Gravity.CENTER
            JUSTIFY -> Gravity.START
            TOP -> Gravity.TOP
            BOTTOM -> Gravity.BOTTOM
        }
    }

    companion object Factory {
        fun parse(type: String?): CoreAlignmentType {
            return when (type) {
                LEFT.value() -> if (isRTLLocale()) RIGHT else LEFT
                CENTER.value() -> CENTER
                MIDDLE.value() -> MIDDLE
                RIGHT.value() -> if (isRTLLocale()) LEFT else RIGHT
                JUSTIFY.value() -> JUSTIFY
                TOP.value() -> TOP
                BOTTOM.value() -> BOTTOM
                else -> {
                    LEFT
                }
            }
        }
    }
}