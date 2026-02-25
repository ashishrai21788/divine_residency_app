package com.app.divine.activity.language.model

/**
 * Data class representing a language item in the selection list
 */
data class LanguageItem(
    val code: String,
    val displayName: String,
    val nativeName: String,
    val flag: String,
    val isSelected: Boolean
) 