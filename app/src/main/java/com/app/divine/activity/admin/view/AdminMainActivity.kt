package com.app.divine.activity.admin.view

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.app.divine.R
import com.app.divine.activity.admin.fragment.AdminHomeFragment
import com.app.divine.activity.admin.fragment.AdminNoticesFragment
import com.app.divine.activity.admin.fragment.AdminVisitorLogsFragment
import com.app.divine.databinding.ActivityAdminMainBinding
import com.app.divine.navigation.DeepLinkRouter
import com.app.divine.fragments.profile.view.ProfileFragment
import com.app.divine.fragments.resident.ResidentComplaintsFragment
import com.notification.FirebaseNotificationManager

/**
 * Admin / RWA shell: overview, society visitor logs, notices, complaints, profile.
 */
class AdminMainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdminMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupBottomNavigation()
        if (savedInstanceState == null) {
            switchFragment(AdminHomeFragment())
        }
        handleNotificationIntent(intent)
        applyDeepLinkFromIntent(intent)
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
        val tab = DeepLinkRouter.adminTabForNotificationType(type) ?: return
        binding.bottomNavigation.selectedItemId = tab
        supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        when (tab) {
            R.id.menu_admin_overview -> switchFragment(AdminHomeFragment())
            R.id.menu_admin_logs -> switchFragment(AdminVisitorLogsFragment())
            R.id.menu_admin_notices -> switchFragment(AdminNoticesFragment())
            R.id.menu_admin_complaints -> switchFragment(ResidentComplaintsFragment())
            R.id.menu_admin_profile -> switchFragment(ProfileFragment())
        }
    }

    private fun setupBottomNavigation() {
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
            when (item.itemId) {
                R.id.menu_admin_overview -> switchFragment(AdminHomeFragment())
                R.id.menu_admin_logs -> switchFragment(AdminVisitorLogsFragment())
                R.id.menu_admin_notices -> switchFragment(AdminNoticesFragment())
                R.id.menu_admin_complaints -> switchFragment(ResidentComplaintsFragment())
                R.id.menu_admin_profile -> switchFragment(ProfileFragment())
            }
            true
        }
    }

    fun switchFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }

    fun pushFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }
}
