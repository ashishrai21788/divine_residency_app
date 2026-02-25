package com.app.core.extensions

import android.content.res.Resources
import android.graphics.Bitmap
import android.os.Handler
import android.os.Looper
import androidx.core.os.ConfigurationCompat
import androidx.core.text.TextUtilsCompat
import androidx.core.view.ViewCompat
import com.google.gson.Gson
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.net.Inet4Address
import java.net.InetAddress
import java.net.NetworkInterface
import java.security.SecureRandom
import java.util.*
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import kotlin.math.acos
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin


fun Any?.hashCodeString() = Integer.toHexString(System.identityHashCode(this))

fun randomNo(startRange: Long, finalRange: Long): Long{
    try {
        return (startRange..finalRange).random()
    }catch (e: java.lang.Exception){
        return 0.toLong()
    }
}

fun Any.toGson() = Gson().toJsonTree(this)

fun Bitmap.getResizeImage(maxSize: Int): Bitmap {

    val bitmapRatio = this.width.toFloat() / this.height.toFloat()
    if (bitmapRatio > 1) {
        width = maxSize
        height = width / bitmapRatio.toInt()
    } else {
        height = maxSize
        width = height * bitmapRatio.toInt()
    }

    val bitmap = this
    return Bitmap.createScaledBitmap(bitmap, width, height, true)
}


fun Any.toJSONObject(): JSONObject? {
    var appJsonData: JSONObject? = null
    val gson = Gson()
    try {
        val stringData = gson.toJson(this) //convert
        appJsonData = JSONObject(stringData)
        return appJsonData

    } catch (je: Throwable) {
        je.printStackTrace()
    }
    return appJsonData
}

fun Any.toJSONArray(): JSONArray? {
    var appJsonData: JSONArray? = null
    val gson = Gson()
    try {
        val stringData = gson.toJson(this) //convert
        appJsonData = JSONArray(stringData)
        return appJsonData

    } catch (je: Throwable) {
        je.printStackTrace()
    }
    return appJsonData
}

fun Any.logReport(message: String? = null, error: Throwable? = null) {
    error?.printStackTrace()
    loge(value = message)
}

fun Any.logReport(pageId: String, message: String, error: Throwable? = null) {
    error?.printStackTrace()
    loge(value = "pageId=$pageId, message=$message")
}

fun Any.mainThreadExecutor(): Executor {
    val handler = Handler(Looper.getMainLooper())
    return Executor { command -> handler.post(command) }
}

fun Any.computationExecutor(threadCount: Int = 4): Executor {
    return Executors.newFixedThreadPool(if (threadCount > 0) threadCount else 2)
}


enum class CoreDays {
    SUNDAY, MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY;

    companion object Factory {
        fun getToday(day: Int = Calendar.getInstance().get(Calendar.DAY_OF_WEEK)): CoreDays {
            return when (day) {
                1 -> SUNDAY
                2 -> MONDAY
                3 -> TUESDAY
                4 -> WEDNESDAY
                5 -> THURSDAY
                6 -> FRIDAY
                7 -> SATURDAY
                else -> SUNDAY
            }
        }

        fun fromDate(date: Date): CoreDays {
            val currentCalendar = Calendar.getInstance()
            currentCalendar.time = date
            return getToday(day = currentCalendar.get(Calendar.DAY_OF_WEEK))
        }

        fun fromDayName(name: String?): CoreDays {
            return when (name) {
                "Sunday" -> SUNDAY
                "Monday" -> MONDAY
                "Tuesday" -> TUESDAY
                "Wednesday" -> WEDNESDAY
                "Thursday" -> THURSDAY
                "Friday" -> FRIDAY
                "Saturday" -> SATURDAY
                else -> {
                    SUNDAY
                }
            }
        }
    }

    fun toIdString(): String {
        return when (this) {
            SUNDAY -> "0"
            MONDAY -> "1"
            TUESDAY -> "2"
            WEDNESDAY -> "3"
            THURSDAY -> "4"
            FRIDAY -> "5"
            SATURDAY -> "6"
        }
    }


    fun nameFirstCap(): String {
        return when (this) {
            SUNDAY -> "Sunday"
            MONDAY -> "Monday"
            TUESDAY -> "Tuesday"
            WEDNESDAY -> "Wednesday"
            THURSDAY -> "Thursday"
            FRIDAY -> "Friday"
            SATURDAY -> "Saturday"
        }
    }

    fun calendarDayId() = when (this) {
        SUNDAY -> 1
        MONDAY -> 2
        TUESDAY -> 3
        WEDNESDAY -> 4
        THURSDAY -> 5
        FRIDAY -> 6
        SATURDAY -> 7
    }

}


fun getSystemLocale(): Locale {
    val locales = ConfigurationCompat.getLocales(Resources.getSystem().configuration)
    return if (locales.isEmpty)  Locale.getDefault() else (locales.get(0) ?: Locale.getDefault())
}

fun isRTLLocale() = TextUtilsCompat.getLayoutDirectionFromLocale( Locale.getDefault()) == ViewCompat.LAYOUT_DIRECTION_RTL

fun getRandomNumber(n: Int = 5): Int {
    val m = 10.0.pow((n - 1).toDouble()).toInt()
    return m + Random().nextInt(9 * m)
}

//=============== Naveen ===========================
fun getRandomAlphaNumeric(n: Int = 5): String {
    val AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz"
    val sb = StringBuilder(n)
    for (i in 0 until n) sb.append(AB[SecureRandom().nextInt(AB.length)])
    return sb.toString()
}


