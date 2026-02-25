package com.app.core.dagger.module

import android.content.Context
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import com.app.core.dagger.APIServices
import com.app.core.utils.CoreConnectionLiveData
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit.GsonConverterFactory
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module(includes = [ContextModule::class])
class NetworkModule() {
    private var CONNECT_TIMEOUT: Long = 3 * 60L
    private var READ_TIMEOUT: Long = 3 * 60L
    private var WRITE_TIMEOUT: Long = 3 * 60L

    @Singleton
    @Provides
    fun retrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.github.com/")
            .addConverterFactory(
                retrofit2.converter.gson.GsonConverterFactory.create(
                    GsonBuilder()
                        .setLenient()
                        .create()
                )
            )
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(okHttpClient)
            .build()
    }

    @Singleton
    @Provides
    fun okHttpClient(stethoInterceptor: StethoInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addNetworkInterceptor(stethoInterceptor)
            .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
            .retryOnConnectionFailure(false)
            .cache(null)
            .build()
    }

    @Singleton
    @Provides
    fun stethoInterceptor(): StethoInterceptor {
        return StethoInterceptor()
    }

    @Singleton
    @Provides
    fun provideRetrofitAPIServices(retrofit: Retrofit): APIServices {
        return retrofit.create(APIServices::class.java)
    }

    @Singleton
    @Provides
    fun provideVolleyClient(context: Context): RequestQueue {
        return Volley.newRequestQueue(context)
    }
    @Singleton
    @Provides
    fun provideNetworkObserver(context: Context): CoreConnectionLiveData {
        return CoreConnectionLiveData(context)
    }

}