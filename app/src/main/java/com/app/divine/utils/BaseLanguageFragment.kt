package com.app.divine.utils

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment

/**
 * Base fragment that automatically handles language changes
 * Extend this class in fragments that need to support real-time language changes
 */
abstract class BaseLanguageFragment : Fragment(), LanguageChangeListener {
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Register for language change notifications
        LanguageChangeManager.registerListener(this)
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        // Unregister from language change notifications
        LanguageChangeManager.unregisterListener(this)
    }
    
    override fun onLanguageChanged(newLanguageCode: String) {
        // Update the UI when language changes
        updateUIForLanguageChange()
    }
    
    /**
     * Override this method in child fragments to update UI elements
     * This method is called whenever the language changes
     */
    abstract fun updateUIForLanguageChange()
    
    /**
     * Helper method to safely update UI on the main thread
     */
    protected fun runOnUiThread(action: () -> Unit) {
        activity?.runOnUiThread {
            if (!isDetached && !isRemoving) {
                action()
            }
        }
    }
} 