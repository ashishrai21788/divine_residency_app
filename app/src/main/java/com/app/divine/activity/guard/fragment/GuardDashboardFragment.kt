package com.app.divine.activity.guard.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.app.divine.R
import com.app.divine.activity.guard.view.GuardMainActivity
import com.app.divine.databinding.FragmentGuardDashboardBinding

/** GuardGraph: Dashboard tab — grid tiles for Add Visitor, Visitor Logs, Deliveries, SOS, Attendance. */
class GuardDashboardFragment : Fragment() {

    private lateinit var binding: FragmentGuardDashboardBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentGuardDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val activity = activity as? GuardMainActivity ?: return
        binding.tileAddVisitor.setOnClickListener { activity.selectGuardTab(R.id.menu_guard_visitors) }
        binding.tileVisitorLogs.setOnClickListener { activity.selectGuardTab(R.id.menu_guard_visitors) }
        binding.tileDeliveries.setOnClickListener { activity.selectGuardTab(R.id.menu_guard_deliveries) }
        binding.tileSos.setOnClickListener { activity.selectGuardTab(R.id.menu_guard_sos) }
        binding.tileAttendance.setOnClickListener { activity.selectGuardTab(R.id.menu_guard_attendance) }
    }
}
