package com.app.core.extensions

import android.content.res.ColorStateList
import androidx.appcompat.widget.AppCompatCheckBox
import androidx.appcompat.widget.AppCompatRadioButton


fun AppCompatRadioButton.setColorStyle(defaultColor: String?, selectedColor: String?) {
    val defaultColorValue = defaultColor.getColor()
    val selectedColorValue = selectedColor.getColor()
    val colorStateList = ColorStateList(
        arrayOf(
            intArrayOf(-android.R.attr.state_checked), // unchecked
            intArrayOf(android.R.attr.state_checked)  // checked
        ),
        intArrayOf(defaultColorValue, selectedColorValue)
    )
    this.supportButtonTintList = colorStateList
}

fun AppCompatRadioButton.setColorStyle(defaultColor: Int, selectedColor: Int) {

    val colorStateList = ColorStateList(
        arrayOf(
            intArrayOf(-android.R.attr.state_checked), // unchecked
            intArrayOf(android.R.attr.state_checked)  // checked
        ),
        intArrayOf(defaultColor, selectedColor)
    )
    this.supportButtonTintList = colorStateList
}


fun AppCompatCheckBox.setColorStyle(defaultColor: String?, selectedColor: String?) {
    val defaultColorValue = defaultColor.getColor()
    val selectedColorValue = selectedColor.getColor()
    val colorStateList = ColorStateList(
        arrayOf(
            intArrayOf(-android.R.attr.state_checked), // unchecked
            intArrayOf(android.R.attr.state_checked)  // checked
        ),
        intArrayOf(defaultColorValue, selectedColorValue)
    )
    this.supportButtonTintList = colorStateList
}

fun AppCompatCheckBox.setColorStyle(defaultColor: Int, selectedColor: Int) {

    val colorStateList = ColorStateList(
        arrayOf(
            intArrayOf(-android.R.attr.state_checked), // unchecked
            intArrayOf(android.R.attr.state_checked)  // checked
        ),
        intArrayOf(defaultColor, selectedColor)
    )
    this.supportButtonTintList = colorStateList
}

