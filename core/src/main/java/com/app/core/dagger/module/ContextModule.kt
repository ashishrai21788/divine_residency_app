package com.app.core.dagger.module

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides

@Module
class ContextModule(private val application: Application) {

    @Provides
    fun context(): Context = application.applicationContext
}