package com.app.core.views

import android.content.Context
import android.util.AttributeSet
import android.view.KeyEvent
import android.widget.AutoCompleteTextView

class CoreAutoCompleteEditText : AutoCompleteTextView {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle)

    override fun onKeyPreIme(keyCode: Int, event: KeyEvent): Boolean {
        if (event.keyCode == KeyEvent.KEYCODE_BACK) {
            //setText("", true)
            clearFocus()
        }
        return super.dispatchKeyEvent(event)
    }
}

