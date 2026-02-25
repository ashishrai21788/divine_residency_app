package com.app.core.utils

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.app.core.extensions.convertIntoModels


class AppySharedPreference(con: Context) {
    val LATITUDE_KEY: String = "latitude_key"
    val LONGITUDE_KEY: String = "longitude_key"
    val LOCATION_KEY: String = "location_key"
    val IS_LOGIN_KEY: String = "is_login_key"
    val IS_FIRST_OPEN_KEY: String = "is_first_open_key"
    val IS_ALREADY_INSTALLED: String = "is_already_installed"
    val IS_TOUCH_PERMISSION_TAKEN: String = "is_touch_permission_taken"
    val APP_SHARED_PREF_KEY: String = "com.app_shared_pref_key"
    val IS_TOUCH_ID_ENABLED: String = "is_touch_id_enabled"
    val CHAT_ROOM_USERNAME: String = "chat_room_username"
    val CHAT_ROOM_USER_IMAGE: String = "chat_room_image"
    val CURRENT_DATE_KEY: String = "is_currentdate_key"

    val APP_FCM_ID: String = "fcm_id"
    val COUNTRY_ISO: String = "country_iso"
    val COUNTRY_NAME: String = "country_name"
    val APP_NOTIFICATION_LIST: String = "app_notification_list"
    val IS_TERMS_AND_CONDITIONS_ACCEPTED = "terms_acceptance_status"
    val IS_PRIVACY_POLICY_ACCEPTED = "privacy_policy_acceptance_status"
    val IS_DISCLAIMER_ACCEPTED = "disclaimer_acceptance_status"
    val IS_COOKIES_POLICY_ACCEPTED = "cookies_acceptance_status"
    val IS_USER_LICENSE_ACCEPTED = "user_license_acceptance_status"
    val IS_USER_ENABLED_REMEMBERED_ME = "is_user_enabled_remember_me"

    val AWS_CONFIGURATION_JSON = "aws_configuration_json"

    // if false > show ProfileFragment
    val FB_GROUP_INFO_UPDATE_STATUS = "fb_group_info_update_status"

    val DATING_PAGE_IDENTIFIER: String = "dating_page_identifier"

    val USER_EMAIL: String = "BID_user_email"
    val USER_PASSWORD: String = "BID_user_password"

    val LAST_KNOWN_LATITUDE: String = "last_known_latitude"
    val LAST_KNOWN_LONGITUDE: String = "last_known_logitude"


    /* for tiktik user */
    val isFirebaseUserCreated: String = "isFirebaseUserCreated"

    val IS_APP_CRASHED="is_app_crashed"


    private var sharedPref: SharedPreferences


    init {
        sharedPref = con.getSharedPreferences(APP_SHARED_PREF_KEY, Context.MODE_PRIVATE)
    }

    fun contains(key: String) = sharedPref.contains(key)


    // save all string type preferences here by sending key and value
    fun setAllPreferenceTypeString(preferenceKey: String, value: String) {
        val editor = sharedPref.edit()
        editor.putString(preferenceKey, value)
        editor.apply()
    }

    // get all string type preferences here be sending key
    fun getAllPreferenceTypeString(preferenceKey: String): String {
        return sharedPref.getString(preferenceKey, "") ?: ""
    }


    // save all Boolean type preferences here be sending key and value
    fun setAllPreferenceTypeBoolean(preferenceKey: String, value: Boolean) {
        val editor = sharedPref.edit()
        editor.putBoolean(preferenceKey, value)
        editor.apply()
    }

    // get all Boolean type preferences here be sending key
    fun getAllPreferenceTypeBoolean(preferenceKey: String): Boolean {
        return sharedPref.getBoolean(preferenceKey, false)
    }

    fun saveArrayList(list: MutableList<String>, key: String) {
        val editor = sharedPref.edit()
        val gson = Gson()
        val json = gson.toJson(list)
        editor.putString(key, json)
        editor.apply()     // This line is IMPORTANT !!!
    }

    fun getArrayList(key: String): MutableList<String> {
        val gson = Gson()
        val json = sharedPref.getString(key, null)
        val type = object : TypeToken<List<String>>() {}
        return (json.convertIntoModels(type) ?: listOf()).toMutableList()


    }

    fun setLatitude(latitude: Double) {
        val editor = sharedPref.edit()
        editor.putString(LATITUDE_KEY, latitude.toString())
        editor.apply()
    }

    fun getLatitude(): Double {
        return sharedPref.getString(LATITUDE_KEY, "0.0")?.toDouble() ?: 0.0
    }

    fun setLongitude(longitude: Double) {
        val editor = sharedPref.edit()
        editor.putString(LONGITUDE_KEY, longitude.toString())
        editor.apply()
    }

    fun getLongitude(): Double {
        return sharedPref.getString(LONGITUDE_KEY, "0.0")?.toDouble() ?: 0.0
    }

    fun setLocation(location: String?) {
        val editor = sharedPref.edit()
        editor.putString(LOCATION_KEY, location)
        editor.apply()
    }

    fun getLocation(): String? {
        return sharedPref.getString(LOCATION_KEY, null)
    }

    fun setLogin(isLogin: Boolean) {
        val editor = sharedPref.edit()
        editor.putBoolean(IS_LOGIN_KEY, isLogin)
        editor.apply()
    }

    fun getLogin(): Boolean {
        return sharedPref.getBoolean(IS_LOGIN_KEY, false)
    }

    fun setFirstOpen(isFirstOpen: Boolean) {
        val editor = sharedPref.edit()
        editor.putBoolean(IS_FIRST_OPEN_KEY, isFirstOpen)
        editor.apply()
    }

    fun getFirstOpen(): Boolean {
        return sharedPref.getBoolean(IS_FIRST_OPEN_KEY, true)
    }

    fun clearPreferences() {
        sharedPref.edit().clear().apply()
    }


    fun storeCharRoomUserDetails(name: String, image: String? = null) {
        val editor = sharedPref.edit()
        editor.putString(CHAT_ROOM_USERNAME, name)
        if (image != null) {
            editor.putString(CHAT_ROOM_USER_IMAGE, image)
        }

        editor.apply()
    }

    fun getUsernameChatRoom(): String? {
        return sharedPref.getString(CHAT_ROOM_USERNAME, null)
    }

    fun getUserDetailsChatRoom(): List<String> {
        val list = listOf<String>(
            sharedPref.getString(CHAT_ROOM_USERNAME, "") as String,
            sharedPref.getString(CHAT_ROOM_USER_IMAGE, "") as String
        )
        return list
    }


    fun shouldLoadFullScreenAd(): Boolean {
        return sharedPref.getInt("full_screen_ad_count", 0) > 5
    }

    fun increaseFullScreenOffSet() {
        val offSet = sharedPref.getInt("full_screen_ad_count", 0)
        sharedPref.edit().putInt("full_screen_ad_count", offSet + 1).apply()
    }

    fun resetFullScreenOffSet() {
        sharedPref.edit().putInt("full_screen_ad_count", 0).apply()
    }

}