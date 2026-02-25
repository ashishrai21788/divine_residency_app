package com.app.divine.utils

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import java.util.concurrent.ConcurrentHashMap

/**
 * SimpleLifecycleLogger - एक simple logging system जो सभी Activity और Fragment के lifecycle को track करता है
 */
class SimpleLifecycleLogger private constructor() {
    
    companion object {
        private const val TAG = "SimpleLifecycleLogger"
        private const val ACTIVITY_TAG = "ActivityLifecycle"
        private const val FRAGMENT_TAG = "FragmentLifecycle"
        private const val APP_TAG = "AppLifecycle"
        
        @Volatile
        private var INSTANCE: SimpleLifecycleLogger? = null
        
        fun getInstance(): SimpleLifecycleLogger {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: SimpleLifecycleLogger().also { INSTANCE = it }
            }
        }
    }
    
    private val activeActivities = ConcurrentHashMap<String, ActivityInfo>()
    private val activeFragments = ConcurrentHashMap<String, FragmentInfo>()
    private var lastActiveActivity: String? = null
    private var lastActiveFragment: String? = null
    private var isInitialized = false
    
    data class ActivityInfo(
        val activity: Activity,
        val startTime: Long = System.currentTimeMillis(),
        var currentState: String = "UNKNOWN",
        var isActive: Boolean = false
    )
    
    data class FragmentInfo(
        val fragment: Fragment,
        val activityName: String,
        val startTime: Long = System.currentTimeMillis(),
        var currentState: String = "UNKNOWN",
        var isActive: Boolean = false
    )
    
    fun initialize(application: Application) {
        if (isInitialized) return
        
        Log.i(TAG, "🚀 Initializing SimpleLifecycleLogger...")
        
        // Register activity lifecycle callbacks
        application.registerActivityLifecycleCallbacks(object : Application.ActivityLifecycleCallbacks {
            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                val activityName = activity.javaClass.simpleName
                activeActivities[activityName] = ActivityInfo(activity, currentState = "CREATED")
                logActivityEvent(activityName, "onCreate", savedInstanceState?.toString() ?: "null")
                
                // Register fragment lifecycle for FragmentActivity
                if (activity is FragmentActivity) {
                    registerFragmentLifecycle(activity)
                }
            }
            
            override fun onActivityStarted(activity: Activity) {
                val activityName = activity.javaClass.simpleName
                activeActivities[activityName]?.currentState = "STARTED"
                logActivityEvent(activityName, "onStart")
            }
            
            override fun onActivityResumed(activity: Activity) {
                val activityName = activity.javaClass.simpleName
                activeActivities[activityName]?.apply {
                    currentState = "RESUMED"
                    isActive = true
                }
                
                // Track last active activity
                if (lastActiveActivity != activityName) {
                    if (lastActiveActivity != null) {
                        Log.i(ACTIVITY_TAG, "🔄 Previous Active Activity: $lastActiveActivity")
                    }
                    lastActiveActivity = activityName
                }
                
                logActivityEvent(activityName, "onResume")
                printCurrentState()
            }
            
            override fun onActivityPaused(activity: Activity) {
                val activityName = activity.javaClass.simpleName
                activeActivities[activityName]?.apply {
                    currentState = "PAUSED"
                    isActive = false
                }
                logActivityEvent(activityName, "onPause")
            }
            
            override fun onActivityStopped(activity: Activity) {
                val activityName = activity.javaClass.simpleName
                activeActivities[activityName]?.currentState = "STOPPED"
                logActivityEvent(activityName, "onStop")
            }
            
            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
                val activityName = activity.javaClass.simpleName
                logActivityEvent(activityName, "onSaveInstanceState")
            }
            
            override fun onActivityDestroyed(activity: Activity) {
                val activityName = activity.javaClass.simpleName
                activeActivities[activityName]?.currentState = "DESTROYED"
                activeActivities.remove(activityName)
                logActivityEvent(activityName, "onDestroy")
                
                // Update last active activity if this was the last one
                if (lastActiveActivity == activityName) {
                    lastActiveActivity = null
                }
            }
        })
        
        // Register app lifecycle observer
        ProcessLifecycleOwner.get().lifecycle.addObserver(object : LifecycleEventObserver {
            override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                when (event) {
                    Lifecycle.Event.ON_CREATE -> logAppEvent("onCreate")
                    Lifecycle.Event.ON_START -> logAppEvent("onStart")
                    Lifecycle.Event.ON_RESUME -> logAppEvent("onResume")
                    Lifecycle.Event.ON_PAUSE -> logAppEvent("onPause")
                    Lifecycle.Event.ON_STOP -> logAppEvent("onStop")
                    Lifecycle.Event.ON_DESTROY -> logAppEvent("onDestroy")
                    else -> {}
                }
            }
        })
        
        isInitialized = true
        Log.i(TAG, "✅ SimpleLifecycleLogger initialized successfully")
    }
    
    private fun registerFragmentLifecycle(activity: FragmentActivity) {
        activity.supportFragmentManager.registerFragmentLifecycleCallbacks(
            object : FragmentManager.FragmentLifecycleCallbacks() {
                override fun onFragmentPreAttached(fm: FragmentManager, f: Fragment, context: android.content.Context) {
                    val fragmentName = f.javaClass.simpleName
                    val activityName = activity.javaClass.simpleName
                    activeFragments[fragmentName] = FragmentInfo(f, activityName, currentState = "PRE_ATTACHED")
                    logFragmentEvent(fragmentName, activityName, "onFragmentPreAttached")
                }
                
                override fun onFragmentAttached(fm: FragmentManager, f: Fragment, context: android.content.Context) {
                    val fragmentName = f.javaClass.simpleName
                    val activityName = activity.javaClass.simpleName
                    activeFragments[fragmentName]?.currentState = "ATTACHED"
                    logFragmentEvent(fragmentName, activityName, "onFragmentAttached")
                }
                
                override fun onFragmentPreCreated(fm: FragmentManager, f: Fragment, savedInstanceState: Bundle?) {
                    val fragmentName = f.javaClass.simpleName
                    val activityName = activity.javaClass.simpleName
                    logFragmentEvent(fragmentName, activityName, "onFragmentPreCreated")
                }
                
                override fun onFragmentCreated(fm: FragmentManager, f: Fragment, savedInstanceState: Bundle?) {
                    val fragmentName = f.javaClass.simpleName
                    val activityName = activity.javaClass.simpleName
                    activeFragments[fragmentName]?.currentState = "CREATED"
                    logFragmentEvent(fragmentName, activityName, "onFragmentCreated")
                }
                
                override fun onFragmentViewCreated(fm: FragmentManager, f: Fragment, v: android.view.View, savedInstanceState: Bundle?) {
                    val fragmentName = f.javaClass.simpleName
                    val activityName = activity.javaClass.simpleName
                    logFragmentEvent(fragmentName, activityName, "onFragmentViewCreated")
                }
                
                override fun onFragmentViewDestroyed(fm: FragmentManager, f: Fragment) {
                    val fragmentName = f.javaClass.simpleName
                    val activityName = activity.javaClass.simpleName
                    logFragmentEvent(fragmentName, activityName, "onFragmentViewDestroyed")
                }
                
                override fun onFragmentStarted(fm: FragmentManager, f: Fragment) {
                    val fragmentName = f.javaClass.simpleName
                    val activityName = activity.javaClass.simpleName
                    activeFragments[fragmentName]?.currentState = "STARTED"
                    logFragmentEvent(fragmentName, activityName, "onFragmentStarted")
                }
                
                override fun onFragmentResumed(fm: FragmentManager, f: Fragment) {
                    val fragmentName = f.javaClass.simpleName
                    val activityName = activity.javaClass.simpleName
                    activeFragments[fragmentName]?.apply {
                        currentState = "RESUMED"
                        isActive = true
                    }
                    
                    // Track last active fragment
                    if (lastActiveFragment != fragmentName) {
                        if (lastActiveFragment != null) {
                            Log.i(FRAGMENT_TAG, "🔄 Previous Active Fragment: $lastActiveFragment")
                        }
                        lastActiveFragment = fragmentName
                    }
                    
                    logFragmentEvent(fragmentName, activityName, "onFragmentResumed")
                }
                
                override fun onFragmentPaused(fm: FragmentManager, f: Fragment) {
                    val fragmentName = f.javaClass.simpleName
                    val activityName = activity.javaClass.simpleName
                    activeFragments[fragmentName]?.apply {
                        currentState = "PAUSED"
                        isActive = false
                    }
                    logFragmentEvent(fragmentName, activityName, "onFragmentPaused")
                }
                
                override fun onFragmentStopped(fm: FragmentManager, f: Fragment) {
                    val fragmentName = f.javaClass.simpleName
                    val activityName = activity.javaClass.simpleName
                    activeFragments[fragmentName]?.currentState = "STOPPED"
                    logFragmentEvent(fragmentName, activityName, "onFragmentStopped")
                }
                
                override fun onFragmentDestroyed(fm: FragmentManager, f: Fragment) {
                    val fragmentName = f.javaClass.simpleName
                    val activityName = activity.javaClass.simpleName
                    activeFragments[fragmentName]?.currentState = "DESTROYED"
                    logFragmentEvent(fragmentName, activityName, "onFragmentDestroyed")
                }
                
                override fun onFragmentDetached(fm: FragmentManager, f: Fragment) {
                    val fragmentName = f.javaClass.simpleName
                    val activityName = activity.javaClass.simpleName
                    activeFragments[fragmentName]?.currentState = "DETACHED"
                    activeFragments.remove(fragmentName)
                    logFragmentEvent(fragmentName, activityName, "onFragmentDetached")
                    
                    // Update last active fragment if this was the last one
                    if (lastActiveFragment == fragmentName) {
                        lastActiveFragment = null
                    }
                }
            }, true
        )
    }
    
    private fun logActivityEvent(activityName: String, event: String, extra: String = "") {
        val message = if (extra.isNotEmpty()) {
            "🟢 Activity: $activityName -> $event ($extra)"
        } else {
            "🟢 Activity: $activityName -> $event"
        }
        Log.d(ACTIVITY_TAG, message)
    }
    
    private fun logFragmentEvent(fragmentName: String, activityName: String, event: String) {
        val message = "🔵 Fragment: $fragmentName (in $activityName) -> $event"
        Log.d(FRAGMENT_TAG, message)
    }
    
    private fun logAppEvent(event: String) {
        val message = "🟡 App -> $event"
        Log.d(APP_TAG, message)
    }
    
    fun printCurrentState() {
        Log.i(TAG, "=== CURRENT LIFECYCLE STATE ===")
        
        // Current Active Activity
        Log.i(TAG, "📱 Current Active Activity: $lastActiveActivity")
        
        // Current Active Fragment
        Log.i(TAG, "📄 Current Active Fragment: $lastActiveFragment")
        
        // All Active Activities
        Log.i(TAG, "📱 All Active Activities (${activeActivities.size}):")
        activeActivities.forEach { (name, info) ->
            val duration = System.currentTimeMillis() - info.startTime
            Log.i(TAG, "  • $name: ${info.currentState} (${duration}ms) ${if (info.isActive) "✅" else "⏸️"}")
        }
        
        // All Active Fragments
        Log.i(TAG, "📄 All Active Fragments (${activeFragments.size}):")
        activeFragments.forEach { (name, info) ->
            val duration = System.currentTimeMillis() - info.startTime
            Log.i(TAG, "  • $name (in ${info.activityName}): ${info.currentState} (${duration}ms) ${if (info.isActive) "✅" else "⏸️"}")
        }
        
        Log.i(TAG, "=== END LIFECYCLE STATE ===")
    }
    
    fun getCurrentActiveActivity(): String? = lastActiveActivity
    
    fun getCurrentActiveFragment(): String? = lastActiveFragment
    
    fun getAllActiveActivities(): Map<String, ActivityInfo> = activeActivities.toMap()
    
    fun getAllActiveFragments(): Map<String, FragmentInfo> = activeFragments.toMap()
} 