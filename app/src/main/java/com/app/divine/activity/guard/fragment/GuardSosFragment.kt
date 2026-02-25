package com.app.divine.activity.guard.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.app.divine.databinding.FragmentGuardSosBinding

/** GuardGraph: SOS tab. */
class GuardSosFragment : Fragment() {
    private lateinit var binding: FragmentGuardSosBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentGuardSosBinding.inflate(inflater, container, false)
        return binding.root
    }
}
