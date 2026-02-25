package com.app.divine.fragments.notifications.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.app.divine.databinding.NotificationFragmentBinding
import com.app.core.dagger.preference.AppPreferences
import com.app.core.dagger.roomdatabase.AppDatabase
import com.app.core.extensions.ui.coreComponent
import com.app.divine.fragments.notifications.di.DaggerNotificationsFragmentComponent
import com.app.divine.fragments.notifications.di.NotificationsFragmentModule
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import javax.inject.Inject

class NotificationsFragment : Fragment() {

    private lateinit var  binding: NotificationFragmentBinding
    //
    @Inject
    lateinit var appDatabase: AppDatabase

    @Inject
    lateinit var retrofit: Retrofit

    @Inject
    lateinit var okHttpClient: OkHttpClient

    @Inject
    lateinit var appPreferences: AppPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = NotificationFragmentBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        DaggerNotificationsFragmentComponent.builder()
            .coreComponent(coreComponent())
            .notificationsFragmentModule(NotificationsFragmentModule(this))
            .build()
            .inject(this)
    }
} 