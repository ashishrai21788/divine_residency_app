package com.app.core.extensions


import android.webkit.MimeTypeMap
import java.io.File


fun File.getMimeType(): String {
    return MimeTypeMap.getFileExtensionFromUrl(toString())
        ?.run { MimeTypeMap.getSingleton().getMimeTypeFromExtension(toLowerCase()) }
        ?: ""
}