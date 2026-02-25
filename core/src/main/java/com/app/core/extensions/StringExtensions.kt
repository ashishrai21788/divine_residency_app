package com.app.core.extensions

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.text.Editable
import android.text.Html
import android.text.Spanned
import android.webkit.URLUtil
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import com.app.core.utils.CoreMetaData
import com.app.core.utils.provideGsonWithCoreJsonString
import org.json.JSONObject
import java.io.UnsupportedEncodingException
import java.math.BigDecimal
import java.math.BigInteger
import java.net.URLDecoder
import java.net.URLEncoder
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern


fun String?.isNotNullOrEmpty(): Boolean = this != null && this.trim().isNotEmpty()

fun String?.getColor(): Int {

    return try {
        val color = this?.let {
            if (this.isEmpty()) -1 else this.getOctColor()
        } ?: kotlin.run {
            -1
        }
        color
    } catch (e: Exception) {
        e.printStackTrace()
        -1
    }
}

private fun String.getOctColor(): Int {
    if (this.contains("rgba")) {
        var tempstr = this.split("rgba\\(".toRegex()).dropLastWhile { it.isEmpty() }
            .toTypedArray()//.split(",");
        tempstr = tempstr[1].split("\\)".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        tempstr = tempstr[0].split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()


        if (!this.equals("rgba(255,255,255,0)", ignoreCase = true)) {
            return Color.parseColor(
                String.format(
                    "#%02x%02x%02x%02x",
                    Math.round(java.lang.Float.parseFloat(tempstr[3].trim()) * 255),
                    Integer.parseInt(tempstr[0].trim()),
                    Integer.parseInt(tempstr[1].trim()),
                    Integer.parseInt(tempstr[2].trim())
                )
            )
        }
        return Color.parseColor("#00000000")
    } else {
        return this.getObjColor()
    }

}


private fun String.getObjColor(): Int {

    try {
        if (this.contains("rgb")) {
            var tempstr =
                this.split("rgb\\(".toRegex()).dropLastWhile { it.isEmpty() }
                    .toTypedArray()//.split(",");
            tempstr =
                tempstr[1].split("\\)".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            tempstr = tempstr[0].split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            return Color.rgb(
                Integer.parseInt(tempstr[0].trim { it <= ' ' }),
                Integer.parseInt(tempstr[1].trim { it <= ' ' }),
                Integer.parseInt(tempstr[2].trim { it <= ' ' })
            )

        } else if (this.contains("#")) {
            if (this.length < 5) {
                val color = this.replace("#", "")
                var tempColor = ""
                for (i in 0..2)
                    tempColor = color[i].toString() + color[i].toString() + tempColor
                //Log.i("AppCompactView", "received color #$tempColor")
                return Color.parseColor("#$tempColor")

            } else
                return Color.parseColor(this)
        }
    } catch (e: Exception) {
        return Color.parseColor("#000000")
    }

    return Color.parseColor("#000000")
}

fun String?.getToolBarTextSize(): Float {
    if (this.equals("largeHeaderBar", ignoreCase = true))
        return 26f
    else if (this.equals("mediumHeaderBar", ignoreCase = true))
        return 20f
    else if (this.equals("smallHeaderBar", ignoreCase = true))
        return 19f
    else if (this.equals("xlargeHeaderBar", ignoreCase = true))
        return 36f
    else
        return 20f
}

fun String?.getFloatValue(): Float {
    return try {
        this?.toFloatOrNull() ?: 0f
    } catch (e: java.lang.Exception) {
        // e.printStackTrace()
        0f
    }
}

fun String?.getDoubleValue(): Double {
    return try {
        this?.toDoubleOrNull() ?: 0.toDouble()
    } catch (e: java.lang.Exception) {
        // e.printStackTrace()
        0.toDouble()
    }
}


fun String?.getIntValue(defaultValue: Int = 0): Int {
    return try {
        this?.trim()?.toIntOrNull() ?: defaultValue
    } catch (e: java.lang.Exception) {
        // e.printStackTrace()
        defaultValue
    }
}

fun String?.getLongValue(defaultValue: Long = 0): Long {
    return try {
        this?.toLongOrNull() ?: defaultValue
    } catch (e: java.lang.Exception) {
        // e.printStackTrace()
        defaultValue
    }
}

fun String?.getBigDecimalValue(defaultValue: BigDecimal = BigDecimal(0)): BigDecimal {
    return try {
        this?.toBigDecimal() ?: defaultValue
    } catch (e: java.lang.Exception) {
        // e.printStackTrace()
        defaultValue
    }
}

fun BigDecimal?.getIntWithBigDecimal(defaultValue: Int = 0): Int {
    return try {
        this?.toInt() ?: defaultValue
    } catch (e: java.lang.Exception) {
        // e.printStackTrace()
        defaultValue
    }
}

fun String?.getBooleanValue(): Boolean {
    return try {
        return this?.trim() == "true" || this?.trim() == "True" || this?.trim() == "TRUE" || this?.trim() == "1" || this?.trim() == "Yes" || this?.trim() == "YES" || this?.trim() == "yes"
    } catch (e: java.lang.Exception) {
        // e.printStackTrace()
        false
    }
}


fun String?.getPlayStoreUrl(): String {
    return "https://play.google.com/store/apps/details?id=$this"
}

fun String?.validateEmail(): Boolean = this?.let {
    Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE)
        .matcher(this).find()
} ?: kotlin.run { false }


fun String?.isPhoneInput(): Boolean {
    this?.let {
        val doubleValue: Double = this.trim().toDoubleOrNull() ?: 0.toDouble()
        return (doubleValue > 0.toDouble())
    } ?: kotlin.run {
        return false
    }
}


fun String?.isMobileInput(): Boolean {
    this?.let {
        val doubleValue: Double = this.trim().toDoubleOrNull() ?: 0.toDouble()
        return (doubleValue > 0.toDouble()) && this.trim().length >= 10
    } ?: kotlin.run {
        return false
    }
}


fun String?.stableId() = this?.hashCode()?.toLong() ?: 0L


fun String.toJsonObject(): JsonObject? {
    return try {
        Gson().fromJson(this, JsonObject::class.java)
    } catch (e: Throwable) {
        e.printStackTrace()
        null
    }
}

fun String.toJSONObject(): JSONObject? {
    return try {
        JSONObject(this)
    } catch (e: Throwable) {
        e.printStackTrace()
        null
    }
}

fun String.toJsonArray(): JsonArray? {
    return try {
        Gson().fromJson(this, JsonArray::class.java)
    } catch (e: Throwable) {
        e.printStackTrace()
        null
    }
}

fun String.actionViewForUrl(context: Context?): Boolean {
    try {

        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(if (this.startsWith("http")) this else "https://$this"))
        context?.startActivity(browserIntent)
        return true
    } catch (e: java.lang.Exception) {
        e.printStackTrace()
        return false
    }
}

