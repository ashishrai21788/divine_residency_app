package com.app.divine.fragments.resident

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.app.divine.api.ApiResult
import com.app.divine.api.dataOrNull
import com.app.divine.api.dto.VillaBillDto
import com.app.divine.api.dto.VillaPaymentDto
import com.app.divine.databinding.FragmentResidentBillingBinding
import com.app.divine.databinding.ItemBillRowBinding
import com.app.divine.databinding.ItemPaymentRowBinding
import com.app.core.dagger.preference.AppPreferences
import com.app.core.extensions.ui.coreComponent
import com.app.divine.ui.common.StatusChipHelper
import com.app.divine.ui.common.bindEmptyState
import com.app.divine.villa.VillaViewModelFactory
import com.app.divine.villa.viewmodel.VillaBillingViewModel

class ResidentBillingFragment : Fragment() {

    private lateinit var binding: FragmentResidentBillingBinding
    private lateinit var billingViewModel: VillaBillingViewModel
    private var societyId: String = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: android.os.Bundle?): View {
        binding = FragmentResidentBillingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: android.os.Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        societyId = coreComponent().appPreferences().getAllPreferenceTypeString(AppPreferences.VILLA_SOCIETY_ID_KEY)
        billingViewModel = ViewModelProvider(this, VillaViewModelFactory(requireContext()))[VillaBillingViewModel::class.java]

        binding.billingSwipe.setOnRefreshListener { load() }
        binding.billingError.findViewById<Button>(com.app.divine.R.id.error_retry)?.setOnClickListener { load() }

        billingViewModel.myBillsResult.observe(viewLifecycleOwner) { onBills(it) }
        billingViewModel.myPaymentsResult.observe(viewLifecycleOwner) { onPayments(it) }
        load()
    }

    private fun load() {
        if (societyId.isEmpty()) {
            showError("Not logged in")
            return
        }
        binding.billingLoading.visibility = View.VISIBLE
        binding.billingError.visibility = View.GONE
        billingViewModel.loadMyBills(societyId)
        billingViewModel.loadMyPayments(societyId)
    }

    private fun onBills(result: ApiResult<List<VillaBillDto>>) {
        binding.billingSwipe.isRefreshing = false
        when (result) {
            is ApiResult.Success -> {
                val list = result.dataOrNull().orEmpty()
                val total = list.sumOf { (it.amount ?: 0.0).toDouble() }.toFloat()
                val pending = list.filter { (it.status ?: "").uppercase() != "PAID" }.sumOf { (it.amount ?: 0.0).toDouble() }.toFloat()
                val paid = total - pending
                binding.billingTotal.text = "₹%.2f".format(total)
                binding.billingPaid.text = "₹%.2f".format(paid)
                binding.billingPending.text = "₹%.2f".format(pending)
                val first = list.firstOrNull()
                binding.billingDueDate.text = first?.dueDate?.let { "Due: $it" } ?: ""
                val statusLabel = if (pending > 0) "PENDING" else "PAID"
                StatusChipHelper.apply(binding.billingStatusPill, statusLabel)

                binding.billingBillsList.removeAllViews()
                if (list.isEmpty()) {
                    binding.billingBillsEmpty.visibility = View.VISIBLE
                    binding.billingBillsEmpty.bindEmptyState("No bills")
                } else {
                    binding.billingBillsEmpty.visibility = View.GONE
                    list.forEach { bill ->
                        val item = ItemBillRowBinding.inflate(layoutInflater, binding.billingBillsList, false)
                        item.billMonth.text = "Month ${bill.month ?: ""} ${bill.year ?: ""}"
                        item.billDue.text = "Due: ${bill.dueDate ?: ""}"
                        item.billAmount.text = "₹${bill.amount ?: 0}"
                        StatusChipHelper.apply(item.billStatus, bill.status ?: "PENDING")
                        binding.billingBillsList.addView(item.root)
                    }
                }
                binding.billingLoading.visibility = View.GONE
            }
            is ApiResult.Error -> {
                binding.billingLoading.visibility = View.GONE
                showError(result.message)
            }
            else -> {}
        }
    }

    private fun onPayments(result: ApiResult<List<VillaPaymentDto>>) {
        when (result) {
            is ApiResult.Success -> {
                val list = result.dataOrNull().orEmpty()
                binding.billingPaymentsList.removeAllViews()
                if (list.isEmpty()) {
                    binding.billingPaymentsEmpty.visibility = View.VISIBLE
                    binding.billingPaymentsEmpty.bindEmptyState("No payments yet")
                } else {
                    binding.billingPaymentsEmpty.visibility = View.GONE
                    list.forEach { pay ->
                        val item = ItemPaymentRowBinding.inflate(layoutInflater, binding.billingPaymentsList, false)
                        item.paymentMonth.text = pay.paidAt ?: "—"
                        item.paymentMode.text = pay.paymentMode ?: ""
                        item.paymentAmount.text = "₹${pay.amountPaid ?: 0}"
                        binding.billingPaymentsList.addView(item.root)
                    }
                }
            }
            else -> {}
        }
    }

    private fun showError(msg: String) {
        binding.billingError.visibility = View.VISIBLE
        binding.billingError.findViewById<TextView>(com.app.divine.R.id.error_message)?.text = msg
    }
}
