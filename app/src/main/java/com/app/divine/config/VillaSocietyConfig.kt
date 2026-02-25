package com.app.divine.config

/**
 * Central config for Villa Society API and WebSocket base URLs.
 * Replace with BuildConfig or remote config in production.
 */
object VillaSocietyConfig {

    /** HTTP base for Villa Society API (no trailing slash). */
    const val API_BASE_URL = "http://localhost:3000"

    /** API path prefix (with leading slash). */
    const val API_PATH = "/api"

    /** Full API base for Retrofit: must end with / for relative paths. */
    const val API_BASE_URL_WITH_PATH = "$API_BASE_URL$API_PATH/"

    /** WebSocket base (same host as API). */
    const val SOCKET_BASE_URL = "http://localhost:3000"
}
