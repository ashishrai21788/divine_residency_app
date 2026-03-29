package com.app.divine.fragments.home.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.app.divine.R
import com.app.divine.api.ApiResult
import com.app.divine.api.dataOrNull
import com.app.divine.databinding.HomeFragmentBinding
import com.app.divine.databinding.ItemDeliveryRowBinding
import com.app.divine.ui.common.bindEmptyState
import com.app.divine.ui.common.StatusChipHelper
import com.app.divine.ui.common.VisitorApprovalBottomSheet
import com.app.divine.ui.common.VisitorCardBinder
import com.app.core.dagger.preference.AppPreferences
import com.app.core.extensions.ui.coreComponent
import com.app.divine.fragments.home.di.DaggerHomeFragmentComponent
import com.app.divine.fragments.home.di.HomeFragmentModule
import com.app.divine.villa.VillaViewModelFactory
import com.app.divine.villa.viewmodel.VillaBillingViewModel
import com.app.divine.villa.viewmodel.VillaDeliveriesViewModel
import com.app.divine.villa.viewmodel.VillaSOSViewModel
import com.app.divine.villa.viewmodel.VillaVisitorsViewModel
import com.google.android.material.snackbar.Snackbar
import javax.inject.Inject

class HomeFragment : Fragment() {

    private lateinit var binding: HomeFragmentBinding

    @Inject
    lateinit var appPreferences: AppPreferences

    private lateinit var visitorsViewModel: VillaVisitorsViewModel
    private lateinit var billingViewModel: VillaBillingViewModel
    private lateinit var deliveriesViewModel: VillaDeliveriesViewModel
    private lateinit var sosViewModel: VillaSOSViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = HomeFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val factory = VillaViewModelFactory(requireContext())
        visitorsViewModel = ViewModelProvider(this, factory)[VillaVisitorsViewModel::class.java]
        billingViewModel = ViewModelProvider(this, factory)[VillaBillingViewModel::class.java]
        deliveriesViewModel = ViewModelProvider(this, factory)[VillaDeliveriesViewModel::class.java]
        sosViewModel = ViewModelProvider(this, factory)[VillaSOSViewModel::class.java]

        val societyId = appPreferences.getAllPreferenceTypeString(AppPreferences.VILLA_SOCIETY_ID_KEY)
        binding.homeUserName.text = "Resident"
        binding.homeAvatarInitial.text =
            binding.homeUserName.text.toString().trim().firstOrNull()?.uppercaseChar()?.toString() ?: "R"
        binding.homeVillaFloor.text = if (societyId.isNotEmpty()) "Society • $societyId" else ""

        binding.homeAvatarInitial.setOnClickListener {
            (activity as? com.app.divine.activity.landing.view.LandingMainActivity)?.run {
                supportFragmentManager.beginTransaction()
                    .replace(com.app.divine.R.id.fragment_container, com.app.divine.fragments.profile.view.ProfileFragment())
                    .addToBackStack(null)
                    .commit()
            }
        }

        binding.homeBillViewDetails.setOnClickListener {
            (activity as? com.app.divine.activity.landing.view.LandingMainActivity)?.run {
                supportFragmentManager.beginTransaction()
                    .replace(com.app.divine.R.id.fragment_container, com.app.divine.fragments.resident.ResidentBillingFragment())
                    .addToBackStack(null)
                    .commit()
            }
        }

        binding.homeSosButton.setOnClickListener { showSosConfirmation() }
        binding.homeError.findViewById<android.widget.Button>(com.app.divine.R.id.error_retry)?.setOnClickListener { loadAll(societyId) }

