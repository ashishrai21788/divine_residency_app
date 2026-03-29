package com.app.core.config

/**
 * **Single place to set the NestJS backend host** for Villa Society (REST + Socket.IO).
 *
 * ### What to change
 * Update [HOST_ORIGIN] only. [API_BASE_URL] and [SOCKET_BASE_URL] are the same origin — the API and
 * Socket.IO share host/port; REST lives under [API_PATH] (`/api` by default on the server), while
 * Socket.IO uses **`/socket.io/`** automatically (do **not** append `/socket` or `/socket.io` here).
 *
 * ### Emulator (Android Studio AVD)
 * Use **`http://10.0.2.2:3000`** — `10.0.2.2` is the special alias to your development machine’s
 * `localhost` from inside the emulator.
 *
 * ### Physical phone (same Wi‑Fi as your PC)
 * Use your computer’s **LAN IP**, e.g. **`http://192.168.1.8:3000`** (find it with `ipconfig` /
 * `ifconfig`; port must match the API, usually `3000`).
 *
 * You must also allow **cleartext HTTP** for that host in `res/xml/network_security_config.xml`
 * (see the commented example there for adding your LAN IP). For production, prefer **HTTPS** and
 * set [HOST_ORIGIN] to `https://your-api.example.com` (no cleartext config needed).
 *
 * ### Production
 * Prefer [BuildConfig](https://developer.android.com/build/gradle-tips#share-custom-fields-between-app-and-library)
 * or remote config so debug vs release URLs differ without editing source.
 */
object VillaSocietyConfig {

    /**
     * Scheme + host + port only, **no** path and **no** trailing slash.
     * Change this one value when switching emulator ↔ device ↔ staging.
     */
    const val HOST_ORIGIN: String = "http://10.0.2.2:3000"

    /** Same as [HOST_ORIGIN]; REST base without path segment. */
    const val API_BASE_URL: String = HOST_ORIGIN

    /**
     * Global HTTP prefix on the server (Nest `setGlobalPrefix`, default `api`).
     * Retrofit base URL becomes `HOST_ORIGIN + API_PATH + /`.
     */
    const val API_PATH: String = "/api"

    /** Full Retrofit base; must end with `/` for relative `@GET("auth/login")` paths. */
    const val API_BASE_URL_WITH_PATH: String = "$API_BASE_URL$API_PATH/"

    /**
     * Socket.IO **origin** only (same as [HOST_ORIGIN]). The client connects to `/socket.io/`
     * on this host; JWT is sent in the handshake (`auth.token` + `Authorization` header).
     */
    const val SOCKET_BASE_URL: String = HOST_ORIGIN
}
