package com.app.divine.utils

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import org.json.JSONObject
import java.io.IOException

object LanguageManager {
    private const val TAG = "LanguageManager"
    private const val PREF_NAME = "language_preferences"
    private const val KEY_SELECTED_LANGUAGE = "selected_language"
    private const val DEFAULT_LANGUAGE = "en"
    
    private var languageData: JSONObject? = null
    private var currentLanguage: String = DEFAULT_LANGUAGE
    private lateinit var sharedPreferences: SharedPreferences
    
    /**
     * Initialize the language manager by reading the JSON file
     * @param context Application context
     */
    fun initialize(context: Context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        currentLanguage = getSelectedLanguage()
        loadLanguageFile(context, currentLanguage)
    }
    
    /**
     * Load language file for the specified language code
     * @param context Application context
     * @param languageCode Language code (e.g., "en", "hi", "ar")
     */
    private fun loadLanguageFile(context: Context, languageCode: String) {
        try {
            val fileName = "language_$languageCode.json"
            val inputStream = context.assets.open(fileName)
            val size = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()
            
            val jsonString = String(buffer, Charsets.UTF_8)
            languageData = JSONObject(jsonString)
            currentLanguage = languageCode
            
            Log.d(TAG, "Language file loaded successfully: $fileName")
        } catch (e: IOException) {
            Log.e(TAG, "Error reading language file for $languageCode: ${e.message}")
            // Fallback to default language if the requested language file doesn't exist
            if (languageCode != DEFAULT_LANGUAGE) {
                Log.w(TAG, "Falling back to default language: $DEFAULT_LANGUAGE")
                loadLanguageFile(context, DEFAULT_LANGUAGE)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error parsing language file for $languageCode: ${e.message}")
            // Fallback to default language if parsing fails
            if (languageCode != DEFAULT_LANGUAGE) {
                Log.w(TAG, "Falling back to default language: $DEFAULT_LANGUAGE")
                loadLanguageFile(context, DEFAULT_LANGUAGE)
            }
        }
    }
    
    /**
     * Get a string value from the language JSON
     * @param key The key to look for (supports dot notation like "login.title")
     * @return The string value or the key itself if not found
     */
    fun getString(key: String): String {
        return try {
            if (languageData == null) {
                Log.w(TAG, "Language data not initialized")
                return key
            }
            
            val keys = key.split(".")
            var currentObject = languageData
            
            for (i in keys.indices) {
                if (i == keys.size - 1) {
                    // Last key, return the string value
                    return currentObject?.getString(keys[i]) ?: key
                } else {
                    // Navigate to nested object
                    currentObject = currentObject?.getJSONObject(keys[i])
                    if (currentObject == null) {
                        Log.w(TAG, "Key not found: $key")
                        return key
                    }
                }
            }
            
            key
        } catch (e: Exception) {
            Log.e(TAG, "Error getting string for key '$key': ${e.message}")
            key
        }
    }
    
    /**
     * Get a string value with default fallback
     * @param key The key to look for
     * @param defaultValue Default value if key is not found
     * @return The string value or default value
     */
    fun getString(key: String, defaultValue: String): String {
        val value = getString(key)
        return if (value == key) defaultValue else value
    }
    
    /**
     * Set the selected language and reload the language file
     * @param context Application context
     * @param languageCode Language code (e.g., "en", "hi", "ar")
     */
    fun setLanguage(context: Context, languageCode: String) {
        if (currentLanguage != languageCode) {
            saveSelectedLanguage(languageCode)
            loadLanguageFile(context, languageCode)
            
            // Only notify listeners if context is valid
            try {
                // Notify all listeners about the language change
                LanguageChangeManager.notifyLanguageChanged(languageCode)
            } catch (e: Exception) {
                Log.e(TAG, "Error notifying language change: ${e.message}")
            }
        }
    }
    
    /**
     * Get the currently selected language code
     * @return Current language code
     */
    fun getCurrentLanguage(): String {
        return currentLanguage
    }
    
    /**
     * Get the selected language from SharedPreferences
     * @return Selected language code or default language
     */
    private fun getSelectedLanguage(): String {
        return sharedPreferences.getString(KEY_SELECTED_LANGUAGE, DEFAULT_LANGUAGE) ?: DEFAULT_LANGUAGE
    }
    
    /**
     * Save the selected language to SharedPreferences
     * @param languageCode Language code to save
     */
    private fun saveSelectedLanguage(languageCode: String) {
        sharedPreferences.edit().putString(KEY_SELECTED_LANGUAGE, languageCode).apply()
    }
    
    /**
     * Get available languages from assets folder
     * @param context Application context
     * @return List of available language codes
     */
    fun getAvailableLanguages(context: Context): List<String> {
        val languages = mutableListOf<String>()
        try {
            val files = context.assets.list("")
            files?.forEach { fileName ->
                if (fileName.startsWith("language_") && fileName.endsWith(".json")) {
                    val languageCode = fileName.removePrefix("language_").removeSuffix(".json")
                    languages.add(languageCode)
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error getting available languages: ${e.message}")
        }
        return languages.sorted()
    }
    
    /**
     * Check if the language manager is initialized
     * @return true if initialized, false otherwise
     */
    fun isInitialized(): Boolean {
        return languageData != null
    }
    
    /**
     * Clear the language data (useful for testing or memory management)
     */
    fun clear() {
        languageData = null
    }
} 