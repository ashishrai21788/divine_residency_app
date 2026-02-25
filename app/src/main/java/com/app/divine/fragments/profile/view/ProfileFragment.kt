package com.app.divine.fragments.profile.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.app.divine.databinding.ProfileFragmentBinding
import com.app.core.dagger.preference.AppPreferences
import com.app.core.dagger.roomdatabase.AppDatabase
import com.app.core.extensions.ui.coreComponent
import com.app.divine.fragments.profile.di.DaggerProfileFragmentComponent
import com.app.divine.fragments.profile.di.ProfileFragmentModule
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import javax.inject.Inject

class ProfileFragment : Fragment() {

    private lateinit var  binding: ProfileFragmentBinding
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
        binding = ProfileFragmentBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        DaggerProfileFragmentComponent.builder()
            .coreComponent(coreComponent())
            .profileFragmentModule(ProfileFragmentModule(this))
            .build()
            .inject(this)
    }
} 