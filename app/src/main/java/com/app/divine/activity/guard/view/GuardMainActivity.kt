package com.app.divine.activity.guard.view

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.notification.FirebaseNotificationManager
import androidx.fragment.app.Fragment
import com.app.divine.R
import com.app.divine.activity.guard.fragment.GuardAttendanceFragment
import com.app.divine.activity.guard.fragment.GuardDeliveriesFragment
import com.app.divine.activity.guard.fragment.GuardDashboardFragment
import com.app.divine.activity.guard.fragment.GuardSosFragment
import com.app.divine.activity.guard.fragment.GuardVisitorsFragment
import com.app.divine.databinding.ActivityGuardMainBinding
import com.app.divine.navigation.DeepLinkRouter
import com.app.divine.realtime.RealtimeRefresh
import com.app.divine.realtime.VillaSocketEvent
import com.app.divine.realtime.VillaSocketManager
import com.app.divine.realtime.asRealtimeRefresh

/**
 * GuardGraph container: Dashboard, Add Visitor, Visitor Logs, Deliveries, SOS, Attendance.
 * Uses existing navigation pattern (FragmentTransaction.replace + bottom nav).
 */
class GuardMainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGuardMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGuardMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupBottomNavigation()
        if (savedInstanceState == null) {
            switchFragment(GuardDashboardFragment())
        }
        handleNotificationIntent(intent)
        applyDeepLinkFromIntent(intent)
        connectRealtimeSocket()
    }


    override fun onDestroy() {
        (application as? com.app.divine.AppApplication)?.getVillaSocketManager()?.disconnect()
        super.onDestroy()
    }

    private fun connectRealtimeSocket() {
        val app = application as? com.app.divine.AppApplication ?: return
        if (!app.coreComponent.appPreferences().getLogin()) return
        val socketManager = app.getVillaSocketManager()
        socketManager.connect(com.app.core.config.VillaSocietyConfig.SOCKET_BASE_URL)
        socketManager.socketEventLiveData.observe(this) { event ->
            if (event != null && socketManager.isGuardEvent(event.event)) {
                onRealtimeEvent(event)
            }
        }
    }

    /** Dispatches to current fragment if it implements [RealtimeRefresh] (sos_triggered, visitor_status_update). */
    protected fun onRealtimeEvent(event: VillaSocketEvent) {
        supportFragmentManager.findFragmentById(R.id.fragment_container)?.asRealtimeRefresh()?.onRealtimeEvent(event)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        handleNotificationIntent(intent)
        applyDeepLinkFromIntent(intent)
    }

    private fun handleNotificationIntent(intent: Intent?) {
        if (intent?.action == "come.notification.ACTION_NOTIFICATION_CLICK") {
            FirebaseNotificationManager.getInstance().handleIntent(this, intent)
        }
    }

    private fun applyDeepLinkFromIntent(intent: Intent?) {
        val type = intent?.getStringExtra(DeepLinkRouter.EXTRA_DEEP_LINK_TYPE) ?: return
        val tab = DeepLinkRouter.guardTabForNotificationType(type) ?: return
        binding.bottomNavigation.selectedItemId = tab
        when (tab) {
            R.id.menu_guard_dashboard -> switchFragment(GuardDashboardFragment())
            R.id.menu_guard_visitors -> switchFragment(GuardVisitorsFragment())
            R.id.menu_guard_deliveries -> switchFragment(GuardDeliveriesFragment())
            R.id.menu_guard_sos -> switchFragment(GuardSosFragment())
            R.id.menu_guard_attendance -> switchFragment(GuardAttendanceFragment())
        }
    }

    private fun setupBottomNavigation() {
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menu_guard_dashboard -> switchFragment(GuardDashboardFragment())
                R.id.menu_guard_visitors -> switchFragment(GuardVisitorsFragment())
                R.id.menu_guard_deliveries -> switchFragment(GuardDeliveriesFragment())
                R.id.menu_guard_sos -> switchFragment(GuardSosFragment())
                R.id.menu_guard_attendance -> switchFragment(GuardAttendanceFragment())
            }
            true
        }
    }

    fun selectGuardTab(menuItemId: Int) {
        binding.bottomNavigation.selectedItemId = menuItemId
    }

    private fun switchFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
}
