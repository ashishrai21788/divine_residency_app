package com.app.divine.ui.common

import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.app.divine.R

object StatusChipHelper {

    fun apply(textView: TextView, status: String) {
        textView.visibility = View.VISIBLE
        val s = status.trim().uppercase()
        textView.text = s.ifEmpty { "—" }
        val (bgRes, textColorRes) = when {
            s.contains("PAID") && !s.contains("UNPAID") ->
                R.drawable.bg_status_chip_paid to R.color.villa_tag_text_paid
            s.contains("REJECT") || s.contains("DENIED") ->
                R.drawable.bg_status_chip_rejected to R.color.villa_tag_text_rejected
            s.contains("APPROV") ->
                R.drawable.bg_status_chip_approved to R.color.villa_tag_text_approved
            s.contains("PENDING") || s.contains("UNPAID") || s.contains("OVERDUE") ->
                R.drawable.bg_status_chip_pending to R.color.villa_tag_text_pending
            else ->
                R.drawable.bg_status_chip_default to R.color.villa_tag_text_default
        }
        textView.setBackgroundResource(bgRes)
        textView.setTextColor(ContextCompat.getColor(textView.context, textColorRes))
    }
}