fun String.actionDialNumber(context: Context?) {
    try {
        val browserIntent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$this"))
        context?.startActivity(browserIntent)
    } catch (e: java.lang.Exception) {
        e.printStackTrace()
    }
}

fun String.actionSMS(context: Context?) {
    try {
        val uri = Uri.parse("smsto:$this")
        val browserIntent = Intent(Intent.ACTION_SENDTO, uri)
        context?.startActivity(browserIntent)
    } catch (e: java.lang.Exception) {
        e.printStackTrace()
    }
}

fun String.actionEmail(context: Context?) {
    try {
        val uri = Uri.parse("mailto:$this")
        val browserIntent = Intent(Intent.ACTION_SENDTO, uri)
        context?.startActivity(browserIntent)
    } catch (e: java.lang.Exception) {
        e.printStackTrace()
    }
}

fun String.getSharableIntent(): Intent {
    val intent = Intent("android.intent.action.SEND")
    intent.type = "text/plain"
    intent.putExtra("android.intent.extra.TEXT", this)
    return intent
}


fun getDateInInputPattern(pattern: String): String {
    val calendar = Calendar.getInstance()
    val time = calendar.time
    val outputFmt = SimpleDateFormat(pattern, Locale.US)
    return outputFmt.format(time)
}


fun String.encodeURIComponent(): String {
    val result: String? = try {
        URLEncoder.encode(this, "UTF-8")
            .replace("\\+".toRegex(), "%20")
            .replace("\\%21".toRegex(), "!")
            .replace("\\%27".toRegex(), "'")
            .replace("\\%28".toRegex(), "(")
            .replace("\\%29".toRegex(), ")")
            .replace("\\%7E".toRegex(), "~")
    } catch (e: UnsupportedEncodingException) {
        this
    }
    return result ?: ""
}


fun String.getSharableAppPackageName(): String {

    return when (this) {
        "skype" -> {
            "com.skype.raider"
        }
        "linechat" -> {
            "jp.naver.line.android"
        }
        "whatsapp" -> {
            "com.whatsapp"
        }
        "wechat" -> {
            "com.tencent.mm"
        }
        "snapchat" -> {
            "com.snapchat.android"
        }
        "line" -> {
            "jp.naver.line.android"
        }
        else -> {
            ""
        }
    }

}

