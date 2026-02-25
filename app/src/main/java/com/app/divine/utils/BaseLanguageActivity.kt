package com.app.divine.utils

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

/**
 * Base activity that automatically handles language changes
 * Extend this class in activities that need to support real-time language changes
 */
abstract class BaseLanguageActivity : AppCompatActivity(), LanguageChangeListener {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Register for language change notifications
        LanguageChangeManager.registerListener(this)
    }
    
    override fun onDestroy() {
        super.onDestroy()
        // Unregister from language change notifications
        LanguageChangeManager.unregisterListener(this)
    }
    
    override fun onLanguageChanged(newLanguageCode: String) {
        // Check if activity is still valid before updating UI
        if (isFinishing || isDestroyed) {
            return
        }
        
        // Update the UI when language changes
        try {
            updateUIForLanguageChange()
        } catch (e: Exception) {
            // Log the error but don't crash
            android.util.Log.e("BaseLanguageActivity", "Error updating UI for language change: ${e.message}")
        }
    }
    
    /**
     * Override this method in child activities to update UI elements
     * This method is called whenever the language changes
     */
    abstract fun updateUIForLanguageChange()
    
    /**
     * Helper method to safely update UI on the main thread
     */
    protected fun runOnUiThreadSafely(action: () -> Unit) {
        if (!isFinishing && !isDestroyed) {
            try {
                super.runOnUiThread(action)
            } catch (e: Exception) {
                android.util.Log.e("BaseLanguageActivity", "Error running on UI thread: ${e.message}")
            }
        }
    }
} 