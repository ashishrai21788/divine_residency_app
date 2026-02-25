package com.app.core.extensions

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.widget.*
import com.app.core.R
import com.app.core.utils.AlertDialogListener
import com.app.core.utils.DialogButtonClickListener

const val MAX_LINES: Int = 6
const val VERTICAL_PADDING: Int = 30
const val HORIZONTAL_PADDING: Int = 20

fun Context.showListDialog(
    title: String?,
    type: String,
    dialogButtonClickListener: DialogButtonClickListener<Any>?,
    listData: ArrayList<String>,
    selectedItemList: ArrayList<Boolean>,
    itemPosition: Int
) {

    val alertDialog = Dialog(this)
    alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
    alertDialog.setContentView(R.layout.list_dialog)

    val listLayout = alertDialog.findViewById<LinearLayout>(R.id.list_layout)
    val separatorView = alertDialog.findViewById<View>(R.id.button_separator)
    val buttonLayout = alertDialog.findViewById<LinearLayout>(R.id.mCancelOkBtnView)
    val titleView = alertDialog.findViewById<TextView>(R.id.title)
    val cancelBtn = alertDialog.findViewById<TextView>(R.id.mCancelBtn)
    val okBtn = alertDialog.findViewById<TextView>(R.id.mOkBtn)


    val cancelButtonText =  "Cancel"
    val okButtonText = "Ok"

    cancelBtn.text = cancelButtonText
    okBtn.text = okButtonText

    if (title == null) {
        titleView.visibility = View.GONE
    } else {
        titleView.text = title
    }


    if (type == "checkbox") {
        for (i in listData.indices) {
            val checkbox = CheckBox(this) // dynamically creating CheckBox .
            checkbox.text = listData[i].trim()
            checkbox.setOnClickListener {
                selectedItemList[i] = checkbox.isChecked
            }
            checkbox.setPadding(HORIZONTAL_PADDING, VERTICAL_PADDING, HORIZONTAL_PADDING, VERTICAL_PADDING)
            checkbox.maxLines = MAX_LINES
            checkbox.setTextColor(Color.BLACK)
            listLayout.addView(checkbox)
        }

        for (i in 0 until listLayout.childCount) {
            (listLayout.getChildAt(i) as CheckBox).isChecked = selectedItemList[i]
        }
    } else if (type == "radio") {
        buttonLayout.visibility = View.GONE
        separatorView.visibility = View.INVISIBLE

        val radioGroup = RadioGroup(this)
        for (i in listData.indices) {
            val radioButton = RadioButton(this)

//            var layoutParams = RadioGroup.LayoutParams(RadioGroup.LayoutParams.WRAP_CONTENT, RadioGroup.LayoutParams.WRAP_CONTENT)
//            layoutParams.setMargins(15, 15, 15, 15)
//            radioButton.layoutParams = layoutParams
            radioButton.text = listData[i].trim()

            radioButton.setPadding(HORIZONTAL_PADDING, VERTICAL_PADDING, HORIZONTAL_PADDING, VERTICAL_PADDING)
            radioButton.maxLines = MAX_LINES
            radioButton.setOnClickListener {
                for (position in selectedItemList.indices) {
                    selectedItemList[position] = false
                }
                selectedItemList[i] = radioButton.isChecked

                Handler().postDelayed({
                    okBtn.callOnClick()
                }, 350)
            }
            radioButton.setTextColor(Color.BLACK)
            radioGroup.addView(radioButton)
        }
        listLayout.addView(radioGroup)

        for (i in 0 until (listLayout.getChildAt(0) as RadioGroup).childCount) {
            ((listLayout.getChildAt(0) as RadioGroup).getChildAt(i) as RadioButton).isChecked = selectedItemList[i]
        }

    }

    cancelBtn.setOnClickListener {
        alertDialog.dismiss()
        dialogButtonClickListener?.onNegativeButtonClick(itemPosition, type, selectedItemList)
    }
    okBtn.setOnClickListener {
        alertDialog.dismiss()
        dialogButtonClickListener?.onPositiveButtonClick(itemPosition, type, selectedItemList)
    }
    alertDialog.show()
}