fun String.getSharableAppPlayStoreLink(): String {

    return when (this) {
        "skype" -> {
            "market://details?id=com.skype.raider"
        }
        "linechat" -> {
            "market://details?id=jp.naver.line.android"
        }
        "whatsapp" -> {
            "market://details?id=com.whatsapp"
        }
        "wechat" -> {
            "market://details?id=com.tencent.mm"
        }
        "snapchat" -> {
            "market://details?id=com.snapchat.android"
        }
        "line" -> {
            "market://details?id=com.snapchat.android"
        }
        else -> {
            ""
        }
    }

}

fun String.convertStringToDateDDMMMYYYY(): String {
    val date = SimpleDateFormat("dd-MM-yyyy").parse(this)
    val format = SimpleDateFormat("dd-MMM-yyyy")
    return format.format(date)
}

fun String.convertSimpleDateFormat(): String {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        android.icu.text.SimpleDateFormat(this, Locale.getDefault()).format(Date())
    } else {
        SimpleDateFormat(this, Locale.getDefault()).format(Date())
    }
}

fun String.toUrlUtf(): String {
    return try {
        URLEncoder.encode(this, "UTF-8")
    } catch (e: Throwable) {
        e.printStackTrace()
        ""
    }
}

fun String.decodeUrlUTF(): String {
    return try {
        URLDecoder.decode(this, "UTF-8")
    } catch (e: Throwable) {
        e.printStackTrace()
        ""
    }
}


fun String.getMD5(): String {
    return try {
        val md = MessageDigest.getInstance("MD5")
        val messageDigest = md.digest(this.toByteArray())
        val number = BigInteger(1, messageDigest)
        var hashtext = number.toString(16)
        // Now we need to zero pad it if you actually want the full 32 chars.
        while (hashtext.length < 32) {
            hashtext = "0$hashtext"
        }
        hashtext
    } catch (e: NoSuchAlgorithmException) {
        this
    }

}

fun String.getSHA1(): String {
    return try {
        val md = MessageDigest.getInstance("SHA-1")
        val messageDigest = md.digest(this.toByteArray())
        val number = BigInteger(1, messageDigest)
        var hashtext = number.toString(16)
        // Now we need to zero pad it if you actually want the full 32 chars.
        while (hashtext.length < 32) {
            hashtext = "0$hashtext"
        }
        hashtext
    } catch (e: NoSuchAlgorithmException) {
        this
    }
}

