package com.app.divine.fragments.notifications.di

import android.content.Context
import com.app.core.dagger.preference.AppPreferences
import com.app.core.dagger.qualifier.DefaultRetrofit
import com.app.core.dagger.roomdatabase.AppDatabase
import com.app.core.extensions.ui.getViewModel
import com.app.divine.fragments.home.viewmodel.HomeViewModel
import com.app.divine.fragments.notifications.view.NotificationsFragment
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit

@Module
class NotificationsFragmentModule(val fragment: NotificationsFragment) {
    @Provides
    fun getSaveData(): String {
        return ""
    }
    @NotificationsFragmentScope
    @Provides
    fun provideViewModel(appDatabase: AppDatabase,
                         @DefaultRetrofit retrofit: Retrofit,
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