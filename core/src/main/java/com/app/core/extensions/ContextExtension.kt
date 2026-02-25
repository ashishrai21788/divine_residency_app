package com.app.core.extensions

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.ActivityManager
import android.app.AlertDialog
import android.content.*
import android.content.Context.ACTIVITY_SERVICE
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.graphics.drawable.Drawable
import android.location.Address
import android.location.Geocoder
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkInfo
import android.net.Uri
import android.net.wifi.WifiManager
import android.os.Build
import android.os.Environment
import android.os.Handler
import android.provider.MediaStore
import android.provider.Settings
import android.telephony.TelephonyManager
import android.text.format.Formatter
import android.util.DisplayMetrics
import android.util.Patterns
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.app.core.R
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.app.core.utils.AlertDialogListener
import com.app.core.utils.CoreTaskListener
import com.app.core.utils.FilePickUtils
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.io.IOException
import java.lang.Math.sqrt
import java.net.URISyntaxException
import java.util.*


fun Context.readFileFromAsset(fileName: String): String {
    return applicationContext.assets.open(fileName).bufferedReader().use {
        it.readText()
    }
}

fun Context.providePhoneIP(): String {
    val manager = applicationContext.getSystemService(Context.WIFI_SERVICE) as? WifiManager
    return manager?.connectionInfo?.ipAddress?.let { Formatter.formatIpAddress(it) } ?: ""
}

fun Context.getDeviceDimensions(): Pair<Int, Int> {
    var widthHeight: Pair<Int, Int>
    this.resources.displayMetrics.let {
        val dpHeight = it.heightPixels
        val dpWidth = it.widthPixels
        widthHeight = Pair(dpWidth, dpHeight)
    }

    return widthHeight
}

fun Context.isDeviceTab(): Boolean {
    val metrics = DisplayMetrics()
    (this as Activity).windowManager.defaultDisplay.getMetrics(metrics)
    val yInches = metrics.heightPixels / metrics.ydpi
    val xInches = metrics.widthPixels / metrics.xdpi
    val diagonalInches = sqrt(xInches * xInches + yInches * yInches.toDouble())
    return diagonalInches >= 6.5
}

fun Context.isInternetOn(): Boolean {
    val connectivityManager: ConnectivityManager =
        getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager ?: return false
    if (Build.VERSION.SDK_INT >= 29) {
        val capabilities: NetworkCapabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
                ?: return false
        when {
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> return true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> return true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> return true
        }
    } else {
        try {
            val activeNetworkInfo: NetworkInfo? = connectivityManager.activeNetworkInfo
            return activeNetworkInfo?.isConnected ?: false
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }
    return false
}



fun Context.runUIWithDelay(runnable: Runnable, delay: Long = 1000) {
    Handler().postDelayed(runnable, delay)
}



fun Context?.localBroadcast(intent: Intent) {
    this?.let {
        LocalBroadcastManager.getInstance(it).sendBroadcast(intent)
    }
}

fun Context?.localBroadcastAction(action: String) {
    this?.let {
        LocalBroadcastManager.getInstance(it).sendBroadcast(Intent(action))
    }
}

fun Activity?.openFilePicker(fileType: String, requestCode: Int) {
    this?.let {

        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.type = "$fileType/*"
        this.startActivityForResult(intent, requestCode)

    }
}

fun Activity?.openImageFilePicker(fileType: String, requestCode: Int) {
    this?.let {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.type = "$fileType/*"
        this.startActivityForResult(intent, requestCode)
    }
}

fun Activity?.openMultipleImagePicker(requestCode: Int) {
    this?.let {

        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        intent.type = "image/*"
        this.startActivityForResult(intent, requestCode)
    }
}

fun Activity?.openVideoPicker(requestCode: Int) {
    this?.let {

        val intent = Intent()
        intent.type = "video/*"
        intent.action = Intent.ACTION_GET_CONTENT
        this.startActivityForResult(Intent.createChooser(intent, "Select Video"), requestCode)

    }
}


fun Context?.showToastL(message: String?) {
    this?.let { context ->
        message?.let { text ->
            Toast.makeText(context, text, Toast.LENGTH_LONG).show()
        }
    }
}

fun Context?.showToastS(message: String?) {
    this?.let { context ->
        message?.let { text ->
            Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
        }
    }
}


@SuppressLint("HardwareIds")
fun Context.getDeviceId(): String {
    try {
        return Settings.Secure.getString(
            contentResolver,
            Settings.Secure.ANDROID_ID
        )
    } catch (e: java.lang.Exception) {
        return ""
    }

}


fun Context.getDrawableName(name: String): Drawable {
    var resourceId = resources.getIdentifier(name.replace("-", "_"), "drawable", packageName)
    if (resourceId == 0) {
        resourceId = resources.getIdentifier("fail", "drawable", packageName)
    }
    return resources.getDrawable(resourceId)
}

fun Context?.getIconName(name: String): String? {
    var resourceId =
        this?.resources?.getIdentifier(name.replace("-", "_"), "string", this.packageName)
    if (resourceId == 0) {
        resourceId = this?.resources?.getIdentifier("fail", "string", this.packageName)
    }
    return resourceId?.let { this?.resources?.getString(it) }
}

/**
 * this is use to show android alert dialog
 * with multiple options
 */
fun <T> Context.showConfirmationDialog(
    title: String,
    message: String,
    actionType: String,
    actionBtn: String,
    icon: Boolean,
    isSingleBtn: Boolean,
    data: T,
    listener: AlertDialogListener,
    cancelable: Boolean = true,
    cancelButtonName: String? = null

) {

    val status = false
    val alertDialog = AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle)
    if (title.isBlank()) {
        alertDialog.setTitle("appName")
    } else {
        alertDialog.setTitle(title)
    }

    alertDialog.setMessage(message)
    // Specifying a listener allows you to take an action before dismissing the dialog.
    // The dialog is automatically dismissed when a dialog button is clicked.
    alertDialog.setPositiveButton(actionBtn, DialogInterface.OnClickListener { _, _ ->
        listener.onOkClick(actionType, data)
    })
    //cancelablity of alert dialog
    alertDialog.setCancelable(cancelable)

    // A null listener allows the button to dismiss the dialog and take no further action.
    val noButtonText = "No"
    if (isSingleBtn) {
        alertDialog.setNegativeButton("", null)
    } else {
        cancelButtonName?.let {
            alertDialog.setNegativeButton(cancelButtonName, null)
        } ?: run {
            alertDialog.setNegativeButton(noButtonText, null)
        }
    }
    if (icon && status) {
        alertDialog.setIcon(if (status) R.drawable.success else R.drawable.fail)
    }
    alertDialog.show()
}

