package com.app.core.utils

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.text.SimpleDateFormat
import java.util.*

@Parcelize
data class DisplayableDateDMY(
    val timeISO: String?,
    val dateFormatFromServer: String = "yyyy-MM-dd'T'HH:mm:ss",
    var date: Date? = null

) : Parcelable {
    init {
        timeISO?.let {
            val isoDateFormat = SimpleDateFormat(dateFormatFromServer, Locale.ENGLISH)
            kotlin.runCatching {
                date = isoDateFormat.parse(it)
            }
        }
    }
}