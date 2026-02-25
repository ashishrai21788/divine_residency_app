package com.app.divine.activity.landing.di

import android.content.Context
import com.app.core.dagger.preference.AppPreferences
import com.app.core.dagger.roomdatabase.AppDatabase
import com.app.core.extensions.ui.getViewModel
import com.app.divine.activity.landing.view.LandingMainActivity
import com.app.divine.activity.landing.viewmodel.LandingMainViewModel
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit

@Module
class LandingMainActivityModule(
    val activity: LandingMainActivity
) {

    @Provides
    fun getSaveData(): String {
        return ""
    }
    @LandingMainActivityScope
    @Provides
    fun provideViewModel(appDatabase: AppDatabase,
                         retrofit: Retrofit,
                         okHttpClient: OkHttpClient,
                         appPreferences: AppPreferences,
                         context: Context
    ): LandingMainViewModel {
        return activity.getViewModel {
            LandingMainViewModel(
                appDatabase,
                retrofit,
                okHttpClient,
                appPreferences,
                context
            )
        }
    }
}