/**
 * this is use to show android alert dialog
 * with multiple options
 */
fun <T> Context.showConfirmationDialogForLink(
    title: String,
    message: String,
    actionType: String,
    actionBtn: String,
    icon: Boolean,
    isSingleBtn: Boolean,
    data: T,
    alertDialogListener: AlertDialogListenerLink,
    viewButtonText: String? = null
) {

    val status = false
    val alertDialog = AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle)
    if (title.isBlank()) {
        alertDialog.setTitle("AppName")
    } else {
        alertDialog.setTitle(title)

    }
    alertDialog.setMessage(message)
    // Specifying a listener allows you to take an action before dismissing the dialog.
    // The dialog is automatically dismissed when a dialog button is clicked.

    val defaultViewButtonText = "View"
    alertDialog.setNegativeButton(viewButtonText ?: defaultViewButtonText) { _, _ ->
        alertDialogListener.onViewClick(actionType, data)
    }

    alertDialog.setPositiveButton(actionBtn) { _, _ ->
        alertDialogListener.onOkClick(actionType, data)
    }

    // A null listener allows the button to dismiss the dialog and take no further action.
//        if (isSingleBtn) {
//            alertDialog.setNegativeButton("", null)
//        } else {
//            alertDialog.setNegativeButton(android.R.string.no, { dialog, which ->
//                alertDialogListener.onOkClick(actionType, data)
//            })
//        }
    if (icon && status) {
        alertDialog.setIcon(if (status) R.drawable.success else R.drawable.fail)
    }
    alertDialog.show()
}

/**
 * this is use to show android alert dialog
 * with multiple options
 */
fun <T> Context.showConfirmationDialogForLink(
    title: String,
    message: String,
    actionType: String,
    actionBtn: String,
    icon: Boolean,
    isSingleBtn: Boolean,
    cancelButton: String,
    data: T,
    alertDialogListener: AlertDialogListenerLink
) {

    val status = false
    val alertDialog = AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle)
    if (title.isBlank()) {
        alertDialog.setTitle("AppName")
    } else {
        alertDialog.setTitle(title)

    }
    alertDialog.setMessage(message)
    // Specifying a listener allows you to take an action before dismissing the dialog.
    // The dialog is automatically dismissed when a dialog button is clicked.
    alertDialog.setPositiveButton(actionBtn, DialogInterface.OnClickListener { dialog, which ->
        alertDialogListener.onOkClick(actionType, data)
    })


    alertDialog.setNegativeButton(cancelButton) { dialog, which ->
        alertDialogListener.onViewClick(actionType, data)

    }


    // A null listener allows the button to dismiss the dialog and take no further action.
