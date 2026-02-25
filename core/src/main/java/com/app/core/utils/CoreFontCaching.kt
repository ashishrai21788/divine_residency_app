package com.app.core.utils

import android.graphics.Typeface

object CoreFontCaching {
    private const val CACHE_SIZE = 5
    private var fontCache: HashMap<String, Typeface> = hashMapOf()
    private var fontCacheFallBack: HashMap<String, Boolean> = hashMapOf()

    @Synchronized
    fun putFont(key: String, font: Typeface, isFailed: Boolean) {
        if (fontCache.size > CACHE_SIZE) {
            fontCache.clear()
            fontCacheFallBack.clear()
        }
        fontCache[key] = font
        fontCacheFallBack[key] = isFailed
    }

    @Synchronized
    fun getFont(key: String): Typeface? {
        return fontCache[key]
    }

    @Synchronized
    fun getFontFailed(key: String): Boolean {
        return fontCacheFallBack.containsKey(key) && fontCacheFallBack[key] == true
    }

}