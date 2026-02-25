package com.notification

import android.content.Context
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations

class FirebaseNotificationManagerTest {

    @Mock
    private lateinit var context: Context

    @Mock
    private lateinit var tokenCallback: FirebaseNotificationManager.TokenCallback

    @Mock
    private lateinit var notificationCallback: FirebaseNotificationManager.NotificationCallback

    private lateinit var manager: FirebaseNotificationManager
    private val testSenderId = "test-sender-id"

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        manager = FirebaseNotificationManager.getInstance()
    }

    @Test
    fun `test initialization with sender ID`() {
        manager.initialize(context, testSenderId, tokenCallback)
        verify(tokenCallback, never()).onError(anyString())
    }

    @Test
    fun `test initialization without sender ID`() {
        manager.initialize(context, "", tokenCallback)
        verify(tokenCallback).onError(anyString())
    }

    @Test
    fun `test token retrieval with sender ID`() = runBlocking {
        manager.initialize(context, testSenderId, tokenCallback)
        manager.getToken(tokenCallback)
        verify(tokenCallback, timeout(5000)).onTokenReceived(anyString())
    }

    @Test
    fun `test token retrieval without initialization`() = runBlocking {
        manager.getToken(tokenCallback)
        verify(tokenCallback).onError("Firebase not initialized with sender ID")
    }

    @Test
    fun `test topic subscription with sender ID`() {
        manager.initialize(context, testSenderId, tokenCallback)
        val topic = "test_topic"
        val topicCallback = mock(FirebaseNotificationManager.TopicCallback::class.java)
        
        manager.subscribeToTopic(topic, topicCallback)
        verify(topicCallback, timeout(5000)).onTopicOperationSuccess(eq(topic), eq(true))
    }

    @Test
    fun `test topic unsubscription with sender ID`() {
        manager.initialize(context, testSenderId, tokenCallback)
        val topic = "test_topic"
        val topicCallback = mock(FirebaseNotificationManager.TopicCallback::class.java)
        
        manager.unsubscribeFromTopic(topic, topicCallback)
        verify(topicCallback, timeout(5000)).onTopicOperationSuccess(eq(topic), eq(false))
    }

    @Test
    fun `test message processing with sender ID`() {
        manager.initialize(context, testSenderId, tokenCallback)
        val message = mock(RemoteMessage::class.java)
        `when`(message.notification).thenReturn(null)
        `when`(message.data).thenReturn(mapOf("key" to "value"))

        manager.setNotificationCallback(notificationCallback)
        manager.processMessage(message, false)

        verify(notificationCallback, timeout(5000)).onDataMessageReceived(message)
    }

    @Test
    fun `test notification action handling with sender ID`() {
        manager.initialize(context, testSenderId, tokenCallback)
        val message = mock(RemoteMessage::class.java)
        val action = "test_action"
        val data = mapOf("action" to action, "key" to "value")
        
        `when`(message.data).thenReturn(data)

        manager.setNotificationCallback(notificationCallback)
        manager.processMessage(message, false)

        verify(notificationCallback, timeout(5000)).onNotificationAction(eq(action), eq(data))
    }

    @Test
    fun `test error handling with sender ID`() {
        manager.initialize(context, testSenderId, tokenCallback)
        val errorMessage = "Test error"
        manager.setNotificationCallback(notificationCallback)
        notificationCallback?.onError(errorMessage)
        
        verify(notificationCallback, timeout(5000)).onError(errorMessage)
    }
} 