//        if (isSingleBtn) {
//            alertDialog.setNegativeButton("", null)
//        } else {
//            alertDialog.setNegativeButton(android.R.string.no, { dialog, which ->
//                alertDialogListener.onOkClick(actionType, data)
//            })
//        }
    if (icon && status) {
        alertDialog.setIcon(R.drawable.fail)
    }
    alertDialog.show()

}


fun Context.openBrowser(url: String?) {
    try {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

fun Context.appName() = applicationInfo.loadLabel(packageManager)

fun Context.getOrientationText(): String = when (resources.configuration.orientation) {
    Configuration.ORIENTATION_LANDSCAPE -> "LANDSCAPE"
    Configuration.ORIENTATION_PORTRAIT -> "PORTRAIT"
    Configuration.ORIENTATION_UNDEFINED -> "UNDEFINED"
    else -> ""
}

fun Context.isPhoneStateGranted(): Boolean {
    val permission = Manifest.permission.READ_PHONE_STATE
    val res = checkCallingOrSelfPermission(permission)
    return (res == PackageManager.PERMISSION_GRANTED)
}


@SuppressLint("MissingPermission")
fun Context.getNetworkType(): String {
    return if (isPhoneStateGranted()) {
        val mTelephonyManager = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        when (mTelephonyManager.networkType) {
            TelephonyManager.NETWORK_TYPE_GPRS,
            TelephonyManager.NETWORK_TYPE_EDGE,
            TelephonyManager.NETWORK_TYPE_CDMA,
            TelephonyManager.NETWORK_TYPE_1xRTT,
            TelephonyManager.NETWORK_TYPE_IDEN -> "2g"
            TelephonyManager.NETWORK_TYPE_UMTS,
            TelephonyManager.NETWORK_TYPE_EVDO_0,
            TelephonyManager.NETWORK_TYPE_EVDO_A,
            TelephonyManager.NETWORK_TYPE_HSDPA,
            TelephonyManager.NETWORK_TYPE_HSUPA,
            TelephonyManager.NETWORK_TYPE_HSPA,
            TelephonyManager.NETWORK_TYPE_EVDO_B,
            TelephonyManager.NETWORK_TYPE_EHRPD,
            TelephonyManager.NETWORK_TYPE_HSPAP -> "3g"
            TelephonyManager.NETWORK_TYPE_LTE -> "4g"
            else -> "WIFI"
        }
    } else {
        "WIFI"
    }

}

fun Context.getOperatorName(): String? {
    return if (isPhoneStateGranted()) {
        val mTelephonyManager = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        return mTelephonyManager.networkOperatorName

    } else {
        null
    }

}


fun Context.hideSoftKeyBoard(view: View) {
    try {
        val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
    } catch (e: java.lang.Exception) {
    }

}


fun Context.getFilePath(uri: Uri): String {
    return FilePickUtils.getPath(this, uri) ?: getFilePathFromRemoteUri(uri = uri)
}

fun Context.getFilePathFromRemoteUri(uri: Uri): String {
    return FilePickUtils.getPathFromRemoteUri(this, uri) ?: ""
}

fun Context.sendEmail(addresses: Array<String?>, subject: String? = null, body: String? = null) {
    try {
        val emails = addresses.filter { it?.isNotBlank() == true }.toTypedArray()
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_EMAIL, emails)
            subject?.let {
                putExtra(Intent.EXTRA_SUBJECT, subject)
            }
            body?.let {
                putExtra(Intent.EXTRA_TEXT, it)
            }
        }
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        }
    } catch (e: java.lang.Exception) {
        e.printStackTrace()
    }
}


fun Context.isGPSEnabled(): Boolean {
    val locationManager = this.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
}

fun Context.getAddressFromLatLong(lat: Double, long: Double): String {
    var address: String = ""
    val geocoder: Geocoder
    val addresses: List<Address>?
    try {
        geocoder = Geocoder(this, Locale.getDefault())

        addresses = geocoder.getFromLocation(
            lat,
            long,
            1
        )
        if (addresses?.isNotEmpty() == true) {
            address = addresses[0].getAddressLine(0)
                ?: ""
        }


    } catch (e: IOException) {

    }
    return address
}

