package com.app.divine.activity.guard.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.app.divine.databinding.FragmentGuardVisitorsBinding

/** GuardGraph: Add Visitor / Visitor Logs tab. */
class GuardVisitorsFragment : Fragment() {
    private lateinit var binding: FragmentGuardVisitorsBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentGuardVisitorsBinding.inflate(inflater, container, false)
        return binding.root
    }
}
