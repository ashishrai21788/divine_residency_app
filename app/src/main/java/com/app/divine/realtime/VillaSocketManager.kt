package com.app.divine.realtime

import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.app.core.config.VillaSocietyConfig
import com.app.core.dagger.preference.AppPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter
import org.json.JSONObject
import java.net.URI
import java.net.URISyntaxException

/**
 * Socket.IO client for NestJS [EventsGateway]: JWT in handshake `auth.token` (matches server).
 * Server emits event names with JSON payloads (e.g. visitor_request + visitor document), not {event,data} envelopes.
 */
class VillaSocketManager(
    private val appPreferences: AppPreferences,
) {
    companion object {
        private const val TAG = "VillaSocketManager"
        private val DEFAULT_SOCKET_URL = VillaSocietyConfig.SOCKET_BASE_URL

        private val TRACKED_EVENTS = arrayOf(
            "visitor_request",
            "visitor_status_update",
            "sos_triggered",
            "delivery_arrived",
            "blacklist_attempt",
            "guard_checked_in",
            "complaint_created",
            "complaint_status_updated",
        )
    }

    private val gson = Gson()
    private val mainHandler = Handler(Looper.getMainLooper())
    private var socket: Socket? = null
    private var lastBaseUrl: String? = null
    private var lastToken: String? = null

    private val eventListeners = TRACKED_EVENTS.associateWith { eventName ->
        Emitter.Listener { args -> dispatchSocketEvent(eventName, args.firstOrNull()) }
    }

    private val onConnect = Emitter.Listener {
        Log.d(TAG, "Socket.IO connected")
        mainHandler.post { _connectionState.value = ConnectionState.CONNECTED }
    }

    private val onDisconnect = Emitter.Listener { args ->
        val reason = args.firstOrNull()?.toString() ?: ""
        Log.d(TAG, "Socket.IO disconnected: $reason")
        mainHandler.post { _connectionState.value = ConnectionState.DISCONNECTED }
    }

    private val onConnectError = Emitter.Listener { args ->
        val err = args.firstOrNull()
        Log.e(TAG, "Socket.IO connect_error: $err")
        mainHandler.post { _connectionState.value = ConnectionState.FAILED }
    }

    private val _socketEventLiveData = MutableLiveData<VillaSocketEvent?>()
    val socketEventLiveData: LiveData<VillaSocketEvent?> = _socketEventLiveData

    private val _connectionState = MutableLiveData<ConnectionState>(ConnectionState.DISCONNECTED)
    val connectionState: LiveData<ConnectionState> = _connectionState

    enum class ConnectionState { CONNECTING, CONNECTED, DISCONNECTED, FAILED }

    /**
     * @param baseUrl HTTP(S) origin only, e.g. `http://10.0.2.2:3000` (no `/socket` path; Engine.IO uses `/socket.io/`).
     */
    fun connect(baseUrl: String = DEFAULT_SOCKET_URL) {
        val token = appPreferences.getAllPreferenceTypeString(AppPreferences.VILLA_ACCESS_TOKEN_KEY)
        if (token.isEmpty()) {
            Log.w(TAG, "No JWT token, skip Socket.IO connect")
            _connectionState.postValue(ConnectionState.FAILED)
            return
        }

        val normalized = normalizeBaseUrl(baseUrl)
        if (socket?.connected() == true && normalized == lastBaseUrl && token == lastToken) {
            Log.d(TAG, "Already connected to $normalized")
            return
        }

        tearDown()
        lastBaseUrl = normalized
        lastToken = token
        _connectionState.postValue(ConnectionState.CONNECTING)

        try {
            val uri = URI.create(normalized)
            val opts = IO.Options().apply {
                reconnection = true
                reconnectionAttempts = Int.MAX_VALUE
                reconnectionDelay = 3000
                reconnectionDelayMax = 20_000
                timeout = 20_000
                forceNew = true
                auth = HashMap<String, String>().apply { put("token", token) }
                extraHeaders = mapOf("Authorization" to listOf("Bearer $token"))
            }

            val s = IO.socket(uri, opts)
            s.on(Socket.EVENT_CONNECT, onConnect)
            s.on(Socket.EVENT_DISCONNECT, onDisconnect)
            s.on(Socket.EVENT_CONNECT_ERROR, onConnectError)
            eventListeners.forEach { (name, listener) -> s.on(name, listener) }
            s.connect()
            socket = s
        } catch (e: URISyntaxException) {
            Log.e(TAG, "Invalid Socket.IO URL: $normalized", e)
            lastBaseUrl = null
            lastToken = null
            _connectionState.postValue(ConnectionState.FAILED)
        } catch (e: Exception) {
            Log.e(TAG, "Socket.IO setup failed", e)
            lastBaseUrl = null
            lastToken = null
            _connectionState.postValue(ConnectionState.FAILED)
        }
    }

    fun disconnect() {
        lastBaseUrl = null
        lastToken = null
        tearDown()
        _connectionState.postValue(ConnectionState.DISCONNECTED)
    }

    private fun tearDown() {
        socket?.let { s ->
            s.off(Socket.EVENT_CONNECT, onConnect)
            s.off(Socket.EVENT_DISCONNECT, onDisconnect)
            s.off(Socket.EVENT_CONNECT_ERROR, onConnectError)
            eventListeners.forEach { (name, listener) -> s.off(name, listener) }
            s.disconnect()
        }
        socket = null
    }

    private fun dispatchSocketEvent(eventName: String, rawPayload: Any?) {
        val data = payloadToMap(rawPayload)
        val event = VillaSocketEvent(event = eventName, data = data)
        mainHandler.post {
            _socketEventLiveData.value = event
            RealtimeDedup.markHandledBySocket(event)
        }
    }

    private fun payloadToMap(arg: Any?): Map<String, Any?> {
        if (arg == null) return emptyMap()
        val json = when (arg) {
            is JSONObject -> arg.toString()
            is String -> arg
            else -> try {
                gson.toJson(arg)
            } catch (e: Exception) {
                Log.w(TAG, "Could not serialize socket payload", e)
                return emptyMap()
            }
        }
        return try {
            val type = object : TypeToken<HashMap<String, Any?>>() {}.type
            gson.fromJson<HashMap<String, Any?>>(json, type) ?: emptyMap()
        } catch (e: Exception) {
            Log.w(TAG, "Could not parse socket JSON: $json", e)
            emptyMap()
        }
    }

    private fun normalizeBaseUrl(url: String): String {
        var u = url.trim().trimEnd('/')
        if (!u.startsWith("http://") && !u.startsWith("https://")) {
            u = "http://$u"
        }
        return u
    }

    fun isResidentEvent(event: String): Boolean =
        event in setOf(
            "visitor_request",
            "visitor_status_update",
            "delivery_arrived",
            "sos_triggered",
            "blacklist_attempt",
            "complaint_created",
            "complaint_status_updated",
        )

    fun isGuardEvent(event: String): Boolean =
        event in setOf(
            "visitor_request",
            "visitor_status_update",
            "delivery_arrived",
            "sos_triggered",
            "blacklist_attempt",
            "guard_checked_in",
            "complaint_created",
            "complaint_status_updated",
        )
}
