package com.app.core.utils

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.View

import androidx.appcompat.widget.AppCompatTextView

open class HSLocaleAwareTextView : AppCompatTextView {
    constructor(context: Context) : super(context) {
        gravity = if (resources.configuration.layoutDirection == View.LAYOUT_DIRECTION_RTL) Gravity.RIGHT else Gravity.LEFT
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        gravity = if (resources.configuration.layoutDirection == View.LAYOUT_DIRECTION_RTL) Gravity.RIGHT else Gravity.LEFT
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        gravity = if (resources.configuration.layoutDirection == View.LAYOUT_DIRECTION_RTL) Gravity.RIGHT else Gravity.LEFT
    }

}
