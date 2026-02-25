package com.app.core.extensions

import java.text.DecimalFormat

fun Double?.parseDoubleOrInt(upToDigit:Int?= 2): String {
    return this?.let {
        if(upToDigit == 3) DecimalFormat("#.###").format(it)
        else if(upToDigit == 4) DecimalFormat("#.####").format(it)
        else DecimalFormat("#.##").format(it)
    } ?: kotlin.run { "0.00" }
}