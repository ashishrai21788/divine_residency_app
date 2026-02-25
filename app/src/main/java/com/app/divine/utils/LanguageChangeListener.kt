package com.app.divine.utils

/**
 * Interface for activities/fragments that need to be notified when language changes
 */
interface LanguageChangeListener {
    fun onLanguageChanged(newLanguageCode: String)
}

/**
 * Manager to handle language change notifications
 */
object LanguageChangeManager {
    private val listeners = mutableListOf<LanguageChangeListener>()
    
    /**
     * Register a listener for language change events
     */
    fun registerListener(listener: LanguageChangeListener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener)
        }
    }
    
    /**
     * Unregister a listener
     */
    fun unregisterListener(listener: LanguageChangeListener) {
        listeners.remove(listener)
    }
    
    /**
     * Notify all registered listeners about language change
     */
    fun notifyLanguageChanged(newLanguageCode: String) {
        // Check if there are any listeners to notify
        if (listeners.isEmpty()) {
            return
        }
        
        val listenersToRemove = mutableListOf<LanguageChangeListener>()
        
        listeners.forEach { listener ->
            try {
                listener.onLanguageChanged(newLanguageCode)
            } catch (e: Exception) {
                // Mark listener for removal if it causes an exception
                listenersToRemove.add(listener)
                android.util.Log.e("LanguageChangeManager", "Error notifying listener: ${e.message}")
            }
        }
        
        // Remove problematic listeners
        listenersToRemove.forEach { listener ->
            listeners.remove(listener)
        }
    }
    
    /**
     * Clear all listeners (useful for testing or app restart)
     */
    fun clearListeners() {
        listeners.clear()
    }
} 