package com.app.divine.activity.splash.view

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.app.divine.R
import com.app.core.dagger.preference.AppPreferences
import com.app.core.dagger.roomdatabase.AppDatabase
import com.app.core.extensions.showToastS
import com.app.core.utils.CoreConnectionLiveData
import com.app.divine.AppApplication
import com.app.divine.activity.splash.di.DaggerSplashActivityComponent
import com.app.divine.activity.splash.di.SplashActivityModule
import com.app.divine.utils.LanguageFlowManager
import com.notification.FirebaseNotificationManager
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import javax.inject.Inject

/** Action used by firebaseSDK when notification is clicked (must match SDK intent action). */
private const val ACTION_NOTIFICATION_CLICK = "come.notification.ACTION_NOTIFICATION_CLICK"

class SplashActivity : AppCompatActivity() {

    val TAG = SplashActivity::class.java.simpleName

    @Inject
    lateinit var appDatabase: AppDatabase

    @Inject
    @field:com.app.core.dagger.qualifier.DefaultRetrofit
    lateinit var retrofit: Retrofit

    @Inject
    lateinit var okHttpClient: OkHttpClient

    @Inject
    lateinit var appPreferences: AppPreferences

    @Inject
    lateinit var coreConnectionLiveData: CoreConnectionLiveData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (intent?.action == ACTION_NOTIFICATION_CLICK) {
            FirebaseNotificationManager.getInstance().handleIntent(this, intent!!)
            finish()
            return
        }
        setContentView(R.layout.activity_splash)
        DaggerSplashActivityComponent.builder()
            .coreComponent((application as AppApplication).coreComponent)
            .splashActivityModule(SplashActivityModule(this))
            .build()
            .inject(this)

        coreConnectionLiveData.observe(this, Observer {
            if (it) {
                showToastS("active")
            } else {
                showToastS("inactive")
            }
        })

        checkLanguageFlow()
    }

    private fun checkLanguageFlow() {
        android.os.Handler(android.os.Looper.getMainLooper()).postDelayed({
            val nextActivity = LanguageFlowManager.getNextActivityAfterSplash(this)
            val intent = Intent(this, nextActivity)
            startActivity(intent)
            finish()
        }, 2000)
    }
}
