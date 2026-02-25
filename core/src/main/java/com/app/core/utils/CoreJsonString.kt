package com.app.core.utils

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import org.json.JSONArray
import org.json.JSONObject

@Parcelize
data class CoreJsonString(val data: String? = null) : Parcelable {

    fun asJSONObject() = try {
        JSONObject(data)
    } catch (e: Exception) {
        null
    }

    fun asJSONArray() = try {
        JSONArray(data)
    } catch (e: Exception) {
        null
    }

    fun isJSONArray() = asJSONArray() != null
    fun isJSONObject() = asJSONObject() != null
}