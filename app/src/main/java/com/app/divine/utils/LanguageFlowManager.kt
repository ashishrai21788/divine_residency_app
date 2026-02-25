package com.app.divine.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.app.divine.activity.language.view.LanguageSelectionActivity

/**
 * Manager to handle app flow based on language selection
 */
object LanguageFlowManager {
    
    private const val PREF_NAME = "language_flow_preferences"
    private const val KEY_LANGUAGE_SELECTED = "language_selected"
    private const val KEY_FIRST_LAUNCH = "first_launch"
    
    /**
     * Check if language has been selected by user
     */
    fun isLanguageSelected(context: Context): Boolean {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getBoolean(KEY_LANGUAGE_SELECTED, false)
    }
    
    /**
     * Mark language as selected
     */
    fun markLanguageSelected(context: Context) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit()
            .putBoolean(KEY_LANGUAGE_SELECTED, true)
            .putBoolean(KEY_FIRST_LAUNCH, false)
            .apply()
    }
    
    /**
     * Check if this is the first launch of the app
     */
    fun isFirstLaunch(context: Context): Boolean {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getBoolean(KEY_FIRST_LAUNCH, true)
    }
    
    /**
     * Mark first launch as completed
     */
    fun markFirstLaunchCompleted(context: Context) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().putBoolean(KEY_FIRST_LAUNCH, false).apply()
    }
    
    /**
     * Get the appropriate starting activity based on app state
     * Note: SplashActivity is always the launcher, this method is for internal flow decisions
     */
    fun getNextActivityAfterSplash(context: Context): Class<*> {
        return when {
            isFirstLaunch(context) -> LanguageSelectionActivity::class.java
            !isLanguageSelected(context) -> LanguageSelectionActivity::class.java
            else -> getMainActivity(context) // Proceed to main app flow
        }
    }
    
    /**
     * Get the main activity to proceed to after language selection
     * You can customize this method to return different activities based on your app's needs
     */
    private fun getMainActivity(context: Context): Class<*> {
        // Replace this with your main activity (e.g., LoginActivity, MainActivity, etc.)
        // You can also add logic here to check user login status and return appropriate activity
        return com.app.divine.activity.loginSignup.view.LoginSignupActivity::class.java
    }
    
    /**
     * Set the main activity class for the app flow
     * Call this method to customize which activity users will be directed to after language selection
     */
    fun setMainActivity(activityClass: Class<*>) {
        // This method can be used to dynamically set the main activity
        // Implementation can be added if needed
    }

    /**
     * Reset language selection (useful for testing or user preference reset)
     */
    fun resetLanguageSelection(context: Context) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().putBoolean(KEY_LANGUAGE_SELECTED, false).apply()
    }
    
    /**
     * Reset first launch flag (useful for testing)
     */
    fun resetFirstLaunch(context: Context) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().putBoolean(KEY_FIRST_LAUNCH, true).apply()
    }
    
    /**
     * Complete the language selection flow and proceed to main app
     */
    fun completeLanguageSelection(context: Context) {
        try {
            // Check if context is still valid
            if (context is Activity && (context.isFinishing || context.isDestroyed)) {
                android.util.Log.d("LanguageFlowManager", "Context is invalid, skipping flow completion")
                return
            }
            
            markLanguageSelected(context)
            markFirstLaunchCompleted(context)
            
            // Start the main app flow
            val intent = Intent(context, getMainActivity(context))
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        } catch (e: Exception) {
            android.util.Log.e("LanguageFlowManager", "Error completing language selection: ${e.message}")
        }
    }
} 