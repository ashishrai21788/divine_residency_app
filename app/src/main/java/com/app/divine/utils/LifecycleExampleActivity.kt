package com.app.divine.utils

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

/**
 * LifecycleExampleActivity - यह एक example activity है जो दिखाता है कि कैसे lifecycle logging system का use करना है
 */
class LifecycleExampleActivity : AppCompatActivity() {
    
    companion object {
        private const val TAG = "LifecycleExample"
    }
    
    private lateinit var statusTextView: TextView
    private lateinit var logButton: Button
    private lateinit var detailedLogButton: Button
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Manual logging example
        LifecycleLogHelper.logActivityEvent(javaClass.simpleName, "onCreate", "Bundle: ${savedInstanceState != null}")
        
        // Set up UI
        setupUI()
    }
    
    override fun onStart() {
        super.onStart()
        LifecycleLogHelper.logActivityEvent(javaClass.simpleName, "onStart")
    }
    
    override fun onResume() {
        super.onResume()
        LifecycleLogHelper.logActivityEvent(javaClass.simpleName, "onResume")
        
        // Update UI with current state
        updateStatusText()
    }
    
    override fun onPause() {
        super.onPause()
        LifecycleLogHelper.logActivityEvent(javaClass.simpleName, "onPause")
    }
    
    override fun onStop() {
        super.onStop()
        LifecycleLogHelper.logActivityEvent(javaClass.simpleName, "onStop")
    }
    
    override fun onDestroy() {
        super.onDestroy()
        LifecycleLogHelper.logActivityEvent(javaClass.simpleName, "onDestroy")
    }
    
    private fun setupUI() {
        // Create a simple layout programmatically
        val layout = android.widget.LinearLayout(this).apply {
            orientation = android.widget.LinearLayout.VERTICAL
            setPadding(32, 32, 32, 32)
        }
        
        statusTextView = TextView(this).apply {
            text = "Lifecycle Status will appear here..."
            setPadding(0, 16, 0, 16)
        }
        
        logButton = Button(this).apply {
            text = "Print Current State"
            setOnClickListener {
                Log.i(TAG, "=== MANUAL STATE REQUEST ===")
                LifecycleLogHelper.printCurrentState()
                updateStatusText()
            }
        }
        
        detailedLogButton = Button(this).apply {
            text = "Print Detailed State"
            setOnClickListener {
                LifecycleLogHelper.printDetailedState()
            }
        }
        
        val summaryButton = Button(this).apply {
            text = "Print Summary"
            setOnClickListener {
                LifecycleLogHelper.printSummary()
            }
        }
        
        val customEventButton = Button(this).apply {
            text = "Log Custom Event"
            setOnClickListener {
                LifecycleLogHelper.logCustomEvent("ExampleActivity", "ButtonClicked", "User clicked custom event button")
            }
        }
        
        val checkActivityButton = Button(this).apply {
            text = "Check Activity Status"
            setOnClickListener {
                val isActive = LifecycleLogHelper.isActivityActive(javaClass.simpleName)
                Log.i(TAG, "Activity ${javaClass.simpleName} is active: $isActive")
            }
        }
        
        layout.addView(statusTextView)
        layout.addView(logButton)
        layout.addView(detailedLogButton)
        layout.addView(summaryButton)
        layout.addView(customEventButton)
        layout.addView(checkActivityButton)
        
        setContentView(layout)
    }
    
    private fun updateStatusText() {
        val currentActivity = LifecycleLogHelper.getCurrentActiveActivity()
        val currentFragment = LifecycleLogHelper.getCurrentActiveFragment()
        val activities = LifecycleLogHelper.getAllActiveActivities()
        val fragments = LifecycleLogHelper.getAllActiveFragments()
        
        val statusText = buildString {
            appendLine("📱 Current Active Activity: $currentActivity")
            appendLine("📄 Current Active Fragment: $currentFragment")
            appendLine()
            appendLine("📊 Active Activities: ${activities.size}")
            activities.forEach { (name, info) ->
                val status = if (info.isActive) "✅" else "⏸️"
                appendLine("  $status $name: ${info.currentState}")
            }
            appendLine()
            appendLine("📊 Active Fragments: ${fragments.size}")
            fragments.forEach { (name, info) ->
                val status = if (info.isActive) "✅" else "⏸️"
                appendLine("  $status $name (in ${info.activityName}): ${info.currentState}")
            }
            appendLine()
            appendLine("🔄 Last updated: ${java.text.SimpleDateFormat("HH:mm:ss", java.util.Locale.getDefault()).format(java.util.Date())}")
        }
        
        statusTextView.text = statusText
    }
} 