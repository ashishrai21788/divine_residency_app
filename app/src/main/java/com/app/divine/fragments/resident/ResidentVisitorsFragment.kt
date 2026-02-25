package com.app.divine.fragments.resident

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.app.divine.databinding.FragmentResidentVisitorsBinding
import com.app.divine.realtime.RealtimeRefresh
import com.app.divine.realtime.VillaSocketEvent

/** ResidentGraph: Visitors tab — approve/reject visitors. Implements [RealtimeRefresh] for visitor_request, visitor_status_update. */
class ResidentVisitorsFragment : Fragment(), RealtimeRefresh {
    private lateinit var binding: FragmentResidentVisitorsBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentResidentVisitorsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onRealtimeEvent(event: VillaSocketEvent) {
        when (event.event) {
            "visitor_request", "visitor_status_update" -> {
                // Refresh visitor list: get ViewModel and call loadVisitorLogs(societyId) when fragment has DI
            }
            else -> {}
        }
    }
}
