package com.app.divine.fragments.profile.di

import android.content.Context
import com.app.core.dagger.preference.AppPreferences
import com.app.core.dagger.roomdatabase.AppDatabase
import com.app.core.extensions.ui.getViewModel
import com.app.core.dagger.qualifier.DefaultRetrofit
import dagger.Module
import dagger.Provides
import com.app.divine.fragments.home.viewmodel.HomeViewModel
import com.app.divine.fragments.profile.view.ProfileFragment
import okhttp3.OkHttpClient
import retrofit2.Retrofit

@Module
class ProfileFragmentModule(val fragment: ProfileFragment) {
    @Provides
    fun getSaveData(): String {
        return ""
    }
    @ProfileFragmentScope
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