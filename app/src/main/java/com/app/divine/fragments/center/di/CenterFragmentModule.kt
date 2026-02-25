package com.app.divine.fragments.center.di

import android.content.Context
import com.app.core.dagger.preference.AppPreferences
import com.app.core.dagger.roomdatabase.AppDatabase
import com.app.core.extensions.ui.getViewModel
import com.app.divine.fragments.center.view.CenterFragment
import dagger.Module
import dagger.Provides
import com.app.divine.fragments.home.viewmodel.HomeViewModel
import okhttp3.OkHttpClient
import retrofit2.Retrofit

@Module
class CenterFragmentModule(private val fragment: CenterFragment) {
    @Provides
    fun getSaveData(): String {
        return ""
    }
    @CenterFragmentScope
    @Provides
    fun provideViewModel(appDatabase: AppDatabase,
                         retrofit: Retrofit,
                         okHttpClient: OkHttpClient,
                         appPreferences: AppPreferences,
                         context: Context
    ): HomeViewModel {
        return fragment.getViewModel {
            HomeViewModel(
                appDatabase,
                retrofit,
                okHttpClient,
                appPreferences,
                context
            )
        }
    }
} 