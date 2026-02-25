package com.app.core.extensions

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

fun <T> List<T>.deepClone(): List<T> {
    try {
        val gson = Gson()
        val jsonString = gson.toJson(this)
        val listType = object : TypeToken<List<T>>() {}.type
        return gson.fromJson<List<T>>(jsonString, listType)
    } catch (e: Exception) {
        e.printStackTrace()
        return this
    }

}