fun Context.getCircularProgressDrawable(): CircularProgressDrawable {
    val circularProgressDrawable = CircularProgressDrawable(this)
    circularProgressDrawable.strokeWidth = 5f
    circularProgressDrawable.centerRadius = 30f

    return circularProgressDrawable
}

fun Context?.getFileProviderUri(file: File?): Uri? {
    return try {
        this?.applicationContext?.let {
            file?.let { file ->
                FileProvider.getUriForFile(
                    it,
                    this.applicationContext.packageName + ".provider",
                    file
                )
            }
        }
    } catch (ex: java.lang.Exception) {
        ex.printStackTrace()
        null
    }
}

@SuppressLint("LogNotTimber")
fun Context.isValidForCommonWebView(webViewUrl: String?, nativeOsBrowser: String? = ""): Boolean {
    if (!webViewUrl.isNotNullOrEmpty()) return false
    var isValid = false
    var url = webViewUrl ?: ""
    try {
        if (url.startsWith("http") == false) {
            url = "http://$url"
        }
        if (url.startsWith("https://goo.gl/maps/") || url.startsWith("http://goo.gl/maps/") || url.startsWith(
                "http://plus.codes"
            ) || url.startsWith("https://plus.codes") || url.contains("maps.google.com") || url.contains(
                "https://www.google.com/maps"
            )
        ) {
            val gmmIntentUri = Uri.parse(url)
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            this.startActivity(mapIntent)
        } else {
            if (nativeOsBrowser != null && nativeOsBrowser.equals("true", ignoreCase = true)) {
                val i = Intent(Intent.ACTION_VIEW)
                i.data = Uri.parse(url)
                this.startActivity(i)

            } else {
                isValid = true
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return isValid
}

fun Context?.getAppVersionCode(): Int {
    if (this == null) return 0
    return try {
        this.packageManager.getPackageInfo(applicationContext.packageName, 0).versionCode
    } catch (e: java.lang.Exception) {
        e.printStackTrace()
        0
    }
}

fun Context?.openAppOnPlayStore() {
    try {
        val appPackageName: String = this?.applicationContext?.packageName ?: return
        try {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("market://details?id=$appPackageName")
                )
            )
        } catch (error: ActivityNotFoundException) {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/details?id=$appPackageName")
                )
            )
        }
    } catch (e: java.lang.Exception) {
        e.printStackTrace()
    }
}


fun Context.isTestFlightApp() =
    applicationContext.packageName == "com.app.testpiwikbasicapp" || applicationContext.packageName == "com.app.testpiwikapp" || applicationContext.packageName == "com.app.testlab"

fun Context.isDevelopmentBuild() = applicationContext.packageName == "com.app.mNative"


fun Context.hasPermissionInManifest(permission: String): Boolean {
    val packageInfo =
        this.packageManager.getPackageInfo(
            this.applicationContext.packageName,
            PackageManager.GET_PERMISSIONS
        )
    val permissions = packageInfo.requestedPermissions
    if (permissions.isNullOrEmpty())
        return false
    for (perm in permissions) {
        if (perm == permission)
            return true
    }

    return false
}


fun Context.getCountryISO(): String? {
    val manager: TelephonyManager? =
        this.getSystemService(Context.TELEPHONY_SERVICE) as? TelephonyManager
    var countryISO = manager?.simCountryIso?.toUpperCase(Locale.US)
    if (countryISO.isNullOrEmpty()) {
        countryISO = manager?.networkCountryIso
    }
    return if (countryISO.isNotNullOrEmpty()) countryISO?.toUpperCase(Locale.US) else null
}

fun Context.checkPermissionsArray(permissions: Array<String>): Boolean {
    var permissionAllowed = true
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
        return true
    }

    permissions.forEach permission@{ permission ->
        val result = this.checkSelfPermission(permission)
        if (result == PackageManager.PERMISSION_DENIED) {
            permissionAllowed = false
            return@permission
        }
    }
    return permissionAllowed
}

