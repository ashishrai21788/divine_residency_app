# Firebase Notification Manager

A comprehensive Firebase Cloud Messaging (FCM) notification manager for Android applications. This library provides a simple and efficient way to handle Firebase notifications in your Android app.

## Features

- Firebase SDK initialization
- FCM token management
- Topic subscriptions
- Error handling
- Callback mechanisms
- Extensible design

## Setup

1. Add the following dependencies to your app's `build.gradle`:

```gradle
dependencies {
    implementation platform('com.google.firebase:firebase-bom:32.7.0')
    implementation 'com.google.firebase:firebase-messaging-ktx'
    implementation 'org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.1'
}
```

2. Add the `google-services.json` file to your app module.

3. Add the following service to your `AndroidManifest.xml`:

```xml

<service android:name="com.notification.CustomFirebaseMessagingService" android:exported="false">
    <intent-filter>
        <action android:name="com.google.firebase.MESSAGING_EVENT" />
    </intent-filter>
</service>
```

## Usage

### Initialization

```kotlin
// Initialize the Firebase Notification Manager
FirebaseNotificationManager.getInstance().initialize(
    context = applicationContext,
    callback = object : FirebaseNotificationManager.TokenCallback {
        override fun onTokenReceived(token: String) {
            // Handle the FCM token
            Log.d("FCM", "Token: $token")
        }

        override fun onError(error: String) {
            // Handle any errors
            Log.e("FCM", error)
        }
    }
)
```

### Subscribe to Topics

```kotlin
FirebaseNotificationManager.getInstance().subscribeToTopic(
    topic = "news",
    callback = object : FirebaseNotificationManager.TopicCallback {
        override fun onTopicOperationSuccess(topic: String, isSubscribed: Boolean) {
            // Handle successful subscription
            Log.d("FCM", "Successfully subscribed to $topic")
        }

        override fun onTopicOperationError(topic: String, error: String) {
            // Handle subscription error
            Log.e("FCM", "Failed to subscribe to $topic: $error")
        }
    }
)
```

### Unsubscribe from Topics

```kotlin
FirebaseNotificationManager.getInstance().unsubscribeFromTopic(
    topic = "news",
    callback = object : FirebaseNotificationManager.TopicCallback {
        override fun onTopicOperationSuccess(topic: String, isSubscribed: Boolean) {
            // Handle successful unsubscription
            Log.d("FCM", "Successfully unsubscribed from $topic")
        }

        override fun onTopicOperationError(topic: String, error: String) {
            // Handle unsubscription error
            Log.e("FCM", "Failed to unsubscribe from $topic: $error")
        }
    }
)
```

### Handle Incoming Messages

```kotlin
FirebaseNotificationManager.getInstance().setNotificationCallback(
    object : FirebaseNotificationManager.NotificationCallback {
        override fun onMessageReceived(message: RemoteMessage) {
            // Handle incoming message
            Log.d("FCM", "Message received: ${message.data}")
        }

        override fun onNewToken(token: String) {
            // Handle new token
            Log.d("FCM", "New token: $token")
        }

        override fun onError(error: String) {
            // Handle error
            Log.e("FCM", error)
        }
    }
)
```

## Error Handling

The library provides comprehensive error handling through callback interfaces. All operations that might fail (token retrieval, topic subscription, etc.) include error callbacks to handle failures gracefully.

## Thread Safety

The `FirebaseNotificationManager` is implemented as a singleton and is thread-safe. All operations are performed on appropriate threads using Kotlin coroutines where necessary.

## Best Practices

1. Always initialize the manager in your Application class
2. Handle all callbacks appropriately
3. Implement proper error handling
4. Store the FCM token securely
5. Handle token refresh events
6. Implement proper notification channels for Android 8.0 and above 