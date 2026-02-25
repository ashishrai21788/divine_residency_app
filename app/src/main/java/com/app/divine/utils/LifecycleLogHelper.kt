package com.app.divine.utils

import android.util.Log

/**
 * LifecycleLogHelper - Manual logging और debugging के लिए helper methods
 */
object LifecycleLogHelper {
    
    private const val TAG = "LifecycleLogHelper"
    
    /**
     * Print current lifecycle state
     */
    fun printCurrentState() {
        SimpleLifecycleLogger.getInstance().printCurrentState()
    }
    
    /**
     * Get current active activity
     */
    fun getCurrentActiveActivity(): String? {
        return SimpleLifecycleLogger.getInstance().getCurrentActiveActivity()
    }
    
    /**
     * Get current active fragment
     */
    fun getCurrentActiveFragment(): String? {
        return SimpleLifecycleLogger.getInstance().getCurrentActiveFragment()
    }
    
    /**
     * Get all active activities
     */
    fun getAllActiveActivities(): Map<String, SimpleLifecycleLogger.ActivityInfo> {
        return SimpleLifecycleLogger.getInstance().getAllActiveActivities()
    }
    
    /**
     * Get all active fragments
     */
    fun getAllActiveFragments(): Map<String, SimpleLifecycleLogger.FragmentInfo> {
        return SimpleLifecycleLogger.getInstance().getAllActiveFragments()
    }
    
    /**
     * Check if specific activity is active
     */
    fun isActivityActive(activityName: String): Boolean {
        val activities = getAllActiveActivities()
        return activities.containsKey(activityName) && activities[activityName]?.isActive == true
    }
    
    /**
     * Check if specific fragment is active
     */
    fun isFragmentActive(fragmentName: String): Boolean {
        val fragments = getAllActiveFragments()
        return fragments.containsKey(fragmentName) && fragments[fragmentName]?.isActive == true
    }
    
    /**
     * Get fragments for specific activity
     */
    fun getFragmentsForActivity(activityName: String): List<String> {
        val fragments = getAllActiveFragments()
        return fragments.filter { it.value.activityName == activityName }.keys.toList()
    }
    
    /**
     * Print detailed state information
     */
    fun printDetailedState() {
        Log.i(TAG, "=== DETAILED LIFECYCLE STATE ===")
        
        val currentActivity = getCurrentActiveActivity()
        val currentFragment = getCurrentActiveFragment()
        val activities = getAllActiveActivities()
        val fragments = getAllActiveFragments()
        
        Log.i(TAG, "🎯 Current Active Components:")
        Log.i(TAG, "  📱 Activity: $currentActivity")
        Log.i(TAG, "  📄 Fragment: $currentFragment")
        
        Log.i(TAG, "📊 All Activities (${activities.size}):")
        activities.forEach { (name, info) ->
            val duration = System.currentTimeMillis() - info.startTime
            val status = if (info.isActive) "✅ ACTIVE" else "⏸️ INACTIVE"
            Log.i(TAG, "  • $name")
            Log.i(TAG, "    State: ${info.currentState}")
            Log.i(TAG, "    Status: $status")
            Log.i(TAG, "    Duration: ${duration}ms")
        }
        
        Log.i(TAG, "📊 All Fragments (${fragments.size}):")
        fragments.forEach { (name, info) ->
            val duration = System.currentTimeMillis() - info.startTime
            val status = if (info.isActive) "✅ ACTIVE" else "⏸️ INACTIVE"
            Log.i(TAG, "  • $name (in ${info.activityName})")
            Log.i(TAG, "    State: ${info.currentState}")
            Log.i(TAG, "    Status: $status")
            Log.i(TAG, "    Duration: ${duration}ms")
        }
        
        Log.i(TAG, "=== END DETAILED STATE ===")
    }
    
    /**
     * Print summary of current state
     */
    fun printSummary() {
        val currentActivity = getCurrentActiveActivity()
        val currentFragment = getCurrentActiveFragment()
        val activities = getAllActiveActivities()
        val fragments = getAllActiveFragments()
        
        Log.i(TAG, "📋 LIFECYCLE SUMMARY:")
        Log.i(TAG, "  📱 Active Activity: $currentActivity")
        Log.i(TAG, "  📄 Active Fragment: $currentFragment")
        Log.i(TAG, "  📊 Total Activities: ${activities.size}")
        Log.i(TAG, "  📊 Total Fragments: ${fragments.size}")
        
        val activeActivities = activities.filter { it.value.isActive }.size
        val activeFragments = fragments.filter { it.value.isActive }.size
        
        Log.i(TAG, "  ✅ Active Activities: $activeActivities")
        Log.i(TAG, "  ✅ Active Fragments: $activeFragments")
    }
    
    /**
     * Manual activity event logging
     */
    fun logActivityEvent(activityName: String, event: String, extra: String = "") {
        val message = if (extra.isNotEmpty()) {
            "🟢 Manual Activity: $activityName -> $event ($extra)"
        } else {
            "🟢 Manual Activity: $activityName -> $event"
        }
        Log.d(TAG, message)
    }
    
    /**
     * Manual fragment event logging
     */
    fun logFragmentEvent(fragmentName: String, activityName: String, event: String) {
        val message = "🔵 Manual Fragment: $fragmentName (in $activityName) -> $event"
        Log.d(TAG, message)
    }
    
    /**
     * Custom event logging
     */
    fun logCustomEvent(tag: String, event: String, data: String = "") {
        val message = if (data.isNotEmpty()) {
            "🟣 Custom: $tag -> $event ($data)"
        } else {
            "🟣 Custom: $tag -> $event"
        }
        Log.d(TAG, message)
    }
} 