package com.app.divine.utils

import android.widget.TextView
import androidx.appcompat.widget.Toolbar

/**
 * Extension function to set text from language file
 * @param key The language key (supports dot notation like "login.title")
 */
fun TextView.setTextFromLanguage(key: String) {
    this.text = LanguageManager.getString(key)
}

/**
 * Extension function to set text from language file with default value
 * @param key The language key
 * @param defaultValue Default value if key is not found
 */
fun TextView.setTextFromLanguage(key: String, defaultValue: String) {
    this.text = LanguageManager.getString(key, defaultValue)
}

/**
 * Extension function to set toolbar title from language file
 * @param key The language key
 */
fun Toolbar.setTitleFromLanguage(key: String) {
    this.title = LanguageManager.getString(key)
}

/**
 * Extension function to set toolbar title from language file with default value
 * @param key The language key
 * @param defaultValue Default value if key is not found
 */
fun Toolbar.setTitleFromLanguage(key: String, defaultValue: String) {
    this.title = LanguageManager.getString(key, defaultValue)
} 