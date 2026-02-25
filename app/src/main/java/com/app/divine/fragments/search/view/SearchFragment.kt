package com.app.divine.fragments.search.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.app.divine.databinding.SearchFragmentBinding
import com.app.core.dagger.preference.AppPreferences
import com.app.core.dagger.roomdatabase.AppDatabase
import com.app.core.extensions.ui.coreComponent
import com.app.divine.fragments.search.di.DaggerSearchFragmentComponent
import com.app.divine.fragments.search.di.SearchFragmentModule
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import javax.inject.Inject

class SearchFragment : Fragment() {

    private lateinit var binding: SearchFragmentBinding

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
        binding = SearchFragmentBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        DaggerSearchFragmentComponent.builder()
            .coreComponent(coreComponent())
            .searchFragmentModule(SearchFragmentModule(this))
            .build()
            .inject(this)
    }
} 