fun Context.handleDefaultUri(url: String): Boolean {
    when {
        url.startsWith("https://goo.gl/maps/") || url.startsWith("http://goo.gl/maps/") || url.startsWith(
            "http://plus.codes"
        ) || url.startsWith(
            "https://plus.codes"
        ) || url.contains("maps.google.com") -> {
            try {
                val gmsIntentUri = Uri.parse(url)
                val mapIntent = Intent(Intent.ACTION_VIEW, gmsIntentUri)
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent)
            } catch (e: java.lang.Exception) {
            }

            return true
        }
        url.contains("whatsapp") -> {
            try {
                var intent = applicationContext.packageManager.getLaunchIntentForPackage("com.whatsapp")
                    ?: applicationContext.packageManager.getLaunchIntentForPackage("com.whatsapp.w4b")
                if (intent != null) {
                    intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                    startActivity(intent)
                } else {
                    val marketIntent = Intent(Intent.ACTION_VIEW)
                    marketIntent.data = Uri.parse("market://details?id=com.whatsapp")
                    startActivity(marketIntent)
                }
            } catch (e: java.lang.Exception) {
            }

        }
        url.startsWith("intent://") -> {
            try {
                val intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME)
                val existPackage = intent.getPackage()
                    ?.let { getPackageManager().getLaunchIntentForPackage(it) }
                if (existPackage != null) {
                    intent.addCategory("android.intent.category.BROWSABLE")
                    intent.component = null
                    intent.selector = null
                    startActivity(intent)
                } else {
                    val marketIntent = Intent(Intent.ACTION_VIEW)
                    marketIntent.data = Uri.parse("market://details?id=" + intent.getPackage())
                    startActivity(marketIntent)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return true
        }
        url.startsWith("market://") -> {
            try {
                val intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME)
                if (intent != null) {
                    intent.addCategory("android.intent.category.BROWSABLE")
                    intent.component = null
                    intent.selector = null
                    startActivity(intent)
                }
            } catch (e: URISyntaxException) {
                e.printStackTrace()
            }
            return true
        }

        url.startsWith("sms:") -> {
            try {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.addCategory(Intent.CATEGORY_DEFAULT)
                intent.data = Uri.parse(url)
                startActivity(intent)
            } catch (e: java.lang.Exception) {
            }
            return true
        }
        url.startsWith("tel:") -> {
            try {
                val intent = Intent(Intent.ACTION_DIAL, Uri.parse(url))
                startActivity(intent)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return true
        }
        url.startsWith("mailto:") -> {
            try {
                val gg = url.split("\\?")
                val splitEmail = gg[0]
                val splitEmaill =
                    splitEmail.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                val toEmail = splitEmaill[1]

                var sendSubject = ""
                try {
                    val subjectContent = gg[1]
                    val splitSubject =
                        subjectContent.split("=".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                    sendSubject = splitSubject[1]
                    sendSubject = sendSubject.replace("%20", " ")
                } catch (e: Exception) {
                    e.printStackTrace()
                }

                val intent = Intent(Intent.ACTION_SENDTO)
                intent.data = Uri.parse("mailto:")
                intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(toEmail))
                intent.putExtra(Intent.EXTRA_SUBJECT, sendSubject)
                intent.putExtra(Intent.EXTRA_TEXT, "")
                if (intent.resolveActivity(packageManager) != null) {
                    startActivity(intent)
                } else {
                    startActivity(Intent(Intent.ACTION_SENDTO, Uri.parse(toEmail)))
                }
            } catch (e: java.lang.Exception) {
            }
            return true
        }
        Patterns.EMAIL_ADDRESS.matcher(url).matches() -> {
            val intent = Intent(Intent.ACTION_SENDTO)
            intent.data = Uri.parse("mailto:$url")
            if (intent.resolveActivity(packageManager) != null) {
                startActivity(intent)
            }
        }
    }

    return false
}


fun Context.getStorageDirectoryPath(): File {
    val storagePath =
        File(Environment.getExternalStorageDirectory(), this.getString(R.string.app_name) + File.separator)
    if (!storagePath.exists()) {
        storagePath.mkdirs()
    }
    return storagePath
}

fun Context.cacheImage(imageUrl: String) {
        try {
            Glide.with(this)
                .load(imageUrl)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(object : CustomTarget<Drawable>() {
                    override fun onLoadCleared(placeholder: Drawable?) {

                    }

                    override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {

                    }
                })
        }catch (e:Exception){
            e.printStackTrace()
        }
}



fun Context.checkTopRunningActivity(activityName: String): Boolean {
    val tasks = (getSystemService(ACTIVITY_SERVICE) as ActivityManager).getRunningTasks(10)
    return tasks.any { runningTaskInfo -> runningTaskInfo.topActivity?.className?.contains(activityName) == true }
}


