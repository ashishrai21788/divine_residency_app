package com.app.divine.activity.guard.fragment

import android.view.View
import androidx.fragment.app.Fragment
import com.app.divine.databinding.FragmentGuardSosBinding
import com.app.divine.ui.common.bindEmptyState

/** GuardGraph: SOS tab — list of active SOS alerts (placeholder until backend list API). */
class GuardSosFragment : Fragment() {

    private lateinit var binding: FragmentGuardSosBinding

    override fun onCreateView(inflater: android.view.LayoutInflater, container: android.view.ViewGroup?, savedInstanceState: android.os.Bundle?): View {
        binding = FragmentGuardSosBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: android.os.Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.sosSwipe.setOnRefreshListener { binding.sosSwipe.isRefreshing = false }
        binding.sosEmpty.visibility = View.VISIBLE
        binding.sosEmpty.bindEmptyState(
            "No active SOS alerts",
            "Alerts will appear here when residents trigger SOS."
        )
    }
}
