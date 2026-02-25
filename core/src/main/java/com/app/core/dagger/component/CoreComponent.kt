package com.app.core.dagger.component

import android.content.Context
import com.app.core.dagger.APIServices
import com.app.core.dagger.ApplicationScope
import com.app.core.dagger.module.AppPreference
import com.app.core.dagger.module.ContextModule
import com.app.core.dagger.module.DatabaseModule
import com.app.core.dagger.module.NetworkModule
import com.app.core.dagger.preference.AppPreferences
import com.app.core.dagger.qualifier.DefaultRetrofit
import com.app.core.dagger.qualifier.VillaSociety
import com.app.core.dagger.roomdatabase.AppDatabase
import com.app.core.utils.CoreConnectionLiveData
import dagger.Component
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import javax.inject.Singleton

@ApplicationScope
@Singleton
@Component(modules =  [ContextModule::class, NetworkModule::class, DatabaseModule::class, AppPreference::class])
interface CoreComponent {
        fun context(): Context
        @DefaultRetrofit
        fun retrofit() : Retrofit
        fun okHttpClient() : OkHttpClient
        /** Retrofit instance for Villa Society API (base URL http://localhost:3000/api/), with Bearer auth. */
        @VillaSociety
        fun villaSocietyRetrofit(): Retrofit
        fun provideRetrofitAPIServices() : APIServices
        fun appDatabase(): AppDatabase
        fun appPreferences(): AppPreferences
        fun networkConnection(): CoreConnectionLiveData
}