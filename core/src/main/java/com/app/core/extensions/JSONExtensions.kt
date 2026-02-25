package com.app.core.extensions

import com.google.gson.Gson
import org.json.JSONException
import org.json.JSONObject

fun Any?.convertAnyDataToJSONObject(): JSONObject? = this?.let {
    try {
        return JSONObject(Gson().toJson(this))
    } catch (je: JSONException) {
        logReport(message = "convertAnyDataToJSONObject", error = je)
    }
    return null
} ?: kotlin.run { null }