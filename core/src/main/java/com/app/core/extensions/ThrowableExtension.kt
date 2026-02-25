package com.app.core.extensions

import java.io.PrintWriter
import java.io.StringWriter

fun Throwable.logStackTrace(): String {
    val sw = StringWriter()
    this.printStackTrace(PrintWriter(sw))
    return sw.toString()
}