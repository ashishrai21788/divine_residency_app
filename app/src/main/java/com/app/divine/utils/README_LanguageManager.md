# Language Manager Documentation

## Overview
The LanguageManager is a utility class that reads language strings from JSON files (`language_en.json`, `language_hi.json`, `language_ar.json`, etc.) located in the assets folder. It provides a centralized way to manage all text strings in the application with support for multiple languages.

## Setup

### 1. JSON File Structure
Language files are located at `app/src/main/assets/` with the naming convention `language_{code}.json` (e.g., `language_en.json`, `language_hi.json`, `language_ar.json`). Each file contains all the language strings organized in a hierarchical structure:

**English (language_en.json):**
```json
{
  "app_name": "eRickshaw Driver",
  "login": {
    "title": "Login",
    "email": "Email",
    "password": "Password",
    "forgot_password": "Forgot Password?",
    "login_button": "Login"
  },
  "common": {
    "ok": "OK",
    "cancel": "Cancel",
    "loading": "Loading..."
  }
}
```

**Hindi (language_hi.json):**
```json
{
  "app_name": "ई-रिक्शा ड्राइवर",
  "login": {
    "title": "लॉगिन",
    "email": "ईमेल",
    "password": "पासवर्ड",
    "forgot_password": "पासवर्ड भूल गए?",
    "login_button": "लॉगिन करें"
  },
  "common": {
    "ok": "ठीक है",
    "cancel": "रद्द करें",
    "loading": "लोड हो रहा है..."
  }
}
```

### 2. Initialization
The LanguageManager is automatically initialized in the `AppApplication.onCreate()` method and loads the user's previously selected language:

```kotlin
// In AppApplication.kt
override fun onCreate() {
    super.onCreate()
    // ... other initialization code
    
    // Initialize LanguageManager (loads user's selected language)
    LanguageManager.initialize(this)
    
    // ... rest of initialization
}
```

## Usage

### 1. Basic Usage
```kotlin
import com.app.divine.utils.LanguageManager

// Get a simple string
val appName = LanguageManager.getString("app_name")

// Get a nested string using dot notation
val loginTitle = LanguageManager.getString("login.title")
val loginEmail = LanguageManager.getString("login.email")
```

### 2. With Default Values
```kotlin
// If the key doesn't exist, return a default value
val text = LanguageManager.getString("some.key", "Default Text")
```

### 3. Using Extension Functions
```kotlin
import com.app.divine.utils.setTextFromLanguage
import com.app.divine.utils.setTitleFromLanguage

// Set TextView text directly
textView.setTextFromLanguage("login.title")

// Set TextView text with default value
textView.setTextFromLanguage("some.key", "Default Text")

// Set Toolbar title
toolbar.setTitleFromLanguage("login.title")
```

### 4. In Activities/Fragments
```kotlin
class LoginActivity : AppCompatActivity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Set toolbar title
        binding.toolbar.setTitleFromLanguage("login.title")
        
        // Set text views
        binding.tvEmail.setTextFromLanguage("login.email")
        binding.tvPassword.setTextFromLanguage("login.password")
        binding.btnLogin.setTextFromLanguage("login.login_button")
        
        // Set text programmatically
        binding.tvForgotPassword.text = LanguageManager.getString("login.forgot_password")
    }
}
```

### 5. Language Selection
```kotlin
// Change language
LanguageManager.setLanguage(context, "hi") // Switch to Hindi

// Get current language
val currentLang = LanguageManager.getCurrentLanguage() // Returns "hi"

// Get available languages
val availableLanguages = LanguageManager.getAvailableLanguages(context)

// Using LanguageHelper for better language management
val languages = LanguageHelper.getAvailableLanguages(context)
val currentLanguageName = LanguageHelper.getCurrentLanguageDisplayName()
val isRTL = LanguageHelper.isCurrentLanguageRTL()
```

### 6. Real-Time Language Changes
The system supports real-time language changes throughout the app:

#### Base Classes for Automatic Language Updates:
```kotlin
// For Activities
class MyActivity : BaseLanguageActivity() {
    override fun updateUIForLanguageChange() {
        // Update all UI elements with new language
        binding.title.text = LanguageManager.getString("my.title")
        binding.description.text = LanguageManager.getString("my.description")
    }
}

// For Fragments
class MyFragment : BaseLanguageFragment() {
    override fun updateUIForLanguageChange() {
        // Update all UI elements with new language
        binding.title.text = LanguageManager.getString("my.title")
    }
}
```

#### Manual Language Change Listener:
```kotlin
class MyActivity : AppCompatActivity(), LanguageChangeListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LanguageChangeManager.registerListener(this)
    }
    
    override fun onDestroy() {
        super.onDestroy()
        LanguageChangeManager.unregisterListener(this)
    }
    
    override fun onLanguageChanged(newLanguageCode: String) {
        // Update UI when language changes
        updateUI()
    }
}
```

