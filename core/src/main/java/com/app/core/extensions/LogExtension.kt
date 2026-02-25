package com.app.core.extensions

import android.annotation.SuppressLint
import android.util.Log
//import com.app.core.BuildConfig
import com.app.core.utils.CoreMetaData

@SuppressLint("LogNotTimber")
fun Any.loge(tag: String = "", value: String?) {
    //if (!(BuildConfig.DEBUG || CoreMetaData.shouldEnableReleaseLog)) return
    val customTag = if (tag.isNotEmpty()) tag else this.javaClass.simpleName
    val messageToDisplay = value ?: "empty message"
    Log.e(customTag, if (tag.isNotEmpty()) "${this.javaClass.simpleName} >> $messageToDisplay" else messageToDisplay)
}

@SuppressLint("LogNotTimber")
fun Any.logd(tag: String = "", value: String?) {
    //if (!(BuildConfig.DEBUG || CoreMetaData.shouldEnableReleaseLog)) return
    val customTag = if (tag.isNotEmpty()) tag else this.javaClass.simpleName
    val messageToDisplay = value ?: "empty message"
    Log.d(customTag, if (tag.isNotEmpty()) "${this.javaClass.simpleName} >> $messageToDisplay" else messageToDisplay)
}

@SuppressLint("LogNotTimber")
fun Any.logi(tag: String = "", value: String?) {
    //if (!(BuildConfig.DEBUG || CoreMetaData.shouldEnableReleaseLog)) return
    val customTag = if (tag.isNotEmpty()) tag else this.javaClass.simpleName
    val messageToDisplay = value ?: "empty message"
    Log.i(customTag, if (tag.isNotEmpty()) "${this.javaClass.simpleName} >> $messageToDisplay" else messageToDisplay)
}

@SuppressLint("LogNotTimber")
fun Any.logv(tag: String = "", value: String?) {
    //if (!(BuildConfig.DEBUG || CoreMetaData.shouldEnableReleaseLog)) return
    val customTag = if (tag.isNotEmpty()) tag else this.javaClass.simpleName
    val messageToDisplay = value ?: "empty message"
    Log.v(customTag, if (tag.isNotEmpty()) "${this.javaClass.simpleName} >> $messageToDisplay" else messageToDisplay)
}

@SuppressLint("LogNotTimber")
fun Any.logw(tag: String = "", value: String?) {
   // if (!(BuildConfig.DEBUG || CoreMetaData.shouldEnableReleaseLog)) return
    val customTag = if (tag.isNotEmpty()) tag else this.javaClass.simpleName
    val messageToDisplay = value ?: "empty message"
    Log.w(customTag, if (tag.isNotEmpty()) "${this.javaClass.simpleName} >> $messageToDisplay" else messageToDisplay)
}