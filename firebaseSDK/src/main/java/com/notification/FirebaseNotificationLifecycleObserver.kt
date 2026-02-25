package com.notification

import android.app.Activity
import android.app.Application
import android.os.Bundle
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ProcessLifecycleOwner

/**
 * Lifecycle observer for handling app state changes related to Firebase notifications
 */
class FirebaseNotificationLifecycleObserver : Application.ActivityLifecycleCallbacks, LifecycleObserver {

    private var activityReferences = 0
    private var isActivityChangingConfigurations = false

    companion object {
        @Volatile
        private var instance: FirebaseNotificationLifecycleObserver? = null

        fun getInstance(): FirebaseNotificationLifecycleObserver {
            return instance ?: synchronized(this) {
                instance ?: FirebaseNotificationLifecycleObserver().also { instance = it }
            }
        }
    }

    fun register(application: Application) {
        application.registerActivityLifecycleCallbacks(this)
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
    }

    fun unregister(application: Application) {
        application.unregisterActivityLifecycleCallbacks(this)
        ProcessLifecycleOwner.get().lifecycle.removeObserver(this)
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {}

    override fun onActivityStarted(activity: Activity) {
        if (++activityReferences == 1 && !isActivityChangingConfigurations) {
            // App is in foreground
            FirebaseNotificationManager.getInstance().updateAppState(true)
        }
    }

    override fun onActivityResumed(activity: Activity) {}

    override fun onActivityPaused(activity: Activity) {}

    override fun onActivityStopped(activity: Activity) {
        isActivityChangingConfigurations = activity.isChangingConfigurations
        if (--activityReferences == 0 && !isActivityChangingConfigurations) {
            // App is in background
            FirebaseNotificationManager.getInstance().updateAppState(false)
        }
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}

    override fun onActivityDestroyed(activity: Activity) {}

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onAppBackgrounded() {
        FirebaseNotificationManager.getInstance().updateAppState(false)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onAppForegrounded() {
        FirebaseNotificationManager.getInstance().updateAppState(true)
    }
} 