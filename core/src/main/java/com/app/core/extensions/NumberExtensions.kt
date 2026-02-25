package com.app.core.extensions

import android.os.Build
import androidx.annotation.RequiresApi
import com.app.core.utils.CoreMetaData
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.NumberFormat
import java.util.*

fun Float?.toStringOnePlace(): String {
    return this?.let {
        String.format(Locale.ENGLISH, "%.2f", it)
    } ?: kotlin.run { "0.0" }
}

fun Float?.toStringTwoPlace(): String {
    return this?.let {
        String.format(Locale.ENGLISH, "%.2f", it)
    } ?: kotlin.run { "0.00" }
}

fun Float?.toStringTwoPlaceAlways(): String {
    return this?.let {
        String.format(Locale.ENGLISH, "%.2f", it)
    } ?: kotlin.run { "0.00" }
}

fun Float?.toStringThreePlaceAlways(): String {
    return this?.let {
        String.format(Locale.ENGLISH, "%.3f", it)
    } ?: kotlin.run { "0.000" }
}

fun Float?.toStringOnePlaceAlways(): String {
    return this?.let {
        String.format(Locale.ENGLISH, "%.1f", it)
    } ?: kotlin.run { "0.0" }
}

fun Float?.formatToLocale(minimumDecimalPlaces: Int = 0): String {
    val locale = getSystemLocale()
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        return try {
            val format: android.icu.text.DecimalFormat =
                android.icu.text.NumberFormat.getInstance(locale) as android.icu.text.DecimalFormat
            val symbols: android.icu.text.DecimalFormatSymbols = format.decimalFormatSymbols
            format.decimalFormatSymbols = symbols
            format.minimumFractionDigits = minimumDecimalPlaces
            format.maximumFractionDigits = 2
            val formattedValue = format.format(this)
            formattedValue
        } catch (e: Exception) {
            e.printStackTrace()
            return this.toString()
        }
    } else {
        return try {
            val format: DecimalFormat = NumberFormat.getInstance(locale) as DecimalFormat
            val symbols: DecimalFormatSymbols = format.decimalFormatSymbols
            format.decimalFormatSymbols = symbols
            format.minimumFractionDigits = minimumDecimalPlaces
            format.maximumFractionDigits = 2
            val formattedValue = format.format(this)
            formattedValue
        } catch (e: Exception) {
            e.printStackTrace()
            return this.toString()
        }
    }
}

fun Double?.formatToLocale(minimumDecimalPlaces: Int = 0, local: Locale?): String {
    val locale = local ?: getSystemLocale()
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        return try {
            val format: android.icu.text.DecimalFormat =
                android.icu.text.NumberFormat.getInstance(locale) as android.icu.text.DecimalFormat
            val symbols: android.icu.text.DecimalFormatSymbols = format.decimalFormatSymbols
            format.decimalFormatSymbols = symbols
            format.minimumFractionDigits = minimumDecimalPlaces
            format.maximumFractionDigits = 2
            val formattedValue = format.format(this)
            formattedValue
        } catch (e: Exception) {
            e.printStackTrace()
            return this.toString()
        }
    } else {
        return try {
            val format: DecimalFormat = NumberFormat.getInstance(locale) as DecimalFormat
            val symbols: DecimalFormatSymbols = format.decimalFormatSymbols
            format.decimalFormatSymbols = symbols
            format.minimumFractionDigits = minimumDecimalPlaces
            format.maximumFractionDigits = 2
            val formattedValue = format.format(this)
            formattedValue
        } catch (e: Exception) {
            e.printStackTrace()
            return this.toString()
        }
    }
}

fun Int?.formatToLocale(): String {
    val locale = getSystemLocale()
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        return try {
            val format: android.icu.text.DecimalFormat =
                android.icu.text.NumberFormat.getInstance(locale) as android.icu.text.DecimalFormat
            val symbols: android.icu.text.DecimalFormatSymbols = format.decimalFormatSymbols
            format.decimalFormatSymbols = symbols
            format.minimumFractionDigits = 0
            format.maximumFractionDigits = 0
            val formattedValue = format.format(this)
            formattedValue
        } catch (e: Exception) {
            e.printStackTrace()
            return this.toString()
        }
    } else {
        return try {
            val format: DecimalFormat = NumberFormat.getInstance(locale) as DecimalFormat
            val symbols: DecimalFormatSymbols = format.decimalFormatSymbols
            format.decimalFormatSymbols = symbols
            format.minimumFractionDigits = 0
            format.maximumFractionDigits = 0
            val formattedValue = format.format(this)
            formattedValue
        } catch (e: Exception) {
            e.printStackTrace()
            return this.toString()
        }
    }
}

