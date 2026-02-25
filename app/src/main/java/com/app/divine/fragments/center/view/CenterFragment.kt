package com.app.divine.fragments.center.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.app.divine.databinding.CenterFragmentBinding
import com.app.core.dagger.preference.AppPreferences
import com.app.core.dagger.roomdatabase.AppDatabase
import com.app.core.extensions.ui.coreComponent
import com.app.divine.fragments.center.di.CenterFragmentModule
import com.app.divine.fragments.center.di.DaggerCenterFragmentComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import javax.inject.Inject

class CenterFragment : Fragment() {

    private lateinit var binding: CenterFragmentBinding

    //
    @Inject
    lateinit var appDatabase: AppDatabase

    @Inject
    @field:com.app.core.dagger.qualifier.DefaultRetrofit
    lateinit var retrofit: Retrofit

    @Inject
    lateinit var okHttpClient: OkHttpClient

    @Inject
    lateinit var appPreferences: AppPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = CenterFragmentBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        DaggerCenterFragmentComponent.builder()
            .coreComponent(coreComponent())
            .centerFragmentModule(CenterFragmentModule(this))
            .build()
            .inject(this)
    }
} 