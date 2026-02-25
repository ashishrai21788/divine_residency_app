package com.app.divine.activity.signup.viewmodel

import android.content.Context
import com.app.core.dagger.preference.AppPreferences
import com.app.core.dagger.roomdatabase.AppDatabase
import com.app.core.viewmodel.CoreViewModel
import okhttp3.OkHttpClient
import retrofit2.Retrofit

class SignupViewModel(appDatabase: AppDatabase,
                      retrofit: Retrofit,
                      okHttpClient: OkHttpClient,
                      appPreferences: AppPreferences,
                      context: Context
): CoreViewModel(appDatabase, retrofit, okHttpClient, appPreferences, context) {

}