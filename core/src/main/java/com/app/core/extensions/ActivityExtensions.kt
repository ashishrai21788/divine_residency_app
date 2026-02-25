package com.app.core.extensions

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.view.View
import android.view.inputmethod.InputMethodManager


fun Activity.setStatusBarColor(color: Int?) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && color != null) {
        val statusColor = if (color == 0) Color.BLACK else color
        try {
            window.statusBarColor = statusColor
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }
}

fun Activity.hideKeyBoard(view: View?) {
    if (view != null) {
        val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}

fun Activity.appInstalledOrNot(pkg: String): Boolean {
    return try {
        packageManager?.getApplicationInfo(pkg, 0)
        true
    } catch (e: PackageManager.NameNotFoundException) {
        false
    }
}

fun Activity.sendOrderDetailOnWhatsApp(number: String, message: String, split: Boolean = false) {
    var messageToSend = message
    if (split) {
        val builder = StringBuilder()
        message.split("\\n").forEachIndexed { index, chunk ->
            builder.append(chunk).append("\n")
        }
        messageToSend = builder.toString()
    }
    if (this.appInstalledOrNot("com.whatsapp") || this.appInstalledOrNot("com.whatsapp.w4b")) {
        this.openBrowser("http://api.whatsapp.com/send?phone=$number&text=${messageToSend.encodeURIComponent()}")
    } else {
        this.openBrowser("market://details?id=com.whatsapp")
    }
}


