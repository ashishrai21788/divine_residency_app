package com.app.divine.fragments.resident

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.divine.api.ApiResult
import com.app.divine.api.dataOrNull
import com.app.divine.api.dto.VillaVisitorLogDto
import com.app.divine.databinding.FragmentResidentVisitorsBinding
import com.app.divine.databinding.ItemVisitorLogBinding
import com.app.core.dagger.preference.AppPreferences
import com.app.core.extensions.ui.coreComponent
import com.app.core.R as CoreR
import com.app.divine.realtime.RealtimeRefresh
import com.app.divine.realtime.VillaSocketEvent
import com.app.divine.ui.common.StatusChipHelper
import com.app.divine.ui.common.bindEmptyState
import com.app.divine.villa.VillaViewModelFactory
import com.app.divine.villa.viewmodel.VillaVisitorsViewModel
import com.google.android.material.snackbar.Snackbar

class ResidentVisitorsFragment : Fragment(), RealtimeRefresh {

    private lateinit var binding: FragmentResidentVisitorsBinding
    private lateinit var appPreferences: AppPreferences
    private lateinit var visitorsViewModel: VillaVisitorsViewModel
    private var currentTab = 0 // 0 Pending, 1 History
    private var societyId: String = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentResidentVisitorsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        appPreferences = coreComponent().appPreferences()
        societyId = appPreferences.getAllPreferenceTypeString(AppPreferences.VILLA_SOCIETY_ID_KEY)
        visitorsViewModel = ViewModelProvider(this, VillaViewModelFactory(requireContext()))[VillaVisitorsViewModel::class.java]

        binding.visitorsTabs.addTab(binding.visitorsTabs.newTab().setText("Pending"))
        binding.visitorsTabs.addTab(binding.visitorsTabs.newTab().setText("History"))
        binding.visitorsList.layoutManager = LinearLayoutManager(requireContext())

        binding.visitorsTabs.addOnTabSelectedListener(object : com.google.android.material.tabs.TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: com.google.android.material.tabs.TabLayout.Tab?) {
                currentTab = tab?.position ?: 0
                load()
            }
            override fun onTabUnselected(tab: com.google.android.material.tabs.TabLayout.Tab?) {}
            override fun onTabReselected(tab: com.google.android.material.tabs.TabLayout.Tab?) {}
        })
        binding.visitorsSwipe.setOnRefreshListener { load() }
        binding.visitorsError.findViewById<Button>(com.app.divine.R.id.error_retry)?.setOnClickListener { load() }

        observeLogs()
        load()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    private fun load() {
        if (societyId.isEmpty()) {
            showError("Not logged in")
            return
        }
        binding.visitorsLoading.visibility = View.VISIBLE
        binding.visitorsEmpty.visibility = View.GONE
        binding.visitorsError.visibility = View.GONE
        val status = if (currentTab == 0) "pending" else null
        visitorsViewModel.loadVisitorLogs(societyId, null, status)
    }

    private fun observeLogs() {
        visitorsViewModel.logsResult.observe(viewLifecycleOwner) { result ->
            binding.visitorsSwipe.isRefreshing = false
            binding.visitorsLoading.visibility = View.GONE
            when (result) {
                is ApiResult.Loading -> binding.visitorsLoading.visibility = View.VISIBLE
                is ApiResult.Success -> {
                    val list = result.dataOrNull().orEmpty()
                    binding.visitorsError.visibility = View.GONE
                    if (list.isEmpty()) {
                        binding.visitorsEmpty.visibility = View.VISIBLE
                        binding.visitorsEmpty.bindEmptyState(
                            if (currentTab == 0) "No pending visitors" else "No history yet"
                        )
                        binding.visitorsList.adapter = null
                    } else {
                        binding.visitorsEmpty.visibility = View.GONE
                        binding.visitorsList.adapter = VisitorLogAdapter(list, currentTab == 0,
                            onApprove = { id -> showConfirm(id, true) },
                            onReject = { id -> showConfirm(id, false) })
                    }
                }
                is ApiResult.Error -> {
                    binding.visitorsEmpty.visibility = View.GONE
                    binding.visitorsError.visibility = View.VISIBLE
                    binding.visitorsError.findViewById<TextView>(com.app.divine.R.id.error_message)?.text = result.message
                }
            }
        }
        visitorsViewModel.actionResult.observe(viewLifecycleOwner) { result ->
            if (result is ApiResult.Success) Snackbar.make(binding.root, "Done", Snackbar.LENGTH_SHORT).show()
            if (result is ApiResult.Error) Snackbar.make(binding.root, result.message, Snackbar.LENGTH_SHORT).show()
            load()
        }
    }

    private fun showConfirm(visitorId: String, approve: Boolean) {
        AlertDialog.Builder(requireContext(), CoreR.style.AppCompatAlertDialogStyle)
            .setTitle(if (approve) "Approve visitor?" else "Reject visitor?")
            .setPositiveButton(if (approve) "Approve" else "Reject") { _, _ ->
                if (approve) visitorsViewModel.approveVisitor(visitorId)
                else visitorsViewModel.rejectVisitor(visitorId)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun showError(msg: String) {
        binding.visitorsLoading.visibility = View.GONE
        binding.visitorsEmpty.visibility = View.GONE
        binding.visitorsError.visibility = View.VISIBLE
        binding.visitorsError.findViewById<TextView>(com.app.divine.R.id.error_message)?.text = msg
    }

    override fun onRealtimeEvent(event: VillaSocketEvent) {
        if (event.event == "visitor_request" || event.event == "visitor_status_update") load()
    }

    private class VisitorLogAdapter(
        private val items: List<VillaVisitorLogDto>,
        private val showActions: Boolean,
        private val onApprove: (String) -> Unit,
        private val onReject: (String) -> Unit
    ) : RecyclerView.Adapter<VisitorLogAdapter.Holder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
            val b = ItemVisitorLogBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return Holder(b, showActions, onApprove, onReject)
        }
        override fun onBindViewHolder(holder: Holder, position: Int) = holder.bind(items[position])
        override fun getItemCount(): Int = items.size

        class Holder(
            private val b: ItemVisitorLogBinding,
            private val showActions: Boolean,
            private val onApprove: (String) -> Unit,
            private val onReject: (String) -> Unit
        ) : RecyclerView.ViewHolder(b.root) {
            fun bind(log: VillaVisitorLogDto) {
                b.logName.text = log.name ?: "Visitor"
                b.logAvatarInitial.text =
                    (log.name ?: "V").trim().firstOrNull()?.uppercaseChar()?.toString() ?: "V"
                b.logPurposeTime.text = "${log.purpose ?: ""} • ${log.createdAt ?: ""}"
                StatusChipHelper.apply(b.logStatus, log.status ?: "PENDING")
                b.logApprove.visibility = if (showActions && log.status?.uppercase() == "PENDING") View.VISIBLE else View.GONE
                b.logReject.visibility = b.logApprove.visibility
                log.id?.let { id ->
                    b.logApprove.setOnClickListener { onApprove(id) }
                    b.logReject.setOnClickListener { onReject(id) }
                }
            }
        }
    }
}
