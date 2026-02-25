package com.app.core.extensions

import android.os.Build
import android.util.Log
import com.app.core.utils.CoreMetaData
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

fun Date.convertDateWithFormat(format: String, locale: Locale = getSystemLocale()): String {
    return try {
        SimpleDateFormat(format, locale).format(this)
    } catch (e: Exception) {
        e.printStackTrace()
        SimpleDateFormat("dd-MMM-yyyy", locale).format(this)
    }
}

fun Date.simpleDateFormatUtil(format: String, local: Locale? = Locale.US): String {
    var dateString = ""
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val dateFormat: android.icu.text.SimpleDateFormat
        try {
            if (local != null) {
                dateFormat = android.icu.text.SimpleDateFormat(format, local)
            } else {
                dateFormat = android.icu.text.SimpleDateFormat(format)
            }
            dateString = dateFormat.format(this)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    } else {
        val dateFormat: java.text.SimpleDateFormat

        try {
            if (local != null) {
                dateFormat = java.text.SimpleDateFormat(format, local)
            } else {
                dateFormat = java.text.SimpleDateFormat(format)
            }
            dateString = dateFormat.format(this)

        } catch (e: Exception) {
            e.printStackTrace()
        }

    }
    return dateString
}

fun String.appyDateStringFormatUtil(
    currentFormat: String,
    reqFormat: String,
    local: Locale? = Locale.US
): String? {
    var dateString = ""
    try {
        val inputFormat = SimpleDateFormat(currentFormat, local)
        val outputFormat = SimpleDateFormat(reqFormat, local)
        val date = inputFormat.parse(this)
        dateString = outputFormat.format(date)
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return dateString
}
fun String.getTimeAgo(dataFormat: String="yyyy-MM-dd'T'HH:mm:ss",
               local: Locale? = Locale.US): String? {
    var convTime: String? = null
    val suffix = "ago"
    val SECOND_MILLIS = 1000
    val MINUTE_MILLIS = 60 * SECOND_MILLIS
    val HOUR_MILLIS = 60 * MINUTE_MILLIS
    val DAY_MILLIS = 24 * HOUR_MILLIS

    try {
        val inputFormat = SimpleDateFormat(dataFormat, local)
        val pastDateTime = inputFormat.parse(this)
        var time = pastDateTime.time
        if (time < 1000000000000L) {
            time *= 1000
        }

        val now = Calendar.getInstance().timeInMillis
        if (time > now || time <= 0) {
            return "in the future"
        }
        val diff = now - time
        val second = TimeUnit.MILLISECONDS.toSeconds(diff)
        val minute = TimeUnit.MILLISECONDS.toMinutes(diff)
        val hour = TimeUnit.MILLISECONDS.toHours(diff)
        //val day = TimeUnit.MILLISECONDS.toDays(diff)
        val day = diff / DAY_MILLIS
        if(diff < MINUTE_MILLIS){
            convTime =  "moments ago"
        }else if(diff < 2 * MINUTE_MILLIS){
            convTime =  "a minute ago"
        }else if(diff < 60 * MINUTE_MILLIS){
            convTime =  "${diff / MINUTE_MILLIS} minutes ago"
        }else if(diff < 2 * HOUR_MILLIS){
            convTime =  "an hour ago"
        }else if(diff < 24 * HOUR_MILLIS){
            convTime =  "${diff / HOUR_MILLIS} hours ago"
        }else if(diff < 48 * HOUR_MILLIS){
            convTime =  "yesterday"
        } else if (day >= 7) {
            if (day > 360) {
                if((day / 360) == 1L){
                    convTime =  "a month $suffix"
                }else{
                    convTime = "${day / 360} years $suffix"
                }
            } else if (day > 30) {
                if((day / 30) == 1L){
                    convTime =  "a month $suffix"
                }else{
                    convTime = "${day / 30} months $suffix"
                }
            } else {
                if((day / 7) == 1L){
                    convTime =  "a week $suffix"
                }else{
                    convTime =  "${day / 7} weeks $suffix"
                }
            }
        } else if(day < 7){
            if(day == 1L){
                convTime =  "a day $suffix"
            }else{
                convTime =  "$day days $suffix"
            }
        }
    } catch (e: ParseException) {
        e.printStackTrace()
        e.message?.let { Log.e("ConvTimeE", it) }
    }
    return convTime
}

fun String.covertTimeToText(dataFormat: String="yyyy-MM-dd'T'HH:mm:ss",
                            local: Locale? = Locale.US): String? {
    var convTime: String? = null
    val prefix = ""
    val suffix = "ago"
    try {
        val inputFormat = SimpleDateFormat(dataFormat, local)
        val pastDateTime = inputFormat.parse(this)
        val nowTime = Date()
        val dateDiff = nowTime.time - pastDateTime.time
        val second = TimeUnit.MILLISECONDS.toSeconds(dateDiff)
        val minute = TimeUnit.MILLISECONDS.toMinutes(dateDiff)
        val hour = TimeUnit.MILLISECONDS.toHours(dateDiff)
        val day = TimeUnit.MILLISECONDS.toDays(dateDiff)

        if (second < 60) {
            if(second == 1L){
                convTime =  "a seconds $suffix"
            }else {
                convTime = "$second seconds $suffix"
            }
        } else if (minute < 60) {
            if(minute == 1L){
                convTime =  "a minutes $suffix"
            }else {
                convTime = "$minute minutes $suffix"
            }
        } else if (hour < 24) {
            if(hour == 1L){
                convTime =  "an hours $suffix"
            }else {
                convTime = "$hour hours $suffix"
            }
        } else if (day >= 28) {
            if (day > 360) {
                if((day / 360) == 1L){
                    convTime =  "a month $suffix"
                }else{
                    convTime = "${day / 360} years $suffix"
                }
            } else{
                if((day / 28) == 1L){
                    convTime =  "a month $suffix"
                }else{
                    convTime = "${day / 28} months $suffix"
                }
            } /*else {
                if((day / 7) == 1L){
                    convTime =  "a week $suffix"
                }else{
                    convTime =  "${day / 7} weeks $suffix"
                }
            }*/
        } else if (day < 28) {
            if(day == 1L){
                convTime =  "a day $suffix"
            }else{
                convTime =  "$day days $suffix"
            }
        }
    } catch (e: ParseException) {
        e.printStackTrace()
        e.message?.let { Log.e("ConvTimeE", it) }
    }

    return convTime
}

fun String.appyDateStringFormatUtilWithCorrespondingLocale(
    currentFormat: String,
    reqFormat: String,
    currentFormatLocale: Locale? = Locale.US,
    reqFormatLocale: Locale? = Locale.US
): String? {
    var dateString = ""
    try {
        val inputFormat = SimpleDateFormat(currentFormat, currentFormatLocale)
        val outputFormat = SimpleDateFormat(reqFormat, reqFormatLocale)
        val date = inputFormat.parse(this)
        dateString = outputFormat.format(date)
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return dateString
}

fun String.appyDateFormatUtil(
    currentFormat: String,
    local: Locale? = Locale.US
): Date? {
    var dateString: Date? = null
    try {
        val inputFormat = SimpleDateFormat(currentFormat, local)
        dateString = inputFormat.parse(this)
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return dateString
}

fun Date.appyDateFormatUtil(
    reqFormat: String,
    local: Locale? = Locale.US
): String {
    var dateString = ""
    try {
        val inputFormat = SimpleDateFormat(reqFormat, local)
        dateString = inputFormat.format(this)
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return dateString
}


fun String.simpleDateFormatUtil(format: String, local: Locale? = Locale.US): Date? {
    var dateString: Date? = null
    if (Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
        //Calendar date =  Calendar.getInstance();
        //date.set
        val dateFormat: android.icu.text.SimpleDateFormat

        try {

            if (local != null) {
                dateFormat = android.icu.text.SimpleDateFormat(format, local)
            } else {
                dateFormat = android.icu.text.SimpleDateFormat(format)
            }
            dateString = dateFormat.parse(this)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    } else {
        val dateFormat: java.text.SimpleDateFormat

        try {
            if (local != null) {
                dateFormat = java.text.SimpleDateFormat(format, local)
            } else {
                dateFormat = java.text.SimpleDateFormat(format)
            }
            dateString = dateFormat.parse(this)

        } catch (e: Exception) {
            e.printStackTrace()
        }

    }
    return dateString
}

fun localGetDefault(): Locale {
    val localeGetVal: Locale
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        localeGetVal = Locale.getDefault(Locale.Category.DISPLAY)
    } else {
        localeGetVal = Locale.getDefault()
    }

    return localeGetVal
}

fun getCurrentDate(format: String = "dd/MM/yyyy"): String {
    return Date().appyDateFormatUtil(format)
}

fun getCurrentMonth(format: String = "MMMM, yyyy", local: Locale? = Locale.US): String {
    return Date().appyDateFormatUtil(format)
}

fun getCurrentYear(format: String = "yyyy", local: Locale? = Locale.US): String {
    return Date().appyDateFormatUtil(format)
}

fun getCurrentTime(format: String = "HH:mm:ss"): String {
    val sdf = SimpleDateFormat(format)
    return sdf.format(Date())
}

fun getCurrentTimeWithAMPM(format: String = "hh:mm aaa"): String {
    val sdf = SimpleDateFormat(format)
    return sdf.format(Date())
}

fun String.getTimeFormat(oldformat: String = "HH:mm:ss", newformat: String = "HH:mm:ss"): String {
    var time = ""
    val oldformat = SimpleDateFormat(oldformat)
    val newformat = SimpleDateFormat(newformat)
    try {
        val date = oldformat.parse(this)
        time = newformat.format(date)
    } catch (e: ParseException) {
    }
    return time
}

fun getCurrentDateTime(format: String = "dd/MM/yyyy HH:mm:ss", locale: Locale): String {
    return Date().simpleDateFormatUtil(format, locale)
}

fun Date.convertToUnixTimeStamp(): Long {
    return this.time.div(1000)
}



fun dateTimeFormate(format: String?, local: Locale?, date: String): String {
    var dateString = ""
    if (Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
        val dateFormat: android.icu.text.SimpleDateFormat

        try {
            if (local != null) {
                dateFormat = android.icu.text.SimpleDateFormat(format, local)
            } else {
                dateFormat = android.icu.text.SimpleDateFormat(format)
            }
            dateString = dateFormat.format(dateFormat.parse(date))
        } catch (e: Exception) {
            e.printStackTrace()
        }

    } else {
        val dateFormat: java.text.SimpleDateFormat

        try {
            if (local != null) {
                dateFormat = java.text.SimpleDateFormat(format, local)
            } else {
                dateFormat = java.text.SimpleDateFormat(format)
            }
            dateString = dateFormat.format(dateFormat.parse(date))

        } catch (e: Exception) {
            e.printStackTrace()
        }

    }
    return dateString
}


fun Date.convertToTimeDisplay(twelveFormat: Boolean = true): String {
    return if (twelveFormat) {
        SimpleDateFormat("hh:mm a", CoreMetaData.appLocale).format(this)
    } else {
        SimpleDateFormat("HH:mm", CoreMetaData.appLocale).format(this)
    }
}

fun Long.convertDateWithTimeStamp(format: String? = "dd/MM/yyyy HH:mm:ss", locale: Locale): String? {
    val formatter = SimpleDateFormat(format)
    return if (this > 0) {
        formatter.format(Date(this * 1000))
    } else {
        ""
    }
}

fun String.appyDateStringFormatUtilWithFormats (
    reqFormat: String,
    local: Locale? = Locale.US
): String? {
    var dateString = ""
    try {
        val date = this.getDate() ?: return null

        val outputFormat = SimpleDateFormat(reqFormat, local)
        dateString = outputFormat.format(date)
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return dateString
}

fun String.getDate(): Date?{
    //current date format available in admin
    //dd-MMM-yyyy
    //Dd-MM-yyyy
    //Dd/mmm/yyyy
    //Dd/mm/yyyy
    //Mmm-dd-yyyy
    //Mm-dd-yyyy
    //Mmm/dd/yyyy
    //MM/dd/yyyy

    return this.simpleDateFormatUtil("MMM-dd-yyyy") ?:
    this.simpleDateFormatUtil("MM-dd-yyyy") ?:
    this.simpleDateFormatUtil("MMM/dd/yyyy") ?:
    this.simpleDateFormatUtil("MM/dd/yyyy") ?:
    this.simpleDateFormatUtil("dd-MMM-yyyy") ?:
    this.simpleDateFormatUtil("dd-MM-yyyy") ?:
    this.simpleDateFormatUtil("dd/MMM/yyyy")?:
    this.simpleDateFormatUtil("MMM dd yyyy")?:
    this.simpleDateFormatUtil("d MMMM yyyy")?:
    this.simpleDateFormatUtil("dd/MM/yyyy")
}


fun String.newsDateStringFormatUtil(
    currentFormat: String,
    reqFormat: String,
    local: Locale? = Locale.US
): String? {
    var dateString = ""
    try {
        val inputFormat = SimpleDateFormat(currentFormat, local)
        val outputFormat = SimpleDateFormat(reqFormat, local)
        val date = inputFormat.parse(this)
        dateString = outputFormat.format(date)
    } catch (e: Exception) {
        e.printStackTrace()
        return this.toUpperCase()
    }
    return dateString.toUpperCase()
}