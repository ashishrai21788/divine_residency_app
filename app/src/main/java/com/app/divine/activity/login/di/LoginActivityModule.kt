package com.app.divine.activity.login.di

import android.content.Context
import com.app.core.dagger.preference.AppPreferences
import com.app.core.dagger.qualifier.DefaultRetrofit
import com.app.core.dagger.qualifier.VillaSociety
import com.app.core.dagger.roomdatabase.AppDatabase
import com.app.core.extensions.ui.getViewModel
import com.app.divine.activity.login.view.LoginActivity
import com.app.divine.activity.login.viewmodel.LoginViewModel
import com.app.divine.api.villa.VillaAuthApi
import com.app.divine.repository.VillaAuthRepository
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
    fun provideVillaAuthApi(@VillaSociety retrofit: Retrofit): VillaAuthApi =
        retrofit.create(VillaAuthApi::class.java)

    @LoginActivityScope
    @Provides
    fun provideVillaAuthRepository(api: VillaAuthApi, appPreferences: AppPreferences): VillaAuthRepository =
        VillaAuthRepository(api, appPreferences)

    @LoginActivityScope
    @Provides
    fun provideViewModel(
        appDatabase: AppDatabase,
        @DefaultRetrofit retrofit: Retrofit,
        okHttpClient: OkHttpClient,
        appPreferences: AppPreferences,
        context: Context,
        villaAuthRepository: VillaAuthRepository
    ): LoginViewModel {
        return activity.getViewModel {
            LoginViewModel(appDatabase, retrofit, okHttpClient, appPreferences, context, villaAuthRepository)
        }
    }
}
