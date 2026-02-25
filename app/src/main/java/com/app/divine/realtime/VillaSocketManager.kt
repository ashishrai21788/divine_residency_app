package com.app.divine.realtime

import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.app.core.dagger.preference.AppPreferences
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import java.util.concurrent.TimeUnit

/**
 * Lightweight WebSocket manager for Villa Society realtime updates.
 * Join with JWT; listen for visitor_request, visitor_status_update, sos_triggered.
 * Update ViewModels via socketEventLiveData; use RealtimeDedup to avoid duplicate push + socket.
 */
class VillaSocketManager(
    private val appPreferences: AppPreferences,
    private val okHttpClient: OkHttpClient
) {
    companion object {
        private const val TAG = "VillaSocketManager"
        /** Replace with your backend WebSocket URL (e.g. wss://api.example.com/socket) */
        private const val DEFAULT_WS_URL = "ws://localhost:3000/socket"
        private const val RECONNECT_DELAY_MS = 3000L
        private const val PING_INTERVAL_SEC = 30
    }

    private val gson = Gson()
    private val mainHandler = Handler(Looper.getMainLooper())
    private var webSocket: WebSocket? = null
    private var connectionUrl: String = DEFAULT_WS_URL
    private var reconnectRunnable: Runnable? = null
    private var isConnectingOrConnected: Boolean = false

    private val _socketEventLiveData = MutableLiveData<VillaSocketEvent?>()
    val socketEventLiveData: LiveData<VillaSocketEvent?> = _socketEventLiveData

    private val _connectionState = MutableLiveData<ConnectionState>(ConnectionState.DISCONNECTED)
    val connectionState: LiveData<ConnectionState> = _connectionState

    enum class ConnectionState { CONNECTING, CONNECTED, DISCONNECTED, FAILED }

    fun connect(baseUrl: String = DEFAULT_WS_URL) {
        if (isConnectingOrConnected) return
        val token = appPreferences.getAllPreferenceTypeString(AppPreferences.VILLA_ACCESS_TOKEN_KEY)
        if (token.isEmpty()) {
            Log.w(TAG, "No JWT token, skip socket connect")
            _connectionState.postValue(ConnectionState.FAILED)
            return
        }
        isConnectingOrConnected = true
        val url = baseUrl
            .replace("http://", "ws://")
            .replace("https://", "wss://")
            .trimEnd('/') + "/socket?token=${java.net.URLEncoder.encode(token, "UTF-8")}"
        connectionUrl = url
        disconnect()
        _connectionState.postValue(ConnectionState.CONNECTING)
        val request = Request.Builder().url(url).build()
        webSocket = okHttpClient.newWebSocket(request, createListener())
    }

    fun disconnect() {
        isConnectingOrConnected = false
        reconnectRunnable?.let { mainHandler.removeCallbacks(it) }
        reconnectRunnable = null
        webSocket?.close(1000, "Client disconnect")
        webSocket = null
        _connectionState.postValue(ConnectionState.DISCONNECTED)
    }

    private fun scheduleReconnect() {
        if (reconnectRunnable != null) return
        reconnectRunnable = Runnable {
            reconnectRunnable = null
            connect(connectionUrl)
        }
        mainHandler.postDelayed(reconnectRunnable!!, RECONNECT_DELAY_MS)
    }

    private fun createListener() = object : WebSocketListener() {
        override fun onOpen(webSocket: WebSocket, response: Response) {
            Log.d(TAG, "Socket open")
            mainHandler.post { _connectionState.value = ConnectionState.CONNECTED }
        }

        override fun onMessage(webSocket: WebSocket, text: String) {
            try {
                val event = gson.fromJson(text, VillaSocketEvent::class.java)
                if (event.event.isNotEmpty()) {
                    mainHandler.post {
                        _socketEventLiveData.value = event
                        RealtimeDedup.markHandledBySocket(event)
                    }
                }
            } catch (e: JsonSyntaxException) {
                Log.e(TAG, "Parse socket message failed: $text", e)
            }
        }

        override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
            Log.d(TAG, "Socket closing: $code $reason")
        }

        override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
            Log.d(TAG, "Socket closed")
            mainHandler.post {
                isConnectingOrConnected = false
                _connectionState.value = ConnectionState.DISCONNECTED
            }
        }

        override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
            Log.e(TAG, "Socket failure", t)
            mainHandler.post {
                isConnectingOrConnected = false
                _connectionState.value = ConnectionState.FAILED
                scheduleReconnect()
            }
        }
    }

    /** Events relevant for Resident (visitor_request, visitor_status_update, sos_triggered). */
    fun isResidentEvent(event: String): Boolean =
        event in listOf("visitor_request", "visitor_status_update", "sos_triggered")

    /** Events relevant for Guard (sos_triggered, visitor_status_update). */
    fun isGuardEvent(event: String): Boolean =
        event in listOf("sos_triggered", "visitor_status_update")
}
