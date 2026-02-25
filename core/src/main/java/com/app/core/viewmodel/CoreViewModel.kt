package com.app.core.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import com.app.core.dagger.coredao.UserDao
import com.app.core.dagger.preference.AppPreferences
import com.app.core.dagger.roomdatabase.AppDatabase
import okhttp3.OkHttpClient
import retrofit2.Retrofit

open class CoreViewModel(
    val appDatabase: AppDatabase,
    val retrofit: Retrofit,
    val okHttpClient: OkHttpClient,
    val appPreferences: AppPreferences,
    val  context: Context
    ) : ViewModel() {
}