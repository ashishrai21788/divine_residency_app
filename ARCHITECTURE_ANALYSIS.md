# Divine Residency App — Phase 1 Architecture Analysis

**Senior Android Architect — Project analysis only. No feature code.**

---

## 1. Activity structure

| Activity | Role | Base class | In manifest |
|----------|------|------------|-------------|
| **SplashActivity** | Launcher (MAIN); decides post-splash navigation | `AppCompatActivity` | Yes (exported) |
| **LanguageSelectionActivity** | Language picker | (not BaseLanguageActivity in scan) | Yes (x2 entries) |
| **LoginSignupActivity** | Login / Register entry | `AppCompatActivity` | Yes |
| **LoginActivity** | Login screen | `BaseLanguageActivity` | Yes |
| **SignupActivity** | Registration | `BaseLanguageActivity` | Yes |
| **ForgotPasswordActivity** | Forgot password | `BaseLanguageActivity` | Yes |
| **LandingMainActivity** | Main container with bottom nav + fragments | `AppCompatActivity` | Yes |
| **MainActivity** | Compose sample (Greeting) | `ComponentActivity` | **No** (unused) |

**Pattern:** Feature-based packages: `com.app.divine.activity.<feature>.view` (e.g. `splash`, `login`, `landing`). Each feature has its own `view`, `viewmodel`, `di`, and sometimes `api`/`adapter`/`model`.

---

## 2. Fragment vs Compose usage

- **Primary UI:** **XML layouts + DataBinding** (e.g. `ActivityLandingMainBinding`, `HomeFragmentBinding`). Activities use `DataBindingUtil.setContentView` or `Binding.inflate(layoutInflater)`; fragments use `XxxBinding.inflate(inflater, container, false)`.
- **Compose:** Present only in:
  - **MainActivity** — `setContent { DRWATheme { ... } }` (not in manifest, effectively unused).
  - **Theme:** `com.app.divine.ui.theme` (DRWATheme, Color, Type) is Compose-only and not used by the main flow.
- **Conclusion:** App is **Fragment + XML/DataBinding-based**. Compose is available (BOM, Material3) but not part of the main navigation or screens. New screens should follow **XML + DataBinding + Fragments** unless a deliberate move to Compose is made.

---

## 3. Navigation method

- **No NavGraph / Navigation Component** in use. No `NavController`, `NavHostFragment`, or `nav_graph.xml` references in the app.
- **Navigation style:** **Manual** — `Intent` + `startActivity` between activities; fragment switching via `FragmentTransaction.replace()` inside the single container activity.
- **Activity → Activity:** e.g. `LoginSignupActivity` → `LoginActivity` / `SignupActivity` via `startActivity(Intent(...))`. Splash/Language flows use `Intent` and `startActivity`.
- **Fragment switching:** `LandingMainActivity` uses `supportFragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).commit()` and bottom navigation item IDs (`R.id.menu_home`, `menu_search`, etc.) to show `HomeFragment`, `SearchFragment`, `CenterFragment`, `NotificationsFragment`, `ProfileFragment`.
- **Conclusion:** **Manual navigation** (Intents + replace transactions). No single NavGraph or router abstraction. To extend: add new `startActivity`/`Intent` for new activities; add new `when` branch and `replace()` for new tabs in `LandingMainActivity.setupBottomNavigation()`.

---

## 4. MVVM implementation

- **ViewModels:** Present and used. Each screen (activity/fragment) has a corresponding ViewModel in `viewmodel` package (e.g. `SplashViewModel`, `LoginViewModel`, `HomeViewModel`, `LandingMainViewModel`).
- **Base ViewModel:** `CoreViewModel` in `core` (holds `AppDatabase`, `Retrofit`, `OkHttpClient`, `AppPreferences`, `Context`). App ViewModels extend it and add screen-specific LiveData/API calls.
- **Binding to UI:** ViewModels are provided by **Dagger** (activity/fragment-scoped components) and obtained via `getViewModel { XxxViewModel(...) }` (custom `Fragment.getViewModel` / activity equivalent). UI observes **LiveData** (e.g. `viewModel.loginResult.observe(this) { ... }`).
- **No Repository layer in app:** ViewModels call Retrofit/API directly (e.g. `retrofit.create(LoginApiService::class.java)` inside `LoginViewModel`). No Repository or UseCase classes found.
- **Conclusion:** **MVVM is used** (Activity/Fragment → ViewModel → API/DB), but the “Model” side is **direct API/DB access from ViewModels**, not Repositories or UseCases.

---

## 5. Base classes (BaseActivity, BaseFragment, BaseViewModel)

