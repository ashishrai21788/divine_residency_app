package com.app.core.dagger.module

import android.content.Context
import com.app.core.dagger.preference.AppPreferences
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [ContextModule::class])
class AppPreference {

    @Singleton
    @Provides
    fun provideAppPreference(context: Context): AppPreferences{
        return AppPreferences(context)
    }
}