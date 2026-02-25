package com.app.divine.activity.splash.di

import android.content.Context
import com.app.core.dagger.preference.AppPreferences
import com.app.core.dagger.roomdatabase.AppDatabase
import com.app.core.extensions.ui.getViewModel
import com.app.divine.activity.splash.view.SplashActivity
import com.app.divine.activity.splash.viewmodel.SplashViewModel
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit

@Module
class SplashActivityModule(
    val activity: SplashActivity
) {
    @SplashActivityScope
    @Provides
    fun getSaveData(): String {
        return ""
    }
    @SplashActivityScope
    @Provides
    fun provideViewModel(appDatabase: AppDatabase,
                         retrofit: Retrofit,
                         okHttpClient: OkHttpClient,
                         appPreferences: AppPreferences,
                         context: Context
    ): SplashViewModel {
        return activity.getViewModel { SplashViewModel(appDatabase, retrofit, okHttpClient, appPreferences,context) }
    }

}