fun Context.showListDialog(
    title: String?,
    type: String,
    dialogButtonClickListener: DialogButtonClickListener<Any>?,
    listData: Int,
    selectedPosition: Int
) {

    val alertDialog = Dialog(this)
    alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
    alertDialog.setContentView(R.layout.list_dialog)
    var itemSelected = 0

    val listLayout = alertDialog.findViewById<LinearLayout>(R.id.list_layout)
    val separatorView = alertDialog.findViewById<View>(R.id.button_separator)
    val buttonLayout = alertDialog.findViewById<LinearLayout>(R.id.mCancelOkBtnView)
    val titleView = alertDialog.findViewById<TextView>(R.id.title)
    val cancelBtn = alertDialog.findViewById<TextView>(R.id.mCancelBtn)
    val okBtn = alertDialog.findViewById<TextView>(R.id.mOkBtn)

    if (title == null) {
        titleView.visibility = View.GONE
    } else {
        titleView.text = title
    }


    buttonLayout.visibility = View.GONE
    separatorView.visibility = View.INVISIBLE

    val radioGroup = RadioGroup(this)
    for (i in 0 until listData) {
        val radioButton = RadioButton(this)
        radioButton.text = "${i + 1}"
        radioButton.setPadding(HORIZONTAL_PADDING, VERTICAL_PADDING, HORIZONTAL_PADDING, VERTICAL_PADDING)
        radioButton.maxLines = MAX_LINES
        radioButton.setOnClickListener {
            itemSelected = i + 1
            Handler().postDelayed({
                okBtn.callOnClick()
            }, 350)
        }
        radioButton.setTextColor(Color.BLACK)
        radioGroup.addView(radioButton)
    }
    listLayout.addView(radioGroup)

    for (i in 0 until (listLayout.getChildAt(0) as RadioGroup).childCount) {
        if (i == selectedPosition) {
            ((listLayout.getChildAt(0) as RadioGroup).getChildAt(i) as RadioButton).isChecked = true
        }
    }

    cancelBtn.setOnClickListener {
        alertDialog.dismiss()
        dialogButtonClickListener?.onNegativeButtonClick(selectedPosition, type, selectedPosition)
    }
    okBtn.setOnClickListener {
        alertDialog.dismiss()
        dialogButtonClickListener?.onPositiveButtonClick(selectedPosition, type, itemSelected)
    }
    alertDialog.show()
}

fun Context.showCustomListDialog(
    title: String?,
    type: String,
    dialogButtonClickListener: DialogButtonClickListener<Any>?,
    listData: ArrayList<String>,
    selectedPosition: Int
) {

    val alertDialog = Dialog(this)
    alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
    alertDialog.setContentView(R.layout.list_dialog)
    var itemSelected = 0

    val listLayout = alertDialog.findViewById<LinearLayout>(R.id.list_layout)
    val separatorView = alertDialog.findViewById<View>(R.id.button_separator)
    val buttonLayout = alertDialog.findViewById<LinearLayout>(R.id.mCancelOkBtnView)
    val titleView = alertDialog.findViewById<TextView>(R.id.title)
    val cancelBtn = alertDialog.findViewById<TextView>(R.id.mCancelBtn)
    val okBtn = alertDialog.findViewById<TextView>(R.id.mOkBtn)

    if (title == null) {
        titleView.visibility = View.GONE
    } else {
        titleView.text = title
    }


    buttonLayout.visibility = View.GONE
    separatorView.visibility = View.INVISIBLE

    val radioGroup = RadioGroup(this)
    for (data in listData) {
        val radioButton = RadioButton(this)
        radioButton.text = data
        radioButton.setPadding(HORIZONTAL_PADDING, VERTICAL_PADDING, HORIZONTAL_PADDING, VERTICAL_PADDING)
        radioButton.maxLines = MAX_LINES
        radioButton.setOnClickListener {
            itemSelected = radioGroup.indexOfChild(radioButton)
            Handler().postDelayed({
                okBtn.callOnClick()
            }, 350)
        }
        radioButton.setTextColor(Color.BLACK)
        radioGroup.addView(radioButton)
    }
    listLayout.addView(radioGroup)

    for (i in 0 until (listLayout.getChildAt(0) as RadioGroup).childCount) {
        if (i == selectedPosition) {
            ((listLayout.getChildAt(0) as RadioGroup).getChildAt(i) as RadioButton).isChecked = true
        }
    }

    cancelBtn.setOnClickListener {
        alertDialog.dismiss()
        dialogButtonClickListener?.onNegativeButtonClick(selectedPosition, type, selectedPosition)
    }
    okBtn.setOnClickListener {
        alertDialog.dismiss()
        dialogButtonClickListener?.onPositiveButtonClick(itemSelected, type, itemSelected)
    }
    alertDialog.show()
}


fun Context.showInfoDialog(
    title: String,
    message: String,
    positiveButtonName: String
) {
    try {
        val dialogBuilder = AlertDialog.Builder(this)

        val inflater = LayoutInflater.from(this)
        val alertLayout = inflater.inflate(R.layout.info_dialog_view, null)
        dialogBuilder.setView(alertLayout)

        val titleTextView = alertLayout.findViewById<TextView>(R.id.tv_title)
        val infoTextView = alertLayout.findViewById<TextView>(R.id.tv_info)
        val okTextView = alertLayout.findViewById<TextView>(R.id.tv_ok)

        if (title.isBlank()) {
            titleTextView.visibility = View.GONE
        } else {
            titleTextView.visibility = View.VISIBLE
            titleTextView.text = title
            titleTextView.typeface = Typeface.DEFAULT_BOLD
        }


        infoTextView.text = message
        okTextView.text = positiveButtonName

        val alertDialog = dialogBuilder.create()
        okTextView.setOnClickListener { alertDialog.dismiss() }
        alertDialog.show()
    } catch (e: Exception) {

    }

}

