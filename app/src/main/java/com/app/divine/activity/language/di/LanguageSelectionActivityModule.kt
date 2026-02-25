package com.app.divine.activity.language.di

import android.content.Context
import com.app.core.dagger.preference.AppPreferences
import com.app.core.dagger.roomdatabase.AppDatabase
import com.app.core.extensions.ui.getViewModel
import com.app.divine.activity.language.view.LanguageSelectionActivity
import com.app.divine.activity.language.viewmodel.LanguageSelectionViewModel
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit

@Module
class LanguageSelectionActivityModule(
    val activity: LanguageSelectionActivity
) {
    @LanguageSelectionActivityScope
    @Provides
    fun getSaveData(): String {
        return ""
    }
    
    @LanguageSelectionActivityScope
    @Provides
    fun provideViewModel(appDatabase: AppDatabase,
                         retrofit: Retrofit,
                         okHttpClient: OkHttpClient,
                         appPreferences: AppPreferences,
                         context: Context
    ): LanguageSelectionViewModel {
        return activity.getViewModel { 
            LanguageSelectionViewModel(appDatabase, retrofit, okHttpClient, appPreferences, context) 
        }
    }
} 