- **BaseViewModel:** **Yes** — `CoreViewModel` (core module): takes `AppDatabase`, `Retrofit`, `OkHttpClient`, `AppPreferences`, `Context`; all feature ViewModels extend it.
- **BaseActivity:** **No generic BaseActivity.** There is **BaseLanguageActivity** (app): extends `AppCompatActivity`, implements `LanguageChangeListener`, registers with `LanguageChangeManager`, exposes `updateUIForLanguageChange()`. Used by Login, Signup, ForgotPassword. Other activities extend `AppCompatActivity` or `ComponentActivity` directly.
- **BaseFragment:** **No generic BaseFragment.** There is **BaseLanguageFragment** (app): extends `Fragment`, implements `LanguageChangeListener`, same language-change pattern. Current fragments (Home, Search, Center, Notifications, Profile) extend **Fragment** directly, not BaseLanguageFragment.
- **Conclusion:** One **base ViewModel** (CoreViewModel). **Base activities/fragments** are only for **language switching** (BaseLanguageActivity / BaseLanguageFragment); not all screens use them. No shared “base” for toolbar, loading, or error handling.

---

## 6. Repository + UseCase pattern

- **Repository:** **Not used.** No `*Repository` classes. ViewModels create API services (e.g. `retrofit.create(LoginApiService::class.java)`) and call them directly.
- **UseCase:** **Not used.** No UseCase / Interactor classes.
- **Conclusion:** **No Repository or UseCase layer.** API and (where used) DB access live inside ViewModels. Adding Villa Society APIs could either keep this style (new Retrofit interfaces + ViewModel calls) or introduce Repositories/UseCases as a new pattern.

---

## 7. Retrofit setup

- **Single Retrofit instance:** Provided in **core** by **NetworkModule**: `Retrofit.Builder().baseUrl("https://api.github.com/")`, Gson converter, RxJava2CallAdapter, single `OkHttpClient` (timeouts 3 min connect/read/write). Exposed via **CoreComponent** as `retrofit()` and `okHttpClient()`.
- **App module:** Does **not** create its own Retrofit; activities/fragments receive `Retrofit` (and `OkHttpClient`) from Dagger and create **feature-specific interfaces** with `retrofit.create(XxxApiService::class.java)` (e.g. `LoginApiService`).
- **Base URL:** Currently hardcoded in core `NetworkModule` as `https://api.github.com/`. **Villa Society** would require a different base (e.g. `http://localhost:3000/api`) — either change here or support multiple base URLs (e.g. named qualifiers or a configurable base URL).
- **Conclusion:** **Single shared Retrofit** in core; feature APIs are created per screen. New APIs = new Retrofit interface in app (or core if shared) and `retrofit.create(NewApiService::class.java)` in the ViewModel.

---

## 8. Interceptors + token refresh logic

- **Interceptors:** Only **Stetho** is added: `OkHttpClient.Builder().addNetworkInterceptor(stethoInterceptor)` in `NetworkModule`. No auth or logging interceptors found.
- **Token refresh:** **Not implemented.** No `Authorization` header interceptor, no refresh-token logic, no 401 handling. `AppPreferences` exists and could store tokens, but no code path sets/reads auth headers or refreshes tokens.
- **Conclusion:** **No auth interceptors or token refresh.** Adding Villa Society auth (login, refresh-token, profile) will require at least: an interceptor that adds `Authorization`, and optional refresh logic (e.g. on 401) using `POST /auth/refresh-token`.

---

## 9. DI framework (Hilt/Dagger/Koin)

- **Framework:** **Dagger 2** (no Hilt, no Koin).
- **Structure:**
  - **Core:** `CoreComponent` (Singleton) — provides `Context`, `Retrofit`, `OkHttpClient`, `APIServices`, `AppDatabase`, `AppPreferences`, `CoreConnectionLiveData`. Built in `AppApplication` with `ContextModule`, `NetworkModule`, `DatabaseModule`, `AppPreference`.
  - **App:** Each **activity** has its own **Activity-scoped component** (e.g. `SplashActivityComponent`, `LoginActivityComponent`) that **depends on** `CoreComponent` and has a module (e.g. `SplashActivityModule`) that provides the **ViewModel** for that screen. Same for **fragments** (e.g. `HomeFragmentComponent` depends on `CoreComponent`, `HomeFragmentModule` provides `HomeViewModel`).
  - ViewModels are **manually constructed** in the module (e.g. `SplashViewModel(appDatabase, retrofit, okHttpClient, appPreferences, context)`) and provided via `getViewModel { ... }` in the activity/fragment.
- **Scopes:** Custom scopes like `@SplashActivityScope`, `@HomeFragmentScope` tie providers to the activity/fragment lifecycle.
- **Conclusion:** **Dagger 2, component-per-activity/fragment**, with Core as the shared dependency. New screen = new Component + Module depending on `CoreComponent`, provide ViewModel in the module, inject in `onCreate`/`onAttach` and call `getViewModel { ViewModel(...) }`.

---

## 10. Result wrappers / API response models

