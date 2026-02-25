package com.app.core.extensions

import android.content.Context
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Patterns
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData


fun EditText.hideKeyboard() {
    try {
        clearFocus()
        val `in` = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        `in`.hideSoftInputFromWindow(this.windowToken, 0)
    } catch (e: Throwable) {
        e.printStackTrace()
    }

}

fun EditText.showKeyboard() {
    try {
        this.requestFocus()
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
        imm?.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
    } catch (e: Throwable) {
        e.printStackTrace()
    }

}

//================================= Second layout =====================================
fun isValidEmail(target: CharSequence?): Boolean {
    return !TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches()
}

fun isValidMobile(phone: String?): Boolean {
    return Patterns.PHONE.matcher(phone).matches()
}

fun <String> LiveData<String>.isEmpty(): Boolean {
    return (this.value == null || this.value.toString().trim() == "")
}

//=====================================================================================

fun distance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
    val theta = lon1 - lon2
    var dist = (Math.sin(deg2rad(lat1))
            * Math.sin(deg2rad(lat2))
            + (Math.cos(deg2rad(lat1))
            * Math.cos(deg2rad(lat2))
            * Math.cos(deg2rad(theta))))
    dist = Math.acos(dist)
    dist = rad2deg(dist)
    dist = dist * 60 * 1.1515
    return dist
}

fun deg2rad(deg: Double): Double {
    return deg * Math.PI / 180.0
}

fun rad2deg(rad: Double): Double {
    return rad * 180.0 / Math.PI
}


fun EditText.setCursorColor(color: Int) {
    try {
        // TODO backup
//        if (Build.VERSION.SDK_INT > 28) {
//            val cursor = ContextCompat.getDrawable(context, R.drawable.core_edit_cursor_bg) as? GradientDrawable
//            cursor?.setColor(color)
//            cursor?.let {
//                this.textCursorDrawable = it
//            }
//            return
//        }

        val fCursorDrawableRes = TextView::class.java.getDeclaredField("mCursorDrawableRes")
        fCursorDrawableRes.isAccessible = true
        val mCursorDrawableRes = fCursorDrawableRes.getInt(this)
        val fEditor = TextView::class.java.getDeclaredField("mEditor")
        fEditor.isAccessible = true
        val editor = fEditor.get(this)
        val clazz = editor.javaClass
        val fCursorDrawable = clazz.getDeclaredField("mCursorDrawable")
        fCursorDrawable.isAccessible = true
        val drawables = arrayOfNulls<Drawable>(2)
        drawables[0] = ContextCompat.getDrawable(context, mCursorDrawableRes)
        drawables[1] = ContextCompat.getDrawable(context, mCursorDrawableRes)
        drawables[0]?.setColorFilter(color, PorterDuff.Mode.SRC_IN)
        drawables[1]?.setColorFilter(color, PorterDuff.Mode.SRC_IN)
        fCursorDrawable.set(editor, drawables)
    } catch (ignored: Throwable) {
        ignored.printStackTrace()
    }

}

fun EditText.moveCursorToLast() {
    try {
        this.setSelection(text?.length ?: 0)
    } catch (e: Exception) {
    }
}

fun EditText.onChange(cb: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            cb(s?.toString() ?: "")
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    })
}