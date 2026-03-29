package com.app.divine

import android.app.Activity
import android.app.Application
import android.app.Application.ActivityLifecycleCallbacks
import android.os.Bundle
import android.util.Log
import androidx.core.view.WindowCompat
import androidx.lifecycle.LiveData
import com.app.core.dagger.component.CoreComponent
import com.app.core.dagger.component.DaggerCoreComponent
import com.app.core.dagger.module.ContextModule
import com.app.core.dagger.module.NetworkModule
import com.app.core.utils.CoreComponentProvider
import com.app.divine.api.villa.VillaAuthApi
import com.app.divine.notification.VillaFcmHandler
import com.app.divine.notification.VillaNotificationChannels
import com.app.divine.realtime.VillaSocketManager
import com.app.divine.repository.VillaAuthRepository
import com.app.divine.session.SessionRepository
import com.app.divine.utils.LanguageManager
import com.app.divine.utils.SimpleLifecycleLogger
import com.google.firebase.messaging.FirebaseMessaging

import com.notification.FirebaseNotificationManager
import com.notification.models.NotificationData


/** Ensures window content is laid out below the status bar (avoids edge-to-edge overlap on API 35+). */
private class SystemBarInsetsCallback : ActivityLifecycleCallbacks {
    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(activity.window, true)
    }

    override fun onActivityStarted(activity: Activity) = Unit
    override fun onActivityResumed(activity: Activity) = Unit
    override fun onActivityPaused(activity: Activity) = Unit
    override fun onActivityStopped(activity: Activity) = Unit
    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) = Unit
    override fun onActivityDestroyed(activity: Activity) = Unit
}

class AppApplication : Application(), CoreComponentProvider {
    lateinit var coreComponent: CoreComponent

    val sessionRepository: SessionRepository by lazy { SessionRepository(coreComponent.appPreferences()) }

    override fun onCreate() {
        super.onCreate()
        registerActivityLifecycleCallbacks(SystemBarInsetsCallback())
        coreComponent =
            DaggerCoreComponent.builder().contextModule(ContextModule(this)).networkModule(
                NetworkModule()
            ).build()

        if (coreComponent.appPreferences().getLogin()) {
            sessionRepository.refreshFromPreferences()
        }

        LanguageManager.initialize(this)
        SimpleLifecycleLogger.getInstance().initialize(this)

        // Villa Society notification channels (Phase 5)
        VillaNotificationChannels.createAll(this)

        FirebaseNotificationManager.getInstance().initialize(applicationContext, object : FirebaseNotificationManager.NotificationCallback {
            override fun onMessageReceived(notificationData: NotificationData) {
                Log.d("FCM_Callback", "onMessageReceived: ${notificationData.title}")
                VillaFcmHandler.onForegroundMessage(this@AppApplication, notificationData)
            }

            override fun onBackgroundMessageReceived(notificationData: NotificationData) {
                Log.d("FCM_Callback", "onBackgroundMessageReceived: ${notificationData.title}")
            }

            override fun onDataMessageReceived(notificationData: NotificationData) {
                Log.d("FCM_Callback", "onDataMessageReceived: ${notificationData.data}")
            }

            override fun onNewToken(token: String) {
                Log.d("FCM_Callback", "onNewToken: $token")
                sendPushTokenToBackend(token)
            }

            override fun onNotificationAction(action: String, data: Map<String, String>) {
                Log.d("FCM_Callback", "onNotificationAction: $action with data $data")
            }

            override fun onNotificationClicked(notificationData: NotificationData) {
                Log.d("FCM_Callback", "onNotificationClicked: ${notificationData.title}")
                VillaFcmHandler.onNotificationClicked(this@AppApplication, coreComponent.appPreferences(), notificationData)
            }

            override fun onTokenReceived(token: String) {
                Log.d("FCM_Callback", "onTokenReceived: $token")
            }

            override fun onError(error: String) {
                Log.e("FCM_Callback", "onError: $error")
            }
        })
    }

    /** Send FCM token to backend when logged in. Call from onNewToken and after login success. */
    private fun sendPushTokenToBackend(token: String) {
        if (!coreComponent.appPreferences().getLogin()) return
        val api = coreComponent.villaSocietyRetrofit().create(VillaAuthApi::class.java)
        VillaAuthRepository(api, coreComponent.appPreferences()).registerFcmToken(token)
    }

    /** Call after login success to register current FCM token with backend. */
    fun sendFcmTokenToBackendIfLoggedIn() {
        FirebaseMessaging.getInstance().token.addOnSuccessListener { token ->
            sendPushTokenToBackend(token)
        }
    }

    private var _villaSocketManager: VillaSocketManager? = null
    /** Realtime socket (Phase 6). Connect when on Resident/Guard screen; disconnect on logout. */
    fun getVillaSocketManager(): VillaSocketManager {
        if (_villaSocketManager == null) {
            _villaSocketManager = VillaSocketManager(coreComponent.appPreferences())
        }
        return _villaSocketManager!!
    }

    override fun provideCoreComponent(): CoreComponent {
       return coreComponent
    }

    override fun forceLoggedOut() {
        getVillaSocketManager().disconnect()
    }

    /** Call from Profile (or any logout UI): clears Villa auth, unregisters this device's FCM only, disconnects socket. Caller should then start LoginActivity and finish. */
    fun performLogout() {
        val api = coreComponent.villaSocietyRetrofit().create(VillaAuthApi::class.java)
        val repo = VillaAuthRepository(api, coreComponent.appPreferences())
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            val token = task.result?.takeIf { task.isSuccessful }
            repo.logout(fcmTokenForThisDevice = token)
        }
        sessionRepository.clear()
        forceLoggedOut()
    }

    override fun logFacebookEvent(name: String?) {
        TODO("Not yet implemented")
    }

    override fun removeChatSupport() {
        TODO("Not yet implemented")
    }

    override fun getChatSupportVisibilityData(): LiveData<Boolean> {
        TODO("Not yet implemented")
    }

    override fun logFirebaseEvent(
        pageIdentifier: String,
        eventName: String,
        eventParams: HashMap<String, String>,
        keepActualKey: Boolean
    ) {
        TODO("Not yet implemented")
    }

    override fun fetchFCMToken(
        firebaseSenderId: String?,
        callBack: ((String) -> Unit)?
    ) {
        TODO("Not yet implemented")
    }
}