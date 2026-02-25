package com.app.divine.activity.loginSignup.di

import android.content.Context
import com.app.core.dagger.preference.AppPreferences
import com.app.core.dagger.qualifier.DefaultRetrofit
import com.app.core.dagger.roomdatabase.AppDatabase
import com.app.core.extensions.ui.getViewModel
import com.app.divine.activity.loginSignup.view.LoginSignupActivity
import com.app.divine.activity.loginSignup.viewmodel.LoginSignupViewModel
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit

@Module
class LoginSignupActivityModule(
    val activity: LoginSignupActivity
) {
    @LoginSignupActivityScope
    @Provides
    fun getSaveData(): String {
        return ""
    }
    @LoginSignupActivityScope
    @Provides
    fun provideViewModel(appDatabase: AppDatabase,
                         @DefaultRetrofit retrofit: Retrofit,
                         okHttpClient: OkHttpClient,
                         appPreferences: AppPreferences,
                         context: Context
    ): LoginSignupViewModel {
        return activity.getViewModel { LoginSignupViewModel(appDatabase, retrofit, okHttpClient, appPreferences, context) }
    }
} 