        observeVisitors()
        observeBilling()
        observeDeliveries()
        observeSos()
        loadAll(societyId)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        DaggerHomeFragmentComponent.builder()
            .coreComponent(coreComponent())
            .homeFragmentModule(HomeFragmentModule(this))
            .build()
            .inject(this)
    }

    private fun loadAll(societyId: String) {
        if (societyId.isEmpty()) {
            showError("Not logged in")
            return
        }
        binding.homeLoading.visibility = View.VISIBLE
        binding.homeContent.visibility = View.GONE
        binding.homeError.visibility = View.GONE

        visitorsViewModel.loadVisitorLogs(societyId, null, "pending")
        billingViewModel.loadMyPendingBills(societyId)
        deliveriesViewModel.loadDeliveries(societyId = societyId)
    }

    private fun observeVisitors() {
        visitorsViewModel.logsResult.observe(viewLifecycleOwner) { result ->
            when (result) {
                is ApiResult.Loading -> { /* keep shimmer */ }
                is ApiResult.Success -> {
                    binding.homeLoading.visibility = View.GONE
                    binding.homeContent.visibility = View.VISIBLE
                    binding.homeError.visibility = View.GONE
                    val list = result.dataOrNull().orEmpty()
                    renderPendingVisitors(list)
                }
                is ApiResult.Error -> {
                    binding.homeLoading.visibility = View.GONE
                    binding.homeContent.visibility = View.VISIBLE
                    showError(result.message)
                }
            }
        }
        visitorsViewModel.actionResult.observe(viewLifecycleOwner) { result ->
            if (result is ApiResult.Success) {
                Snackbar.make(binding.root, "Done", Snackbar.LENGTH_SHORT).show()
                appPreferences.getAllPreferenceTypeString(AppPreferences.VILLA_SOCIETY_ID_KEY).let { loadVisitorLogsOnly(it) }
            }
            if (result is ApiResult.Error) Snackbar.make(binding.root, result.message, Snackbar.LENGTH_SHORT).show()
        }
    }

    private fun loadVisitorLogsOnly(societyId: String) {
        visitorsViewModel.loadVisitorLogs(societyId, null, "pending")
    }

    private fun renderPendingVisitors(list: List<com.app.divine.api.dto.VillaVisitorLogDto>) {
        binding.homePendingVisitorsList.removeAllViews()
        binding.homePendingVisitorsEmpty.bindEmptyState("No pending visitors")
        binding.homePendingVisitorsEmpty.visibility = if (list.isEmpty()) View.VISIBLE else View.GONE
        list.forEach { log ->
            val item = VisitorCardBinder.inflateAndBind(
                binding.homePendingVisitorsList,
                log,
                onApprove = { showVisitorApprovalSheet(log, approve = true) },
                onReject = { showVisitorApprovalSheet(log, approve = false) }
            )
            binding.homePendingVisitorsList.addView(item.root)
        }
    }

    private fun showVisitorApprovalSheet(log: com.app.divine.api.dto.VillaVisitorLogDto, approve: Boolean) {
        val visitorId = log.id ?: return
        val detail = buildString {
            log.gateId?.let { append("Gate $it") }
            log.createdAt?.let { append(" • $it") }
        }
        VisitorApprovalBottomSheet.show(
            context = requireContext(),
            visitorName = log.name ?: "Visitor",
            detail = detail,
            approve = approve
        ) {
            if (approve) visitorsViewModel.approveVisitor(visitorId)
            else visitorsViewModel.rejectVisitor(visitorId)
        }
    }

    private fun observeBilling() {
        billingViewModel.pendingBillsResult.observe(viewLifecycleOwner) { result ->
            if (result !is ApiResult.Success) return@observe
            val bills = result.dataOrNull().orEmpty()
            val first = bills.firstOrNull()
            if (first != null) {
                binding.homeBillMonth.text = "Month ${first.month ?: ""} ${first.year ?: ""}"
                binding.homeBillTotal.text = "₹${first.amount ?: 0}"
                binding.homeBillPaid.text = "Paid: —"
                binding.homeBillPending.text = "Pending: ₹${first.amount ?: 0}"
                StatusChipHelper.apply(binding.homeBillStatus, first.status ?: "PENDING")
            } else {
                binding.homeBillMonth.text = "No pending bill"
                binding.homeBillTotal.text = "—"
                binding.homeBillPaid.text = ""
                binding.homeBillPending.text = ""
                binding.homeBillStatus.text = "—"
                binding.homeBillStatus.visibility = View.GONE
            }
        }
    }

    private fun observeDeliveries() {
        deliveriesViewModel.listResult.observe(viewLifecycleOwner) { result ->
            if (result is ApiResult.Success) {
                val list = result.dataOrNull().orEmpty().take(3)
                binding.homeDeliveriesList.removeAllViews()
                binding.homeDeliveriesEmpty.bindEmptyState("No recent deliveries")
                binding.homeDeliveriesEmpty.visibility = if (list.isEmpty()) View.VISIBLE else View.GONE
                list.forEach { d ->
                    val item = ItemDeliveryRowBinding.inflate(layoutInflater, binding.homeDeliveriesList, false)
                    item.deliveryDescription.text = d.description ?: "Delivery"
                    item.deliveryDate.text = d.createdAt ?: ""
                    StatusChipHelper.apply(item.deliveryStatus, d.status ?: "PENDING")
                    binding.homeDeliveriesList.addView(item.root)
                }
            }
        }
    }

    private fun observeSos() {
        sosViewModel.triggerResult.observe(viewLifecycleOwner) { result ->
            when (result) {
                is ApiResult.Success -> Snackbar.make(binding.root, "SOS sent", Snackbar.LENGTH_SHORT).show()
                is ApiResult.Error -> Snackbar.make(binding.root, result.message, Snackbar.LENGTH_SHORT).show()
                else -> {}
            }
        }
    }

    private fun showSosConfirmation() {
        AlertDialog.Builder(requireContext(), com.app.core.R.style.AppCompatAlertDialogStyle)
            .setTitle("Trigger SOS?")
            .setMessage("Emergency alert will be sent to security.")
            .setPositiveButton("Send SOS") { _, _ -> sosViewModel.triggerSOS() }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun showError(message: String) {
        binding.homeLoading.visibility = View.GONE
        binding.homeContent.visibility = View.GONE
        binding.homeError.visibility = View.VISIBLE
        binding.homeError.findViewById<android.widget.TextView>(com.app.divine.R.id.error_message)?.text = message
    }
}
