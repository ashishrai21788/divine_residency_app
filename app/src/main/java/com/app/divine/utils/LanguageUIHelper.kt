package com.app.divine.utils

import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.databinding.ObservableField

/**
 * Helper class for common UI language updates
 */
object LanguageUIHelper {
    
    /**
     * Update TextView with language string
     */
    fun updateTextView(textView: TextView, languageKey: String) {
        textView.text = LanguageManager.getString(languageKey)
    }
    
    /**
     * Update TextView with language string and default value
     */
    fun updateTextView(textView: TextView, languageKey: String, defaultValue: String) {
        textView.text = LanguageManager.getString(languageKey, defaultValue)
    }
    
    /**
     * Update Toolbar title with language string
     */
    fun updateToolbarTitle(toolbar: Toolbar, languageKey: String) {
        toolbar.title = LanguageManager.getString(languageKey)
    }
    
    /**
     * Update Toolbar title with language string and default value
     */
    fun updateToolbarTitle(toolbar: Toolbar, languageKey: String, defaultValue: String) {
        toolbar.title = LanguageManager.getString(languageKey, defaultValue)
    }
    
    /**
     * Update ObservableField with language string
     */
    fun updateObservableField(field: ObservableField<String>, languageKey: String) {
        field.set(LanguageManager.getString(languageKey))
    }
    
    /**
     * Update ObservableField with language string and default value
     */
    fun updateObservableField(field: ObservableField<String>, languageKey: String, defaultValue: String) {
        field.set(LanguageManager.getString(languageKey, defaultValue))
    }
    
    /**
     * Update multiple TextViews at once
     */
    fun updateTextViews(vararg pairs: Pair<TextView, String>) {
        pairs.forEach { (textView, languageKey) ->
            textView.text = LanguageManager.getString(languageKey)
        }
    }
    
    /**
     * Update multiple Toolbar titles at once
     */
    fun updateToolbarTitles(vararg pairs: Pair<Toolbar, String>) {
        pairs.forEach { (toolbar, languageKey) ->
            toolbar.title = LanguageManager.getString(languageKey)
        }
    }
    
    /**
     * Update multiple ObservableFields at once
     */
    fun updateObservableFields(vararg pairs: Pair<ObservableField<String>, String>) {
        pairs.forEach { (field, languageKey) ->
            field.set(LanguageManager.getString(languageKey))
        }
    }
} 