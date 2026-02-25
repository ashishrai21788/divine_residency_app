package com.app.core.extensions

import android.webkit.WebView
import com.app.core.extensions.htmlfy
import com.app.core.extensions.isValidUrl

fun WebView.loadHtmlOrUrlData(htmlOrUrl: String?, contentStyle: List<String>? = null) {
    if (htmlOrUrl.isValidUrl()) {
        this.loadUrl(htmlOrUrl ?: "")
    } else {
        this.loadDataWithBaseURL(
            "file:///android_asset/",
            htmlOrUrl?.htmlfy(contentStyle) ?: "",
            "text/html",
            "UTF-8",
            null
        )
    }
}