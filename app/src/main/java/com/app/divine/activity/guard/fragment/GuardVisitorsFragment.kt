package com.app.divine.activity.guard.fragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.divine.api.ApiResult
import com.app.divine.api.dataOrNull
import com.app.divine.api.dto.VillaVisitorLogDto
import com.app.divine.api.dto.VillaVisitorRegisterRequest
import com.app.divine.databinding.FragmentGuardVisitorsBinding
import com.app.divine.databinding.ItemVisitorLogBinding
import com.app.core.dagger.preference.AppPreferences
import com.app.core.extensions.ui.coreComponent
import com.app.divine.sync.GuardVisitorSyncWorker
import com.app.divine.villa.RegisterVisitorOutcome
import com.app.divine.ui.common.StatusChipHelper
import com.app.divine.ui.common.bindEmptyState
import com.app.divine.villa.VillaViewModelFactory
import com.app.divine.villa.viewmodel.VillaVisitorsViewModel
import com.google.android.material.snackbar.Snackbar

class GuardVisitorsFragment : Fragment() {

    private lateinit var binding: FragmentGuardVisitorsBinding
    private lateinit var viewModel: VillaVisitorsViewModel
    private var societyId: String = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: android.os.Bundle?): View {
        binding = FragmentGuardVisitorsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: android.os.Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        societyId = coreComponent().appPreferences().getAllPreferenceTypeString(AppPreferences.VILLA_SOCIETY_ID_KEY)
        viewModel = ViewModelProvider(this, VillaViewModelFactory(requireContext()))[VillaVisitorsViewModel::class.java]

        binding.guardVisitorsTabs.addTab(binding.guardVisitorsTabs.newTab().setText("Add Visitor"))
        binding.guardVisitorsTabs.addTab(binding.guardVisitorsTabs.newTab().setText("Visitor Logs"))

        binding.guardVisitorsTabs.addOnTabSelectedListener(object : com.google.android.material.tabs.TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: com.google.android.material.tabs.TabLayout.Tab?) {
                val isForm = (tab?.position ?: 0) == 0
                binding.guardAddVisitorForm.visibility = if (isForm) View.VISIBLE else View.GONE
                binding.guardVisitorLogsSwipe.visibility = if (isForm) View.GONE else View.VISIBLE
                if (!isForm) loadLogs()
            }
            override fun onTabUnselected(tab: com.google.android.material.tabs.TabLayout.Tab?) {}
            override fun onTabReselected(tab: com.google.android.material.tabs.TabLayout.Tab?) {}
        })

        binding.addVisitorSubmit.setOnClickListener { submitVisitor() }
        binding.guardSyncNowButton.setOnClickListener { GuardVisitorSyncWorker.enqueue(requireContext()) }
        binding.guardVisitorLogsSwipe.setOnRefreshListener { loadLogs() }

        viewModel.offlinePendingLiveData().observe(viewLifecycleOwner) { count ->
            val n = count ?: 0
            if (n > 0) {
                binding.guardOfflineQueueRow.visibility = View.VISIBLE
                binding.guardOfflineQueueBanner.text =
                    getString(com.app.divine.R.string.guard_offline_queue_fmt, n)
            } else {
                binding.guardOfflineQueueRow.visibility = View.GONE
            }
        }

        viewModel.registerOutcome.observe(viewLifecycleOwner) { outcome ->
            when (outcome) {
                is RegisterVisitorOutcome.RemoteSuccess -> {
                    Snackbar.make(binding.root, "Visitor registered", Snackbar.LENGTH_SHORT).show()
                    binding.addVisitorName.text?.clear()
                    binding.addVisitorPhone.text?.clear()
                    binding.addVisitorVilla.text?.clear()
                    binding.addVisitorFloor.text?.clear()
                    binding.addVisitorPurpose.text?.clear()
                    binding.addVisitorVehicle.text?.clear()
                    viewModel.consumeRegisterOutcome()
                }
                is RegisterVisitorOutcome.QueuedOffline -> {
                    Snackbar.make(
                        binding.root,
                        "Saved offline — will sync when online",
                        Snackbar.LENGTH_LONG
                    ).show()
                    binding.addVisitorName.text?.clear()
                    binding.addVisitorPhone.text?.clear()
                    binding.addVisitorVilla.text?.clear()
                    binding.addVisitorFloor.text?.clear()
                    binding.addVisitorPurpose.text?.clear()
                    binding.addVisitorVehicle.text?.clear()
                    viewModel.consumeRegisterOutcome()
                }
                is RegisterVisitorOutcome.Failed -> {
                    Snackbar.make(binding.root, outcome.message, Snackbar.LENGTH_SHORT).show()
                    viewModel.consumeRegisterOutcome()
                }
                else -> {}
            }
        }
        viewModel.logsResult.observe(viewLifecycleOwner) { result ->
            binding.guardVisitorLogsSwipe.isRefreshing = false
            when (result) {
                is ApiResult.Success -> {
                    val list = result.dataOrNull().orEmpty()
                    if (list.isEmpty()) {
                        binding.guardVisitorLogsEmpty.visibility = View.VISIBLE
                        binding.guardVisitorLogsEmpty.bindEmptyState("No visitor logs")
                        binding.guardVisitorLogsList.adapter = null
                    } else {
                        binding.guardVisitorLogsEmpty.visibility = View.GONE
                        binding.guardVisitorLogsList.layoutManager = LinearLayoutManager(requireContext())
                        binding.guardVisitorLogsList.adapter = GuardVisitorLogAdapter(list)
                    }
                }
                else -> {}
            }
        }
    }

    private fun submitVisitor() {
        val name = binding.addVisitorName.text?.toString()?.trim() ?: ""
        val phone = binding.addVisitorPhone.text?.toString()?.trim() ?: ""
        val villa = binding.addVisitorVilla.text?.toString()?.trim() ?: societyId
        val floor = binding.addVisitorFloor.text?.toString()?.trim() ?: societyId
        val purpose = binding.addVisitorPurpose.text?.toString()?.trim()
        val vehicle = binding.addVisitorVehicle.text?.toString()?.trim()
        if (name.isBlank()) {
            binding.addVisitorName.error = "Required"
            return
        }
        if (phone.isBlank()) {
            binding.addVisitorPhone.error = "Required"
            return
        }
        binding.addVisitorName.error = null
        binding.addVisitorPhone.error = null
        val request = VillaVisitorRegisterRequest(
            name = name,
            phone = phone,
            photoUrl = null,
            villaId = villa.ifBlank { societyId },
            floorId = floor.ifBlank { societyId },
            gateId = societyId.ifBlank { "gate1" },
            purpose = purpose?.takeIf { it.isNotBlank() },
            vehicleNumber = vehicle?.takeIf { it.isNotBlank() }
        )
        viewModel.registerVisitorOrQueue(request)
    }

    private fun loadLogs() {
        if (societyId.isEmpty()) return
        viewModel.loadVisitorLogs(societyId, null, null)
    }

    private class GuardVisitorLogAdapter(private val items: List<VillaVisitorLogDto>) : RecyclerView.Adapter<GuardVisitorLogAdapter.Holder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
            val b = ItemVisitorLogBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return Holder(b)
        }
        override fun onBindViewHolder(holder: Holder, position: Int) = holder.bind(items[position])
        override fun getItemCount() = items.size

        class Holder(private val b: ItemVisitorLogBinding) : RecyclerView.ViewHolder(b.root) {
            fun bind(log: VillaVisitorLogDto) {
                b.logName.text = log.name ?: "Visitor"
                b.logAvatarInitial.text =
                    (log.name ?: "V").trim().firstOrNull()?.uppercaseChar()?.toString() ?: "V"
                b.logPurposeTime.text = "${log.purpose ?: ""} • ${log.createdAt ?: ""}"
                StatusChipHelper.apply(b.logStatus, log.status ?: "PENDING")
                b.logApprove.visibility = View.GONE
                b.logReject.visibility = View.GONE
            }
        }
    }
}