fun Context.getDeniedPermissions(requestedPermissions: List<String>): List<String> {
    return requestedPermissions.filter { permission ->
        ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED
    }
}

fun Context.getWhatsAppPackage(): String {
    val sendIntent = Intent("android.intent.action.MAIN")
    sendIntent.action = Intent.ACTION_VIEW
    sendIntent.setPackage("com.whatsapp")
    val url = "https://api.whatsapp.com/send?phone= +919999999990 &text=asdsad"
    sendIntent.data = Uri.parse(url)
    return if (packageManager?.let { packageManager -> sendIntent.resolveActivity(packageManager) } != null) "com.whatsapp" else "com.whatsapp.w4b"
}

fun Context.provideFileFromUri(uri: Uri?): File? {
    if (uri == null) return null
    val extension = getExtension(uri) ?: ""
    try {
        val inputStream = contentResolver.openInputStream(uri) ?: return null
        val file = (when {
            extension.isVideoFile() -> {
                createVideoMediaFile(extension)
            }
            extension.isImageFile() -> {
                createImageMediaFile(extension)
            }
            else -> {
                createFile(extension)
            }
        }) ?: return null
        inputStream.copyTo(file.outputStream())
        inputStream.close()
        return file
    } catch (ex: java.lang.Exception) {
        return null
    }
}


fun Context.provideUriFromFile(file: File): Uri? {
    return FileProvider.getUriForFile(this, applicationContext?.packageName + ".provider", file)
}

/**
 * We create a [Uri] where the image will be stored
 */
fun Context.createImageMediaFile(extension: String): File {
    val storageDir = this.filesDir
    return File.createTempFile("IMG_${System.currentTimeMillis()}_", ".${extension}", storageDir)
}

fun Context.createFile(extension: String): File {
    val storageDir = this.filesDir
    return File.createTempFile("FILE_${System.currentTimeMillis()}_", ".${extension}", storageDir)
}


/**
 * We create a [Uri] where the image will be stored
 */
fun Context.createVideoMediaFile(extension: String): File? {
    val storageDir = this.filesDir
    return File.createTempFile("VID_${System.currentTimeMillis()}_", ".${extension}", storageDir)
}


/**
 * [saveRandomImageFromInternet] downloads a random image from unsplash.com and saves its
 * content
 */
fun Context.saveRandomImageFromInternet(imageUrl: String, actionType: String, coreTaskListener: CoreTaskListener) {
    val tmpImageFile = createImageMediaFile("jpg")
    // We use OkHttp to create HTTP request
    val request = Request.Builder().url(imageUrl).build()
    provideUriFromFile(file = tmpImageFile)?.let { destinationUri ->
        val response = OkHttpClient().newCall(request).execute()
        // .use is an extension function that closes the output stream where we're
        // saving the image content once its lambda is finished being executed
        response.body?.use { responseBody ->
            contentResolver.openOutputStream(destinationUri, "w")?.use {
                responseBody.byteStream().copyTo(it)
                coreTaskListener.onCoreTaskResult(actionType = actionType, data = destinationUri)
            }
        }
    }
}

fun Context.getMimeType(uri: Uri?): String? {
    return try {
        uri?.let { uriValue ->
            var mimeType = contentResolver.getType(uriValue)
            mimeType?.let { mime ->
                return mime
            } ?: run {
                mimeType = MimeTypeMap.getFileExtensionFromUrl(uriValue.toString())
                return mimeType
            }
        } ?: return null
    } catch (e: Exception) {
        null
    }
}

fun Context.getExtension(uri: Uri): String? {
    val mimeTypeMap = MimeTypeMap.getSingleton()
    return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri))
}

fun Context.updateAdMobApplicationId(applicationId: String? = null) {
    val id = applicationId ?: return
    if (!id.isNotNullOrEmpty() || !id.contains("~")) return
    try {
        val applicationMetaData = applicationContext.packageManager.getApplicationInfo(applicationContext.packageName, PackageManager.GET_META_DATA)
        applicationMetaData.metaData.putString("com.google.android.gms.ads.APPLICATION_ID", applicationId)
    } catch (e: java.lang.Exception) {

    }
}


fun Context.isMapInstalledOrNot(uri: String): Boolean {
    val pm = applicationContext.packageManager
    try {
        pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES)
        return true
    } catch (e: PackageManager.NameNotFoundException) {
        return false
    }
    return false
}


fun Context.getColorRes(@ColorRes colorId: Int) = ContextCompat.getColor(this, colorId)
