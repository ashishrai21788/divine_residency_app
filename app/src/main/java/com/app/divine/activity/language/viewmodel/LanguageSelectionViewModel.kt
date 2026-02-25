package com.app.divine.activity.language.viewmodel

import android.app.Activity
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.app.core.dagger.preference.AppPreferences
import com.app.core.dagger.roomdatabase.AppDatabase
import com.app.core.viewmodel.CoreViewModel
import com.app.divine.utils.LanguageHelper
import okhttp3.OkHttpClient
import retrofit2.Retrofit

class LanguageSelectionViewModel(
    appDatabase: AppDatabase,
    retrofit: Retrofit,
    okHttpClient: OkHttpClient,
    appPreferences: AppPreferences,
    context: Context
) : CoreViewModel(appDatabase, retrofit, okHttpClient, appPreferences, context) {
    
    private val _availableLanguages = MutableLiveData<List<LanguageHelper.Language>>()
    val availableLanguages: LiveData<List<LanguageHelper.Language>> = _availableLanguages
    
    private val _currentLanguage = MutableLiveData<LanguageHelper.Language>()
    val currentLanguage: LiveData<LanguageHelper.Language> = _currentLanguage
    
    private val _languageChanged = MutableLiveData<Boolean>()
    val languageChanged: LiveData<Boolean> = _languageChanged
    
    init {
        loadAvailableLanguages()
        loadCurrentLanguage()
    }
    
    private fun loadAvailableLanguages() {
        val languages = LanguageHelper.getAvailableLanguages(context)
        _availableLanguages.postValue(languages)
    }
    
    private fun loadCurrentLanguage() {
        val currentCode = com.app.divine.utils.LanguageManager.getCurrentLanguage()
        val currentLanguage = LanguageHelper.getAvailableLanguages(context)
            .find { it.code == currentCode }
        _currentLanguage.postValue(currentLanguage)
    }
    
    fun changeLanguage(languageCode: String) {
        try {
            // Check if context is still valid
            if (context is Activity) {
                val activity = context as Activity
                if (activity.isFinishing || activity.isDestroyed) {
                    android.util.Log.d("LanguageSelectionViewModel", "Activity is finishing or destroyed, skipping language change")
                    return
                }
            }
            
            com.app.divine.utils.LanguageManager.setLanguage(context, languageCode)
            _languageChanged.postValue(true)
            loadCurrentLanguage()
            
            // Update available languages list to reflect the new selection
            loadAvailableLanguages()
        } catch (e: Exception) {
            android.util.Log.e("LanguageSelectionViewModel", "Error changing language: ${e.message}")
            _languageChanged.postValue(false)
        }
    }
    
    fun getCurrentLanguageCode(): String {
        return com.app.divine.utils.LanguageManager.getCurrentLanguage()
    }
    
    fun isCurrentLanguageRTL(): Boolean {
        return LanguageHelper.isCurrentLanguageRTL()
    }
} 