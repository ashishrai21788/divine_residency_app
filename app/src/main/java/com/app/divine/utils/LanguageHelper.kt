package com.app.divine.utils

import android.content.Context

/**
 * Helper class for language-related utilities
 */
object LanguageHelper {
    
    /**
     * Language data class containing language code and display name
     */
    data class Language(
        val code: String,
        val displayName: String,
        val nativeName: String
    )
    
    /**
     * Get available languages with their display names
     * @param context Application context
     * @return List of available languages
     */
    fun getAvailableLanguages(context: Context): List<Language> {
        val languageCodes = LanguageManager.getAvailableLanguages(context)
        return languageCodes.map { code ->
            Language(
                code = code,
                displayName = getLanguageDisplayName(code),
                nativeName = getLanguageNativeName(code)
            )
        }
    }
    
    /**
     * Get the display name for a language code
     * @param languageCode Language code (e.g., "en", "hi", "ar")
     * @return Display name in English
     */
    private fun getLanguageDisplayName(languageCode: String): String {
        return when (languageCode.lowercase()) {
            "en" -> "English"
            "hi" -> "Hindi"
            "ar" -> "Arabic"
            "es" -> "Spanish"
            "fr" -> "French"
            "de" -> "German"
            "it" -> "Italian"
            "pt" -> "Portuguese"
            "ru" -> "Russian"
            "ja" -> "Japanese"
            "ko" -> "Korean"
            "zh" -> "Chinese"
            "bn" -> "Bengali"
            "te" -> "Telugu"
            "ta" -> "Tamil"
            "mr" -> "Marathi"
            "gu" -> "Gujarati"
            "kn" -> "Kannada"
            "ml" -> "Malayalam"
            "pa" -> "Punjabi"
            else -> languageCode.uppercase()
        }
    }
    
    /**
     * Get the native name for a language code
     * @param languageCode Language code (e.g., "en", "hi", "ar")
     * @return Native name of the language
     */
    private fun getLanguageNativeName(languageCode: String): String {
        return when (languageCode.lowercase()) {
            "en" -> "English"
            "hi" -> "हिंदी"
            "ar" -> "العربية"
            "es" -> "Español"
            "fr" -> "Français"
            "de" -> "Deutsch"
            "it" -> "Italiano"
            "pt" -> "Português"
            "ru" -> "Русский"
            "ja" -> "日本語"
            "ko" -> "한국어"
            "zh" -> "中文"
            "bn" -> "বাংলা"
            "te" -> "తెలుగు"
            "ta" -> "தமிழ்"
            "mr" -> "मराठी"
            "gu" -> "ગુજરાતી"
            "kn" -> "ಕನ್ನಡ"
            "ml" -> "മലയാളം"
            "pa" -> "ਪੰਜਾਬੀ"
            else -> languageCode.uppercase()
        }
    }
    
    /**
     * Get the current language display name
     * @return Current language display name
     */
    fun getCurrentLanguageDisplayName(): String {
        val currentCode = LanguageManager.getCurrentLanguage()
        return getLanguageDisplayName(currentCode)
    }
    
    /**
     * Get the current language native name
     * @return Current language native name
     */
    fun getCurrentLanguageNativeName(): String {
        val currentCode = LanguageManager.getCurrentLanguage()
        return getLanguageNativeName(currentCode)
    }
    
    /**
     * Check if the current language is RTL (Right-to-Left)
     * @return true if RTL, false if LTR
     */
    fun isCurrentLanguageRTL(): Boolean {
        val currentCode = LanguageManager.getCurrentLanguage()
        return when (currentCode.lowercase()) {
            "ar", "he", "fa", "ur" -> true
            else -> false
        }
    }
    
    /**
     * Get language flag emoji (for UI display)
     * @param languageCode Language code
     * @return Flag emoji string
     */
    fun getLanguageFlag(languageCode: String): String {
        return when (languageCode.lowercase()) {
            "en" -> "🇺🇸"
            "hi" -> "🇮🇳"
            "ar" -> "🇸🇦"
            "es" -> "🇪🇸"
            "fr" -> "🇫🇷"
            "de" -> "🇩🇪"
            "it" -> "🇮🇹"
            "pt" -> "🇵🇹"
            "ru" -> "🇷🇺"
            "ja" -> "🇯🇵"
            "ko" -> "🇰🇷"
            "zh" -> "🇨🇳"
            "bn" -> "🇧🇩"
            "te" -> "🇮🇳"
            "ta" -> "🇮🇳"
            "mr" -> "🇮🇳"
            "gu" -> "🇮🇳"
            "kn" -> "🇮🇳"
            "ml" -> "🇮🇳"
            "pa" -> "🇮🇳"
            else -> "��"
        }
    }
} 