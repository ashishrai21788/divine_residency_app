package com.app.divine.activity.landing.view

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.notification.FirebaseNotificationManager
import androidx.fragment.app.Fragment
import com.app.divine.R
import com.app.divine.databinding.ActivityLandingMainBinding
import com.app.core.dagger.preference.AppPreferences
import com.app.core.dagger.roomdatabase.AppDatabase
import com.app.divine.AppApplication
import com.app.divine.activity.landing.di.DaggerLandingMainActivityComponent
import com.app.divine.activity.landing.di.LandingMainActivityModule
import com.app.divine.fragments.home.view.HomeFragment
import com.app.divine.fragments.profile.view.ProfileFragment
import com.app.divine.fragments.resident.ResidentBillingFragment
import com.app.divine.fragments.resident.ResidentComplaintsFragment
import com.app.divine.fragments.resident.ResidentVisitorsFragment
import com.app.divine.realtime.RealtimeRefresh
import com.app.divine.realtime.VillaSocketEvent
import com.app.divine.realtime.VillaSocketManager
import com.app.divine.realtime.asRealtimeRefresh
import com.app.core.dagger.qualifier.DefaultRetrofit
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import javax.inject.Inject

class LandingMainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLandingMainBinding
//
    @Inject
    lateinit var appDatabase: AppDatabase

    @Inject
    @field:DefaultRetrofit
    lateinit var retrofit: Retrofit

    @Inject
    lateinit var okHttpClient: OkHttpClient

    @Inject
    lateinit var appPreferences: AppPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLandingMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        DaggerLandingMainActivityComponent.builder()
            .coreComponent((application as AppApplication).coreComponent)
            .landingMainActivityModule(LandingMainActivityModule(this))
            .build()
            .inject(this)

        setupResidentBottomNavigation()
        if (savedInstanceState == null) {
            switchFragment(HomeFragment())
        }
        handleNotificationIntent(intent)
        connectRealtimeSocket()
    }


    override fun onDestroy() {
        (application as? AppApplication)?.getVillaSocketManager()?.disconnect()
        super.onDestroy()
    }

    private fun connectRealtimeSocket() {
        if (!appPreferences.getLogin()) return
        val socketManager = (application as? AppApplication)?.getVillaSocketManager() ?: return
        socketManager.connect(com.app.divine.config.VillaSocietyConfig.SOCKET_BASE_URL)
        socketManager.socketEventLiveData.observe(this) { event ->
            if (event != null && socketManager.isResidentEvent(event.event)) {
                onRealtimeEvent(event)
            }
        }
    }

    /** Dispatches to current fragment if it implements [RealtimeRefresh] (visitor_request, visitor_status_update, sos_triggered). */
    protected fun onRealtimeEvent(event: VillaSocketEvent) {
        supportFragmentManager.findFragmentById(R.id.fragment_container)?.asRealtimeRefresh()?.onRealtimeEvent(event)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleNotificationIntent(intent)
    }

    private fun handleNotificationIntent(intent: Intent?) {
        if (intent?.action == "come.notification.ACTION_NOTIFICATION_CLICK") {
            FirebaseNotificationManager.getInstance().handleIntent(this, intent)
        }
    }

    /** ResidentGraph: Dashboard, Visitors, Billing, Complaints, Profile (bottom nav). */
    private fun setupResidentBottomNavigation() {
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menu_resident_dashboard -> switchFragment(HomeFragment())
                R.id.menu_resident_visitors -> switchFragment(ResidentVisitorsFragment())
                R.id.menu_resident_billing -> switchFragment(ResidentBillingFragment())
                R.id.menu_resident_complaints -> switchFragment(ResidentComplaintsFragment())
                R.id.menu_resident_profile -> switchFragment(ProfileFragment())
            }
            true
        }
    }

    private fun switchFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
} 