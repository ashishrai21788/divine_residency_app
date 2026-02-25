package com.app.divine.activity.guard.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.app.divine.databinding.FragmentGuardDeliveriesBinding

/** GuardGraph: Deliveries tab. */
class GuardDeliveriesFragment : Fragment() {
    private lateinit var binding: FragmentGuardDeliveriesBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentGuardDeliveriesBinding.inflate(inflater, container, false)
        return binding.root
    }
}
