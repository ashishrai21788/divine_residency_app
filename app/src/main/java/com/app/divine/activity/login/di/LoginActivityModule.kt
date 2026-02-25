package com.app.divine.activity.login.di

import android.content.Context
import com.app.core.dagger.preference.AppPreferences
import com.app.core.dagger.roomdatabase.AppDatabase
import com.app.core.extensions.ui.getViewModel
import com.app.divine.activity.login.view.LoginActivity
import com.app.divine.activity.login.viewmodel.LoginViewModel
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit

@Module
class LoginActivityModule(
    val activity: LoginActivity
) {
    @LoginActivityScope
    @Provides
    fun getSaveData(): String {
        return ""
    }
    @LoginActivityScope
    @Provides
    fun provideViewModel(appDatabase: AppDatabase,
                         retrofit: Retrofit,
                         okHttpClient: OkHttpClient,
                         appPreferences: AppPreferences,
                         context: Context
    ): LoginViewModel{
        return activity.getViewModel { LoginViewModel(appDatabase, retrofit, okHttpClient, appPreferences, context) }
    }

}