fun calcDistanceBtwCoordinates(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
    return if (lat1 == lat2 && lon1 == lon2) {
        0.0
    } else {
        val theta = lon1 - lon2
        var dist =
            sin(Math.toRadians(lat1)) * sin(Math.toRadians(lat2)) + cos(Math.toRadians(lat1)) * cos(
                Math.toRadians(lat2)
            ) * cos(Math.toRadians(theta))
        dist = acos(dist)
        dist = Math.toDegrees(dist)
        dist *= 60.0 * 1.1515
        dist *= 1.609
        dist
    }
}


fun Any.getZopimHtmlData(zopimChatCode: String?, lang: String?): String? {

    return "<!DOCTYPE html>\n" +
            " <html lang='en'>\n" +
            "  <head> \n" +
            "  \t<title>Hello, World!</title>\n" +
            "  \t <meta charset='utf-8'/> \n" +
            "\n" +
            "<style>\n" +
            "     #loading {\n" +
            "    position: absolute;\n" +
            "    top: 50%;\n" +
            "    left: 50%;\n" +
            "  }\n" +
            "  </style>\n" +
            "  \n" +
            "  \t<!-- Start of  Zendesk Widget script -->\n" +
            "<script id=\"ze-snippet\" src=\"https://static.zdassets.com/ekr/snippet.js?key=$zopimChatCode\"> </script>\n" +
            "<!-- End of  Zendesk Widget script -->\n" +
            "\n" +
            "\n" +
            "\n" +
            "  \t <script type=\"text/javascript\"> \n" +
            "\n" +
            "\n" +
            "  function mycall(){\n" +
            "  \t \tsetTimeout(function(){\n" +
            "\n" +

            "  \t \t\t\n" +
            "  \t \t\tzE.activate();\n" +
            "\n" +
            "\n" +
            "hideMinimizeIcon();\n" +
            "\n" +
            "\n" +
            "  \t \t\tsetTimeout(function(){\n" +
            "          hideMinimizeIcon();\n" +
            "   \t \t\t},2000);\n" +
            "\n" +
            "  \t \t},3000);\n" +
            "\n" +
            "  \t }\n" +
            "\n" +
            "\n" +
            "\n" +
            "     function hideMinimizeIcon(){\n" +
            " var loadingLabel = document.getElementById(\"loading\");\n" +
            " loading.style.display = \"none\";\n" +
            "      console.log(document.getElementsByTagName(\"iframe\"))\n" +
            "\n" +
            "          var iframe = document.getElementById(\"webWidget\");\n" +
            "           console.log(iframe);\n" +
            "if (iframe) {\n" +
            "   \n" +
            "     var elmnts = iframe.contentWindow.document.getElementsByClassName(\"sc-jDwBTQ sc-fBuWsC kPYhIj sc-jlyJG QyaYx\");\n" +
            "           if(elmnts)\n" +
            "           {\n" +
            "            for (i = 0; i < elmnts.length; i++) { \n" +


            "if(elmnts[i].getAttribute(\"aria-label\")==\"Minimize widget\" || elmnts[i].getAttribute(\"aria-label\")==\"Minimise widget\" || elmnts[i].getAttribute(\"aria-label\")==\"Popout\")\n" +
            "              {\n" +
            "                  elmnts[i].style.display = \"none\";\n" +
            "              }" +

            " \n" +
            "\n" +
            "}\n" +
            "\n" +
            "           }\n" +
            "        \n" +
            "\n" +
            "\n" +
            "}\n" +
            "     \n" +
            "  \n" +
            "\n" +
            "     }\n" +
            "\n" +
            "  \t</script>\n" +
            "\n" +
            "  \t</head>\n" +
            "  \t <body onload=\"mycall()\"> \n" +
            "\n" +
            "<h2 id=\"loading\">Loading...</h2>" +
            "  \t \t</body>\n" +
            "  \t \t</html>\n"
}


fun isValidIp4Address(hostName: String?): Boolean {
    return try {
        Inet4Address.getByName(hostName) != null
    } catch (ex: java.lang.Exception) {
        false
    }
}


fun Any.proceedWithCatch(
    expression: () -> Any,
    catchExpression: ((e: java.lang.Exception) -> Any)? = null,
    pageId: String? = "",
    fallbackErrorMessage: String? = ""
) {
    try {
        expression()
    } catch (e1: java.lang.Exception) {
        catchExpression?.invoke(e1)
        logReport(
            pageId = pageId ?: "",
            message = e1.message ?: fallbackErrorMessage ?: "",
            error = e1
        )
    }
}

fun File?.getFileSizeInMb(): Double {
    val size = this?.length()?.toString().getDoubleValue()

    return size / (1024 * 1024)//1024 to Kb and then 1024 for MB
}


fun getLogDateDDMMYYY(): String {
    return Date().convertDateWithFormat(format = "dd-MM-yyyy", locale = Locale.US)
}

fun getIPAddress(useIPv4: Boolean=true): String {
    try {
        val interfaces: List<NetworkInterface> =
            Collections.list(NetworkInterface.getNetworkInterfaces())
        for (networkInterface in interfaces) {
            val addrs: List<InetAddress> = Collections.list(networkInterface.inetAddresses)
            for (addr in addrs) {
                if (!addr.isLoopbackAddress) {
                    val sAddr = addr.hostAddress.toUpperCase(Locale.US)
                    val isIPv4 = addr is Inet4Address
                    if (useIPv4) {
                        if (isIPv4) return sAddr
                    } else {
                        if (!isIPv4) {
                            val delim = sAddr.indexOf('%') // drop ip6 port suffix
                            return if (delim < 0) sAddr else sAddr.substring(0, delim)
                        }
                    }
                }
            }
        }
    } catch (ex: java.lang.Exception) {
    } // for now eat exceptions
    return ""
}