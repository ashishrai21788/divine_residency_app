package com.app.divine.activity.guard.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.app.divine.databinding.FragmentGuardDashboardBinding

/** GuardGraph: Dashboard tab. */
class GuardDashboardFragment : Fragment() {
    private lateinit var binding: FragmentGuardDashboardBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentGuardDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }
}
