package com.app.divine.ui.common

import android.view.View
import android.widget.TextView
import com.app.divine.R

/** Included [R.layout.view_empty_state] is the root [View]; use this instead of nested binding fields. */
fun View.bindEmptyState(title: CharSequence, subtitle: CharSequence = "") {
    findViewById<TextView>(R.id.empty_title)?.text = title
    findViewById<TextView>(R.id.empty_subtitle)?.text = subtitle
}
