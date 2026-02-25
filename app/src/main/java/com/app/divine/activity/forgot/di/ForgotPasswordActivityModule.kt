package com.app.divine.activity.forgot.di

import android.content.Context
import com.app.core.dagger.preference.AppPreferences
import com.app.core.dagger.roomdatabase.AppDatabase
import com.app.core.extensions.ui.getViewModel
import com.app.divine.activity.forgot.view.ForgotPasswordActivity
import com.app.divine.activity.forgot.viewmodel.ForgotPasswordViewModel
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit

@Module
class ForgotPasswordActivityModule(
    val activity: ForgotPasswordActivity
) {

    @Provides
    fun getSaveData(): String {
        return ""
    }
    @ForgotPasswordActivityScope
    @Provides
    fun provideViewModel(appDatabase: AppDatabase,
                         retrofit: Retrofit,
                         okHttpClient: OkHttpClient,
                         appPreferences: AppPreferences,
                         context: Context
    ): ForgotPasswordViewModel {
        return activity.getViewModel { ForgotPasswordViewModel(appDatabase, retrofit, okHttpClient, appPreferences,  context) }
    }
}