fun Float.currencyForLocale(
    currencyCode: String,
    currencySymbol: String? = null,
    minimumDecimalPlaces: Int = 2,
    maximumDecimalPlaces: Int = 2,
): String {
    val locale = getSystemLocale()
    val appyCurrencySymbol = if (currencySymbol.isNotNullOrEmpty()) currencySymbol else currencyCode.currencySymbolFromName()

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        return try {
            val format: android.icu.text.DecimalFormat =
                android.icu.text.NumberFormat.getCurrencyInstance(locale) as android.icu.text.DecimalFormat
            val symbols: android.icu.text.DecimalFormatSymbols = format.decimalFormatSymbols
            getCurrencyICU(currencyCode)?.let {
                symbols.currency = it
                symbols.currencySymbol = ""
                format.currency = it
            } ?: kotlin.run {
                symbols.currencySymbol = ""
            }
            format.decimalFormatSymbols = symbols
            format.minimumFractionDigits = minimumDecimalPlaces
            format.maximumFractionDigits = maximumDecimalPlaces
            val formattedValue = format.format(this)
            alignCurrencyData(appyCurrencySymbol, formattedValue)
        } catch (e: Exception) {
            e.printStackTrace()
            alignCurrencyData(appyCurrencySymbol, this.toStringTwoPlace())
        }
    } else {
        return try {
            val format: DecimalFormat = NumberFormat.getCurrencyInstance(locale) as DecimalFormat
            val symbols: DecimalFormatSymbols = format.decimalFormatSymbols
            getCurrency(currencyCode)?.let {
                symbols.currency = it
                symbols.currencySymbol = ""
                format.currency = it
            } ?: kotlin.run {
                symbols.currencySymbol = ""
            }
            format.decimalFormatSymbols = symbols
            format.minimumFractionDigits = minimumDecimalPlaces
            format.maximumFractionDigits = maximumDecimalPlaces
            val formattedValue = format.format(this)
            alignCurrencyData(appyCurrencySymbol, formattedValue)
        } catch (e: Exception) {
            e.printStackTrace()
            alignCurrencyData(appyCurrencySymbol, this.toStringTwoPlace())
        }
    }
}

private fun alignCurrencyData(symbol: String?, format: String): String {
    return if (CoreMetaData.isCurrencyLeftAligned) String.format("%s %s", symbol ?: "", format, Locale.US) else String.format(
        "%s %s",
        format,
        symbol ?: "",
        Locale.US
    )
}

fun getCurrency(currencyCode: String): Currency? {
    return try {
        Currency.getInstance(currencyCode)
    } catch (e: java.lang.Exception) {
        e.printStackTrace()
        null
    }
}

@RequiresApi(Build.VERSION_CODES.N)
fun getCurrencyICU(currencyCode: String): android.icu.util.Currency? {
    return try {
        android.icu.util.Currency.getInstance(currencyCode)
    } catch (e: java.lang.Exception) {
        e.printStackTrace()
        null
    }
}

fun Int.currencyForLocale(currencyCode: String, currencySymbol: String? = null): String {
    return this.toFloat().currencyForLocale(currencyCode, currencySymbol, 0)
}

fun Long.convertUnixTimeToDate(): Date {
    return Date(this * 1000)
}

fun Float.milesToKm() = this.times(1.60934).toFloat()

fun Long.kmToMiles() = this.times(0.621371).toLong()
fun Long.toKM() = this.div(1.60934f).toLong()
fun Long.meterToMiles() = this.times(0.000621371f)
fun Double.between(minValueInclusive: Double, maxValueInclusive: Double) = this in minValueInclusive..maxValueInclusive

fun Double?.parseDoubleOrIntValue(upToDigit: Int? = 2): String {
    return this?.let {
        if (upToDigit == 3) DecimalFormat("#.###").format(it)
        else if (upToDigit == 4) DecimalFormat("#.####").format(it)
        else DecimalFormat("#.##").format(it)
    } ?: kotlin.run { "0.00" }
}


fun Double?.parseDoubleOrIntValue(): String {
    return this?.let {
        String.format(Locale.ENGLISH, "%.2d", it)
    } ?: kotlin.run { "0.00" }
}

fun Double?.showNumberFormatedValues(local: Locale): String {
    try {
        val df = NumberFormat.getInstance(Locale.US) as DecimalFormat
        df.isDecimalSeparatorAlwaysShown = true
        df.maximumFractionDigits = 2
        val dfnd = if (this == 0.0) {
            DecimalFormat("0.00")
        } else {
            DecimalFormat("#,###,###,###.00")
        }
        val n = df.parse(this.toString())
        return dfnd.format(n)
    } catch (e: java.lang.Exception) {
        e.printStackTrace()
        return "0.0"
    }
}

fun Double.dollarValue(locale: Locale? = Locale.US): String {
    val currencyFormatter = NumberFormat.getInstance(locale) as DecimalFormat
    currencyFormatter.maximumFractionDigits = 0
    return currencyFormatter.format(this)
}













