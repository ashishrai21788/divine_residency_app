package com.app.divine.activity.landing.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.app.divine.R
import com.app.divine.databinding.ActivityLandingMainBinding
import com.app.core.dagger.preference.AppPreferences
import com.app.core.dagger.roomdatabase.AppDatabase
import com.app.divine.AppApplication
import com.app.divine.activity.landing.di.DaggerLandingMainActivityComponent
import com.app.divine.activity.landing.di.LandingMainActivityModule
import com.app.divine.fragments.center.view.CenterFragment
import com.app.divine.fragments.home.view.HomeFragment
import com.app.divine.fragments.notifications.view.NotificationsFragment
import com.app.divine.fragments.profile.view.ProfileFragment
import com.app.divine.fragments.search.view.SearchFragment
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import javax.inject.Inject

class LandingMainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLandingMainBinding
//
    @Inject
    lateinit var appDatabase: AppDatabase

    @Inject
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

        setupBottomNavigation()
        if (savedInstanceState == null) {
            switchFragment(HomeFragment())
        }
    }

    private fun setupBottomNavigation() {
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menu_home -> switchFragment(HomeFragment())
                R.id.menu_search -> switchFragment(SearchFragment())
                R.id.menu_center -> switchFragment(CenterFragment())
                R.id.menu_notifications -> switchFragment(NotificationsFragment())
                R.id.menu_profile -> switchFragment(ProfileFragment())
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