#### UI Helper for Common Updates:
```kotlin
// Update multiple TextViews at once
LanguageUIHelper.updateTextViews(
    binding.title to "my.title",
    binding.description to "my.description"
)

// Update Toolbar title
LanguageUIHelper.updateToolbarTitle(binding.toolbar, "my.title")

// Update ObservableField
LanguageUIHelper.updateObservableField(binding.titleField, "my.title")
```

## Best Practices

### 1. Key Naming Convention
- Use lowercase with underscores for simple keys: `app_name`, `loading`
- Use dot notation for nested keys: `login.title`, `signup.full_name`
- Group related strings under common prefixes: `login.*`, `signup.*`, `common.*`

### 2. Error Handling
- The LanguageManager returns the key itself if the string is not found
- Always provide meaningful default values when using `getString(key, defaultValue)`
- Check logs for warnings about missing keys

### 3. Performance
- The JSON file is loaded once during app initialization
- All subsequent calls are fast as they access the in-memory JSONObject
- The LanguageManager is thread-safe
- Language changes are persisted in SharedPreferences
- Automatic fallback to default language if requested language file is missing
- Real-time language changes with automatic UI updates
- Memory-efficient listener management with automatic cleanup

## App Flow Management

The app includes intelligent flow management based on language selection:

### Flow Logic
- **App Launch**: SplashActivity (launcher activity)
- **First Launch**: SplashActivity → LanguageSelectionActivity (with welcome message) → Main App
- **Language Not Selected**: SplashActivity → LanguageSelectionActivity → Main App
- **Language Selected**: SplashActivity → Main App (LoginSignupActivity)

### Flow Manager
```kotlin
// Check if language is selected
val isSelected = LanguageFlowManager.isLanguageSelected(context)

// Check if it's first launch
val isFirstLaunch = LanguageFlowManager.isFirstLaunch(context)

// Get next activity after splash
val nextActivity = LanguageFlowManager.getNextActivityAfterSplash(context)

// Complete language selection and proceed to main app
LanguageFlowManager.completeLanguageSelection(context)

// Reset for testing
LanguageFlowManager.resetLanguageSelection(context)
LanguageFlowManager.resetFirstLaunch(context)
```

### Implementation
The `SplashActivity` is set as the launcher activity in `AndroidManifest.xml`. It:
1. Shows splash screen for 2 seconds
2. Checks language flow status
3. Routes to LanguageSelectionActivity if needed
4. Routes directly to main app if language is already selected

The `LanguageSelectionActivity` handles:
1. Welcome message for first-time users
2. Language selection interface
3. Flow completion to main app

## Adding New Languages

To add support for additional languages:

1. Create new JSON files: `language_es.json`, `language_fr.json`, etc.
2. Add the language to `LanguageHelper.getLanguageDisplayName()` and `LanguageHelper.getLanguageNativeName()`
3. Add the flag emoji to `LanguageHelper.getLanguageFlag()`
4. The LanguageManager will automatically detect and load the new language file

### Example: Adding Spanish Support
1. Create `app/src/main/assets/language_es.json`
2. Add to LanguageHelper:
```kotlin
"es" -> "Spanish" // in getLanguageDisplayName()
"es" -> "Español" // in getLanguageNativeName()
"es" -> "🇪🇸" // in getLanguageFlag()
```

## Example JSON Structure
```json
{
  "app_name": "eRickshaw Driver",
  "login": {
    "title": "Login",
    "email": "Email",
    "password": "Password",
    "forgot_password": "Forgot Password?",
    "login_button": "Login",
    "signup_link": "Don't have an account? Sign up",
    "email_required": "Email is required",
    "password_required": "Password is required",
    "invalid_email": "Please enter a valid email",
    "login_success": "Login successful",
    "login_failed": "Login failed"
  },
  "signup": {
    "title": "Sign Up",
    "full_name": "Full Name",
    "email": "Email",
    "phone": "Phone Number",
    "password": "Password",
    "confirm_password": "Confirm Password",
    "signup_button": "Sign Up",
    "login_link": "Already have an account? Login"
  },
  "common": {
    "ok": "OK",
    "cancel": "Cancel",
    "yes": "Yes",
    "no": "No",
    "save": "Save",
    "delete": "Delete",
    "edit": "Edit",
    "loading": "Loading...",
    "error": "Error",
    "success": "Success",
    "network_error": "Network error. Please check your connection.",
    "try_again": "Try Again",
    "back": "Back",
    "next": "Next",
    "submit": "Submit"
  }
}
``` 