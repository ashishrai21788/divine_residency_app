package com.app.core.extensions

import android.content.Context
import android.graphics.Typeface
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.TranslateAnimation
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.annotation.LayoutRes
import androidx.appcompat.widget.AppCompatCheckBox
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.google.android.material.textfield.TextInputLayout

//import uk.co.deanwild.flowtextview.FlowTextView

fun View.slideDownToUp() {
    this.visibility = View.VISIBLE
    val animate = TranslateAnimation(0f, 0f, this.height.toFloat(), 0f)
    animate.duration = 500
    animate.fillAfter = true
    this.startAnimation(animate)
}

fun View.slideUpToDown() {
    val animate = TranslateAnimation(0f, 0f, 0f, this.height.toFloat())
    animate.duration = 500
    animate.fillAfter = false
    this.startAnimation(animate)
}

fun View.inflater(): LayoutInflater = LayoutInflater.from(this.context)
fun ViewGroup.inflate(@LayoutRes layout: Int, attachParent: Boolean = false): View =
    LayoutInflater.from(this.context).inflate(layout, this, attachParent)

//debounceTime in ms
fun View.clickWithDebounce(debounceTime: Long = 1000L, action: (view: View) -> Unit) {
    this.setOnClickListener(object : View.OnClickListener {
        private var lastClickTime: Long = 0
        override fun onClick(v: View) {
            if (SystemClock.elapsedRealtime() - lastClickTime < debounceTime) {
                return
            } else {
                action(v)
            }
            lastClickTime = SystemClock.elapsedRealtime()
        }
    })
}

fun <T : ViewDataBinding> ViewGroup.inflateBinding(@LayoutRes layout: Int): T {
    return DataBindingUtil.inflate(inflater(), layout, this, false)
}

fun View.hideSoftKeyboard() {
    try {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

fun View.handleAssetOrGoogleFontStyle(font: Typeface, style: String?, includeFontPadding: Boolean?, isGoogleFont: Boolean = false) {
    when (this) {
        is TextView -> {
            if (isGoogleFont) {
                this.typeface = font
            } else {
                if (style == "bold" || style == "medium") {
                    this.setTypeface(font, Typeface.BOLD)
                } else if (style == "italic") {
                    this.setTypeface(font, Typeface.ITALIC)
                } else if (style == "boldItalic") {
                    this.setTypeface(font, Typeface.BOLD_ITALIC)
                } else {
                    this.typeface = font
                }
            }

            this.includeFontPadding = includeFontPadding == true
        }
        is Button -> {
            if (isGoogleFont) {
                this.typeface = font
            } else {
                if (style == "bold" || style == "medium") {
                    this.setTypeface(font, Typeface.BOLD)
                } else if (style == "italic") {
                    this.setTypeface(font, Typeface.ITALIC)
                } else if (style == "boldItalic") {
                    this.setTypeface(font, Typeface.BOLD_ITALIC)
                } else {
                    this.typeface = font
                }
            }
            this.includeFontPadding = includeFontPadding == true
        }
        is EditText -> {
            if (isGoogleFont) {
                this.typeface = font
            } else {
                if (style == "bold" || style == "medium") {
                    this.setTypeface(font, Typeface.BOLD)
                } else if (style == "italic") {
                    this.setTypeface(font, Typeface.ITALIC)
                } else if (style == "boldItalic") {
                    this.setTypeface(font, Typeface.BOLD_ITALIC)
                } else {
                    this.typeface = font
                }
            }
            this.includeFontPadding = includeFontPadding == true
        }
        is AppCompatCheckBox -> {
            if (isGoogleFont) {
                this.typeface = font
            } else {
                if (style == "bold" || style == "medium") {
                    this.setTypeface(font, Typeface.BOLD)
                } else if (style == "italic") {
                    this.setTypeface(font, Typeface.ITALIC)
                } else if (style == "boldItalic") {
                    this.setTypeface(font, Typeface.BOLD_ITALIC)
                } else {
                    this.typeface = font
                }
            }
            this.includeFontPadding = includeFontPadding == true
        }

//        is FlowTextView -> {
//            this.setTypeface(font)
//        }
        is TextInputLayout -> {
            this.typeface = font
        }
        is CheckBox -> {
            this.typeface = font
        }
        is AutoCompleteTextView->{
            this.typeface = font
        }

    }
}

fun View.mirrorView(isBasedOnLocale: Boolean = true) {
    if (isBasedOnLocale) {
        if (isRTLLocale()) {
            this.scaleX = -1f
            this.scaleY = 1f
            this.translationX = 1f
        }
    } else {
        this.scaleX = -1f
        this.scaleY = 1f
        this.translationX = 1f
    }




}


/**
 * Extension method to set View's height.
 */
fun View.setHeight(value: Int) {
    val lp = layoutParams
    lp?.let {
        lp.height = value
        layoutParams = lp
    }
}

/**
 * Extension method to set View's width.
 */
fun View.setWidth(value: Int) {
    val lp = layoutParams
    lp?.let {
        lp.width = value
        layoutParams = lp
    }
}
/**
 * Extension method to resize View with height & width.
 */
fun View.resize(width: Int, height: Int) {
    val lp = layoutParams
    lp?.let {
        lp.width = width
        lp.height = height
        layoutParams = lp
    }
}

fun View.setMargins(
    left: Int? = null,
    top: Int? = null,
    right: Int? = null,
    bottom: Int? = null
) {
    val lp = layoutParams as? ViewGroup.MarginLayoutParams
        ?: return

    lp.setMargins(
        left ?: lp.leftMargin,
        top ?: lp.topMargin,
        right ?: lp.rightMargin,
        bottom ?: lp.rightMargin
    )

    layoutParams = lp
}