@SuppressLint("SimpleDateFormat")
fun String?.getDate(format: String, locale: Locale? = null): Date? {
    return try {
        val date = this ?: return null
        val dateFormat: SimpleDateFormat =
            locale?.let { SimpleDateFormat(format, locale) } ?: SimpleDateFormat(format)
        dateFormat.parse(date)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

@SuppressLint("SimpleDateFormat")
fun convertDateYYYYMMDDtoMMDDYYYY(time: String): String? {
    val inputPattern = "yyyy-MM-dd"
    val outputPattern = "MM-dd-yyyy"
    val inputFormat = SimpleDateFormat(inputPattern)
    val outputFormat = SimpleDateFormat(outputPattern)
    try {
        val date = inputFormat.parse(time) ?: return null
        return outputFormat.format(date)
    } catch (e: ParseException) {
        e.printStackTrace()
    }
    return null
}

fun String?.isYoutubeUrl(): Boolean {
    return this?.let { it.contains("youtube.com") || it.contains("youtu.be") } ?: return false
}

fun String?.getQueryParamFromUrl(key: String): String? {
    this?.let {
        try {
            val videoUri = Uri.parse(it)
            return videoUri.getQueryParameter(key)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }
    return null
}

fun String?.getVideoId(): String? {
    return if (this.isYoutubeUrl()) if (this.getQueryParamFromUrl("v").isNotNullOrEmpty()) this.getQueryParamFromUrl("v") else this?.split("/")
        ?.lastOrNull() else null
}

// in the case of social network, we get post with embedded javascript, so split the string with "href=" tag
// and then check if any of the splitted string contains youtube link
// with the split, i can easily get rid of the same mutliple link, one with js href tag with onclick, and other with post.
fun String?.getVideoMultipleIdForSocialNetworkUsecase(): List<String> {
    if (this.isNullOrEmpty()) return emptyList()

    val mutableList = mutableListOf<String>()
    val reg = "(?:youtube(?:-nocookie)?\\.com\\/(?:[^\\/\\n\\s]+\\/\\S+\\/|(?:v|e(?:mbed)?)\\/|\\S*?[?&]v=)|youtu\\.be\\/)([a-zA-Z0-9_-]{11})"
    val pattern = Pattern.compile(reg, Pattern.CASE_INSENSITIVE)
    val list = this.split("href=")

    list.forEach { splitString ->
        val matcher = pattern.matcher(splitString)
        if (matcher.find()) {
            mutableList.add(matcher.group(1) ?: "")
            matcher.replaceFirst("")
        }
    }
    return mutableList
}

fun String?.getDeeplinkHtml(): String? {
    return this + "<script type=\"text/javascript\">function opendeeplinkpage(toast) { Android.openDeepLinkPage(toast); } </script>"
        .replace("<a onclick=\"openEmail", "<a href=\"javascript:opendeeplinkemail")
}

fun String.unescape(): String? {
    val builder = StringBuilder()
    var startIndex = 0
    while (startIndex < this.length) {
        val delimiter = this[startIndex]
        startIndex++ // consume letter or backslash
        if (delimiter == '\\' && startIndex < this.length) { // consume first after backslash
            val ch = this[startIndex]
            startIndex++
            if (ch == '\\' || ch == '/' || ch == '"' || ch == '\'') {
                builder.append(ch)
            } else if (ch == 'n') builder.append('\n') else if (ch == 'r') builder.append('\r') else if (ch == 't') builder.append(
                '\t'
            ) else if (ch == 'b') builder.append(
                '\b'
            ) else if (ch == 'u') {
                val hex = StringBuilder()
                // expect 4 digits
                if (startIndex + 4 > this.length) {
                    throw RuntimeException("Not enough unicode digits! ")
                }
                for (x in this.substring(startIndex, startIndex + 4).toCharArray()) {
                    if (!Character.isLetterOrDigit(x)) {
                        throw RuntimeException("Bad character in unicode escape.")
                    }
                    hex.append(Character.toLowerCase(x))
                }
                startIndex += 4 // consume those four digits.
                val code = hex.toString().toInt(16)
                builder.append(code.toChar())
            } else {
                throw RuntimeException("Illegal escape sequence: \\$ch")
            }
        } else { // it's not a backslash, or it's the last character.
            builder.append(delimiter)
        }
    }
    var unescaped = builder.toString()
    if (unescaped.startsWith("\"")) {
        unescaped = unescaped.replaceRange(0, 1, "")
    }

    if (unescaped.endsWith("\"")) {
        unescaped = unescaped.replaceRange(unescaped.length - 1, unescaped.length, "")
    }

    return unescaped
}


fun <T> String?.convertIntoModel(classRef: Class<T>): T? {
    return try {
        convertIntoModel(classRef = classRef, gson = classRef.provideGsonWithCoreJsonString())
    } catch (ex: java.lang.Exception) {
        ex.printStackTrace()
        null
    }
}

fun <T> String?.convertIntoModels(type: TypeToken<T>): T? {
    return try {
        Gson().fromJson(this, type.type)
    } catch (ex: java.lang.Exception) {
        ex.printStackTrace()
        null
    }
}


fun <T> String?.convertIntoModel(classRef: Class<T>, gson: Gson): T? {
    return try {
        gson.fromJson(this, classRef)
    } catch (ex: java.lang.Exception) {
        ex.printStackTrace()
        null
    }
}


fun <T> String?.convertIntoModels(type: TypeToken<T>, gson: Gson): T? {
    return try {
        gson.fromJson(this, type.type)
    } catch (ex: java.lang.Exception) {
        ex.printStackTrace()
        null
    }

}

fun String.currencySymbolFromName(): String {
    val currencyHexCode = CoreMetaData.currencyMap.getOrElse(this) { return this }
    val defaultCurrencySymbol = currencyHexCode.stringToHtml()
    if (defaultCurrencySymbol.trim().isNotNullOrEmpty()) return defaultCurrencySymbol
    try {
        Currency.getInstance(this)?.let { currency ->
            if (currency.symbol.isNotEmpty()) {
                return currency.symbol
            }
        }

    } catch (e: java.lang.Exception) {
        e.printStackTrace()
        return defaultCurrencySymbol
    }
    return defaultCurrencySymbol
}

fun String?.isIconImageUrl(): Boolean {
    return this?.contains(".png") ?: false ||
            this?.contains("app_icon_") ?: false ||
            this?.contains(".jpg") ?: false ||
            this?.contains(".jpeg") ?: false ||
            this?.contains(".JPG") ?: false ||
            this?.contains(".JPEG") ?: false ||
            this?.contains(".PNG") ?: false
}

fun String.asRequired(symbol: String = "*", shouldApply: Boolean = true): String {
    return if (shouldApply) String.format("%s %s", this, symbol) else this
}




fun String.extractPageIdentifier(): String? {
    return this.split("--").getOrNull(0)
}


fun String.stringToHtml(flag: Int? = null): String {
    return stringToHtmlSpannable(flag = flag).toString()
}

fun String.stringToHtmlSpannable(flag: Int? = null): Spanned {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        Html.fromHtml(this, flag ?: Html.FROM_HTML_MODE_COMPACT) // for 24 api and more
    } else {
        Html.fromHtml(this)
    }
}


fun String?.isValidUrl(): Boolean {
    return this?.let {
        return URLUtil.isValidUrl(it)
    } ?: kotlin.run { return false }
}

fun String?.htmlfy(contentStyle: List<String>?): String? {
    val rawValue = this ?: return null
    val html =
        "<html dir = ${if (isRTLLocale()) "rtl" else "ltr"}><link rel=\"stylesheet\" href=\"https://fonts.googleapis.com/css?" +
                "family=Permanent+Marker|Lobster|Indie+Flower|Pacifico|Orbitron|Dancing+Script" +
                "|Dosis|Poiret+One|Kaushan+Script|Satisfy|Courgette|Seaweed+Script|Roboto\">" +
                "<link rel=\"stylesheet\" href=\"https://fonts.googleapis.com/css?family=Rakkas" +
                "|Lemonada|Cairo|Amiri|Changa|Lateef|Reem+Kufi|Scheherazade|El+Messiri|Harmattan" +
                "|Mirza|Katibeh\"><link rel=\"stylesheet\" href=\"www/newUiCSS.css\"><meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">"
    val body = contentStyle?.let {
        val contentFont = it.getOrNull(0) ?: "cp-roboto"
        val contentSize = it.getOrNull(1) ?: "mediumContent"
        val contentColor = it.getOrNull(2) ?: "#000000"
        val contentAlignment = it.getOrNull(3) ?: "left"
        "<body class=\"$contentFont $contentSize\" style = color:$contentColor;text-align:$contentAlignment>"
    } ?: run { "" }
    return "$html $body <div>" +
            rawValue.getDeeplinkHtml() +
            "</div>" +
            "</body> </html"
}

fun String?.getYoutubeThumbUrl(): String? {
    return if (this.isYoutubeUrl()) "https://i3.ytimg.com/vi/${this.getVideoId()}/maxresdefault.jpg" else this
}

fun String?.getStrBtTwoDelimilers(startStr: String?, endStr: String?): String? = this?.let {
    it.substring(it.indexOf(startStr ?: "") + 1, it.indexOf(endStr ?: ""))
}

fun String.removeFontWeight(): String =
    if (this.contains("&weight")) this.split("&weight")[0] else if (this.contains("&italic")) this.split("&italic")[0] else this

fun String?.getContactNo(): String = this?.replace("[^0-9]".toRegex(), "") ?: this ?: "qwerty"

fun String.isLikeLatLng(): Boolean {
    return if (this.contains(",") && this.split(",").size == 2) {
        val splitItems = this.split(",")
        splitItems.getOrNull(0).getDoubleValue() > 0.toDouble() || splitItems.getOrNull(1).getDoubleValue() > 0.toDouble()
    } else {
        false
    }
}

fun String.isImageFile(): Boolean {
    val imgExtensionArray = arrayListOf<String>("jpg", "jpeg", "png")
    val outputList = imgExtensionArray.filter { this.equals(it, ignoreCase = true) }
    return outputList.isNotEmpty()
}

fun String.isVideoFile(): Boolean {
    val imgExtensionArray = arrayListOf<String>("mp4", "mkv", "avi", "flv", "wmv", "mov")
    val outputList = imgExtensionArray.filter { this.equals(it, ignoreCase = true) }
    return outputList.isNotEmpty()
}

fun String.toEditable(): Editable = Editable.Factory.getInstance().newEditable(this)

fun String.getPdfPreviewUrl() = "https://drive.google.com/viewerng/viewer?embedded=true&url=$this"

fun String.formatUniversalDateAsAccount(appyDateFormat: String): String {
    try {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        inputFormat.timeZone = TimeZone.getTimeZone("GMT")
        val outputFormat = SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss", Locale.getDefault())
        val date = inputFormat.parse(this) ?: return this
        val dateFormatted = outputFormat.format(date)
        return dateFormatted + " GMT"
    } catch (e: ParseException) {
        e.printStackTrace()
        return this
    }
}



