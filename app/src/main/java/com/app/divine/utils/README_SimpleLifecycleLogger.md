# Simple Lifecycle Logger

यह एक simple lifecycle logging system है जो सभी Activity और Fragment के lifecycle events को automatically track और log करता है।

## 🚀 Features

✅ **Automatic Logging**: सभी Activity और Fragment lifecycle events automatically logged  
✅ **Current & Previous Tracking**: Current active और previous active components track करता है  
✅ **Real-time State**: Real-time state information  
✅ **No Configuration**: कोई configuration changes नहीं करना पड़ता  
✅ **Simple Integration**: सिर्फ एक line add करना होता है  

## 📁 Files

- **`SimpleLifecycleLogger.kt`** - Main logging system
- **`LifecycleLogHelper.kt`** - Helper methods for manual logging
- **`LifecycleExampleActivity.kt`** - Example implementation

## 🔧 Setup

### Step 1: AppApplication में Initialize करें

```kotlin
// AppApplication.kt में
import com.app.divine.utils.SimpleLifecycleLogger

override fun onCreate() {
    super.onCreate()
    
    // Initialize SimpleLifecycleLogger
    SimpleLifecycleLogger.getInstance().initialize(this)
    
    // ... other initializations
}
```

### Step 2: बस इतना ही! 🎉

कोई और configuration नहीं करना पड़ता। सभी activities और fragments automatically track होंगे।

## 📊 Log Output

### Activity Logs
```
🟢 Activity: MainActivity -> onCreate (Bundle: null)
🟢 Activity: MainActivity -> onStart
🟢 Activity: MainActivity -> onResume
🔄 Previous Active Activity: SplashActivity
📱 Current Active Activity: MainActivity
```

### Fragment Logs
```
🔵 Fragment: HomeFragment (in MainActivity) -> onFragmentAttached
🔵 Fragment: HomeFragment (in MainActivity) -> onFragmentCreated
🔵 Fragment: HomeFragment (in MainActivity) -> onFragmentResumed
🔄 Previous Active Fragment: SplashFragment
📄 Current Active Fragment: HomeFragment
```

### App Lifecycle Logs
```
🟡 App -> onCreate
🟡 App -> onStart
🟡 App -> onResume
```

### Current State Logs
```
=== CURRENT LIFECYCLE STATE ===
📱 Current Active Activity: MainActivity
📄 Current Active Fragment: HomeFragment
📱 All Active Activities (2):
  • MainActivity: RESUMED (1500ms) ✅
  • SplashActivity: PAUSED (2000ms) ⏸️
📄 All Active Fragments (1):
  • HomeFragment (in MainActivity): RESUMED (500ms) ✅
=== END LIFECYCLE STATE ===
```

## 🛠️ Usage Examples

### 1. Current State Check करें

```kotlin
// Print current state
LifecycleLogHelper.printCurrentState()

// Get current active components
val currentActivity = LifecycleLogHelper.getCurrentActiveActivity()
val currentFragment = LifecycleLogHelper.getCurrentActiveFragment()

// Check if specific component is active
val isMainActive = LifecycleLogHelper.isActivityActive("MainActivity")
val isHomeActive = LifecycleLogHelper.isFragmentActive("HomeFragment")
```

### 2. Detailed Information

```kotlin
// Print detailed state
LifecycleLogHelper.printDetailedState()

// Print summary
LifecycleLogHelper.printSummary()

// Get all active components
val activities = LifecycleLogHelper.getAllActiveActivities()
val fragments = LifecycleLogHelper.getAllActiveFragments()
```

### 3. Manual Logging

```kotlin
// Manual activity logging
LifecycleLogHelper.logActivityEvent("MainActivity", "onCreate", "Custom data")

// Manual fragment logging
LifecycleLogHelper.logFragmentEvent("HomeFragment", "MainActivity", "onViewCreated")

// Custom event logging
LifecycleLogHelper.logCustomEvent("UserAction", "ButtonClicked", "Login button pressed")
```

## 🔍 Log Tags

- `ActivityLifecycle` - Activity lifecycle events
- `FragmentLifecycle` - Fragment lifecycle events
- `AppLifecycle` - App level lifecycle events
- `SimpleLifecycleLogger` - System initialization and state
- `LifecycleLogHelper` - Manual logging and utilities

## 📱 Example Activity

```kotlin
class YourActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Manual logging (optional)
        LifecycleLogHelper.logActivityEvent(javaClass.simpleName, "onCreate")
    }
    
    override fun onResume() {
        super.onResume()
        
        // Print current state
        LifecycleLogHelper.printCurrentState()
    }
}
```

## 🎯 Benefits

1. **Zero Configuration**: कोई configuration changes नहीं
2. **Automatic Tracking**: सभी components automatically tracked
3. **Current & Previous**: Current और previous active components track करता है
4. **Real-time**: Real-time state information
5. **Simple**: बहुत simple implementation
6. **No Dependencies**: कोई external dependencies नहीं

## 📋 What Gets Tracked

### Activities:
- ✅ onCreate
- ✅ onStart
- ✅ onResume
- ✅ onPause
- ✅ onStop
- ✅ onSaveInstanceState
- ✅ onDestroy

### Fragments:
- ✅ onFragmentPreAttached
- ✅ onFragmentAttached
- ✅ onFragmentPreCreated
- ✅ onFragmentCreated
- ✅ onFragmentViewCreated
- ✅ onFragmentViewDestroyed
- ✅ onFragmentStarted
- ✅ onFragmentResumed
- ✅ onFragmentPaused
- ✅ onFragmentStopped
- ✅ onFragmentDestroyed
- ✅ onFragmentDetached

### App:
- ✅ onCreate
- ✅ onStart
- ✅ onResume
- ✅ onPause
- ✅ onStop
- ✅ onDestroy

## 🚀 Quick Start

1. **AppApplication.kt में एक line add करें**:
   ```kotlin
   SimpleLifecycleLogger.getInstance().initialize(this)
   ```

2. **Logcat में logs देखें**:
   ```
   ActivityLifecycle: 🟢 Activity: MainActivity -> onCreate
   FragmentLifecycle: 🔵 Fragment: HomeFragment -> onFragmentResumed
   ```

3. **Manual state check करें**:
   ```kotlin
   LifecycleLogHelper.printCurrentState()
   ```

बस इतना ही! आपका lifecycle logging system ready है! 🎉

## 📞 Support

यदि आपको कोई समस्या आती है या कोई सवाल है, तो आप:

1. `LifecycleExampleActivity` को run करके देख सकते हैं
2. `LifecycleLogHelper` का use करके manual logging कर सकते हैं
3. Logcat में `ActivityLifecycle`, `FragmentLifecycle`, `AppLifecycle` tags देख सकते हैं

यह system आपके app के lifecycle को completely track करेगा और debugging में बहुत मदद करेगा! 🚀 