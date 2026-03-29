package com.app.divine.ui.common

import android.content.Context
import android.view.LayoutInflater
import android.widget.TextView
import com.app.divine.R
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButton

object VisitorApprovalBottomSheet {

    fun show(
        context: Context,
        visitorName: String,
        detail: String,
        approve: Boolean,
        onConfirm: () -> Unit,
    ) {
        val view = LayoutInflater.from(context).inflate(R.layout.bottom_sheet_visitor_approval, null)
        view.findViewById<TextView>(R.id.approval_sheet_title).text =
            if (approve) "Approve visitor?" else "Reject visitor?"
        view.findViewById<TextView>(R.id.approval_sheet_subtitle).text =
            buildString {
                append(visitorName)
                if (detail.isNotBlank()) {
                    append('\n')
                    append(detail)
                }
            }
        val dialog = BottomSheetDialog(context)
        dialog.setContentView(view)
        view.findViewById<MaterialButton>(R.id.approval_sheet_confirm).apply {
            text = if (approve) "Approve" else "Reject"
            setOnClickListener {
                onConfirm()
                dialog.dismiss()
            }
        }
        view.findViewById<MaterialButton>(R.id.approval_sheet_cancel).setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }
}