fun Context.showInfoDialog(
    title: String,
    message: String,
    positiveButtonName: String,
    alertDialogListener: AlertDialogListener,
    isCancelable: Boolean? = false
) {
    val dialogBuilder = AlertDialog.Builder(this)
    dialogBuilder.setCancelable(isCancelable ?: false)

    val inflater = LayoutInflater.from(this)
    val alertLayout = inflater.inflate(R.layout.info_dialog_view, null)
    dialogBuilder.setView(alertLayout)

    val titleTextView = alertLayout.findViewById<TextView>(R.id.tv_title)
    val infoTextView = alertLayout.findViewById<TextView>(R.id.tv_info)
    val okTextView = alertLayout.findViewById<TextView>(R.id.tv_ok)

    if (title.isBlank()) {
        titleTextView.visibility = View.GONE
    } else {
        titleTextView.typeface = Typeface.DEFAULT_BOLD
        titleTextView.visibility = View.VISIBLE
        titleTextView.text = title
    }


    infoTextView.text = message
    okTextView.text = positiveButtonName

    val alertDialog = dialogBuilder.create()
    okTextView.setOnClickListener {
        alertDialogListener.onOkClick(positiveButtonName, positiveButtonName)
        alertDialog.dismiss()
    }
    try {
        alertDialog.show()
    } catch (e: Exception) {
    }

}

fun Context.showInfoDialog(
    title: String,
    message: String,
    positiveButtonName: String,
    negativeButtonName: String,
    alertDialogListener: AlertDialogListener,
    isCancelable: Boolean? = false
) {
    val dialogBuilder = AlertDialog.Builder(this)
    dialogBuilder.setCancelable(isCancelable ?: false)

    val inflater = LayoutInflater.from(this)
    val alertLayout = inflater.inflate(R.layout.core_info_dialog_view, null)
    dialogBuilder.setView(alertLayout)

    val titleTextView = alertLayout.findViewById<TextView>(R.id.tv_title)
    val infoTextView = alertLayout.findViewById<TextView>(R.id.tv_info)
    val okTextView = alertLayout.findViewById<TextView>(R.id.tv_ok)
    val cancelTextView = alertLayout.findViewById<TextView>(R.id.tv_cancel)

    if (title.isBlank()) {
        titleTextView.visibility = View.GONE
    } else {
        titleTextView.typeface = Typeface.DEFAULT_BOLD
        titleTextView.visibility = View.VISIBLE
        titleTextView.text = title
    }


    infoTextView.text = message?.stringToHtml()
    okTextView.text = positiveButtonName?.stringToHtml()
    cancelTextView.text = positiveButtonName?.stringToHtml()

    val alertDialog = dialogBuilder.create()
    okTextView.setOnClickListener {
        alertDialogListener.onOkClick(positiveButtonName, positiveButtonName)
        alertDialog.dismiss()
    }

    cancelTextView.setOnClickListener {
        alertDialog.dismiss()
    }
    alertDialog.show()
}


fun Context.showConsentDialog(
    title: String? = "",
    message: String?,
    positiveButtonName: String? = "Ok",
    textColor: String? = "#000000",
    clickListener: AlertDialogListener?
) {
    val builder = AlertDialog.Builder(this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT)
    builder.setMessage(message)
        .setCancelable(false)
        .setPositiveButton("<font color=$textColor>$positiveButtonName</font>".stringToHtmlSpannable()) { dialog, _ ->
            dialog.dismiss()
            clickListener?.onOkClick("ok", "1")

        }
    val alert = builder.create()
    alert.setTitle(title)
    alert.show()
}

fun Context.showInfoInvalidUrlDialog(
    message: String,
    positiveButtonName: String,
    alertDialogListener: AlertDialogListener,
    isCancelable: Boolean? = false
) {
    val dialogBuilder = AlertDialog.Builder(this)
    dialogBuilder.setCancelable(isCancelable ?: false)

    val inflater = LayoutInflater.from(this)
    val alertLayout = inflater.inflate(R.layout.info_wrong_url_dialog, null)
    dialogBuilder.setView(alertLayout)

    val infoTextView = alertLayout.findViewById<TextView>(R.id.tv_info)
    val okTextView = alertLayout.findViewById<TextView>(R.id.tv_ok)

    infoTextView.text = message
    infoTextView.typeface = Typeface.DEFAULT_BOLD


    okTextView.text = positiveButtonName
    okTextView.setTextColor("#007aff".getColor())

    val alertDialog = dialogBuilder.create()
    alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    okTextView.setOnClickListener {
        alertDialogListener.onOkClick(positiveButtonName, positiveButtonName)
        alertDialog.dismiss()
    }
    try {
        alertDialog.show()
    } catch (e: Exception) {
    }

}


