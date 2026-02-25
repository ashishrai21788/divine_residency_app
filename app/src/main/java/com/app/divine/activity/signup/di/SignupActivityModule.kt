package com.app.divine.activity.signup.di

import android.content.Context
import com.app.core.dagger.preference.AppPreferences
import com.app.core.dagger.qualifier.DefaultRetrofit
import com.app.core.dagger.roomdatabase.AppDatabase
import com.app.core.extensions.ui.getViewModel
import com.app.divine.activity.signup.view.SignupActivity
import com.app.divine.activity.signup.viewmodel.SignupViewModel
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit

@Module
class SignupActivityModule(
    val activity: SignupActivity
) {

    @Provides
    fun getSaveData(): String {
        return ""
    }
    @SignupActivityScope
    @Provides
    fun provideViewModel(appDatabase: AppDatabase,
                         @DefaultRetrofit retrofit: Retrofit,
                         okHttpClient: OkHttpClient,
                         appPreferences: AppPreferences,
                         context: Context
    ): SignupViewModel {
        return activity.getViewModel { SignupViewModel(appDatabase, retrofit, okHttpClient, appPreferences,  context) }
    }
}