- **No common Result/Resource/ApiResponse wrapper.** Login flow uses raw `JSONObject` in LiveData (`MutableLiveData<JSONObject>`, `loginResult`). Success/failure encoded inside the JSON (e.g. `put("success", false)`).
- **Core:** `APIServices` uses a mix of `Call<*>`, `Call<JsonObject>`, `Call<JSONObject>`, and `Observable<JSONObject>`. No shared response envelope or error type.
- **Conclusion:** **No standard result wrapper.** Each feature can use different types (JSONObject, JsonObject, future DTOs). Introducing a common `Result<T>` or `ApiResponse<T>` would be a new convention.

---

## 11. Existing push notification setup

- **FCM:** Integrated via **firebaseSDK** module. `FirebaseNotificationManager` singleton: init from Application, token handling, topic subscription, callbacks for foreground/background/data messages, notification click, token received, errors. `CustomFirebaseMessagingService` extends `FirebaseMessagingService` and is declared in app manifest (with `remoteMessaging` foregroundServiceType).
- **App integration:** `AppApplication.onCreate()` calls `FirebaseNotificationManager.getInstance().initialize(applicationContext, callback)`. Callback implements: `onMessageReceived`, `onBackgroundMessageReceived`, `onDataMessageReceived`, `onNewToken`, `onNotificationAction`, `onNotificationClicked`, `onTokenReceived`, `onError`. `sendPushToken("", token)` is stubbed.
- **Channels:** `NotificationChannelManager` exists in firebaseSDK; no app-level channel list was read. Notification payload handling and routing on click are placeholder (e.g. “Handle notification body clicks, potentially navigate the user”).
- **Conclusion:** **FCM is set up** (token, messages, service, app callback). Missing: sending token to backend, notification channels configuration in Application, and **deep-link / in-app navigation** from notification payloads (e.g. by `type` and ids). Phase 5 can build on this.

---

## 12. Existing socket/realtime infra

- **No Socket/WebSocket or realtime layer.** No `Socket`, `WebSocket`, `socket.io`, or “realtime” usage found in app or core.
- **Conclusion:** **None.** Phase 6 would add a new SocketManager and wiring (e.g. JWT, rooms, events) if required.

---

# Project Skill Rules (definition for future work)

## When to use Activity vs Fragment

- **Activity:** Use for **full-screen flows** that are entry points or standalone (Splash, Login, Signup, Forgot Password, Language selection, Login/Signup chooser, Landing container). Each has its own window and (usually) its own Dagger component.
- **Fragment:** Use for **tabs or content inside a single container** (e.g. LandingMainActivity). Current tabs: Home, Search, Center, Notifications, Profile. Use Fragment when the screen is part of bottom navigation or a master container; use Activity when it’s a separate flow (auth, onboarding, settings, etc.).

## How ViewModels are scoped

- **Scope:** ViewModels are **scoped to the Activity or Fragment** that owns them. Each screen has a Dagger component (e.g. `SplashActivityComponent`, `HomeFragmentComponent`) with a custom scope (`@SplashActivityScope`, `@HomeFragmentScope`). The module provides the ViewModel; the Activity/Fragment gets it via `getViewModel { XxxViewModel(...) }`, so lifecycle is tied to that activity or fragment.
- **Creation:** ViewModels are **not** created by a generic ViewModelProvider factory from Dagger; they are **explicitly constructed** in the module (with CoreComponent deps) and then provided via the existing `getViewModel` factory. New screens: add a Module that provides the ViewModel the same way.

## How APIs are added

- **Define interface:** Create a Retrofit interface (e.g. in `activity.<feature>.api` or a shared `api` package) with `@GET`/`@POST` and relative paths (or full `@Url` if needed).
- **Use existing Retrofit:** Do **not** create a new Retrofit instance. Get `Retrofit` from Dagger (CoreComponent → Activity/Fragment component). In the ViewModel, call `retrofit.create(YourApiService::class.java)` and use the returned service.
- **Base URL:** Today it’s set once in core `NetworkModule`. For Villa Society, either change that base or introduce a second Retrofit (or base URL) for the new API and inject it the same way.
- **Response handling:** Currently no standard wrapper; can use `Call<T>`, `LiveData<T>`, or later a `Result<T>`/`ApiResponse<T>` once defined.

## How navigation is extended

- **New Activity:** Declare in `AndroidManifest.xml`. Start it with `startActivity(Intent(this, NewActivity::class.java))` (and optionally `finish()` or flags). Add a Dagger component + module for the new activity and inject in `onCreate`.
- **New Fragment (e.g. new tab):** Create the fragment and its component/module. In `LandingMainActivity.setupBottomNavigation()`, add a new `when` branch for the new menu item and call `switchFragment(NewFragment())`. Ensure the fragment’s layout is included in the landing flow as needed.
- **Deep links / FCM:** Not implemented yet. Extension point: in FCM callback (e.g. `onNotificationClicked` or `onDataMessageReceived`), parse payload (e.g. `type`, `visitor_id`, `villa_id`) and call `startActivity` with the right Intent (and flags) to the target Activity or Fragment. Navigation component or a small “Router” could be added later to centralize this.

---

**End of Phase 1 analysis. No feature code generated.**
