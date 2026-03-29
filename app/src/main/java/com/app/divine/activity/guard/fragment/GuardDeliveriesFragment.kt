package com.app.divine.activity.guard.fragment

import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.divine.R
import com.app.divine.api.ApiResult
import com.app.divine.api.dataOrNull
import com.app.divine.api.dto.VillaDeliveryDto
import com.app.divine.databinding.FragmentGuardDeliveriesBinding
import com.app.divine.databinding.ItemDeliveryRowBinding
import com.app.core.dagger.preference.AppPreferences
import com.app.core.extensions.ui.coreComponent
import com.app.divine.ui.common.StatusChipHelper
import com.app.divine.ui.common.bindEmptyState
import com.app.divine.villa.VillaViewModelFactory
import com.app.divine.villa.viewmodel.VillaDeliveriesViewModel
import com.google.android.material.snackbar.Snackbar

class GuardDeliveriesFragment : Fragment() {

    private lateinit var binding: FragmentGuardDeliveriesBinding
    private lateinit var viewModel: VillaDeliveriesViewModel
    private var societyId: String = ""

    override fun onCreateView(inflater: android.view.LayoutInflater, container: android.view.ViewGroup?, savedInstanceState: android.os.Bundle?): android.view.View {
        binding = FragmentGuardDeliveriesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: android.view.View, savedInstanceState: android.os.Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        societyId = coreComponent().appPreferences().getAllPreferenceTypeString(AppPreferences.VILLA_SOCIETY_ID_KEY)
        viewModel = ViewModelProvider(this, VillaViewModelFactory(requireContext()))[VillaDeliveriesViewModel::class.java]

        binding.guardDeliveriesSwipe.setOnRefreshListener { load() }
        binding.guardDeliveriesError.findViewById<Button>(R.id.error_retry)?.setOnClickListener { load() }

        viewModel.listResult.observe(viewLifecycleOwner) { onList(it) }
        viewModel.markReceivedResult.observe(viewLifecycleOwner) { result ->
            if (result is ApiResult.Success) Snackbar.make(binding.root, "Marked as received", Snackbar.LENGTH_SHORT).show()
            if (result is ApiResult.Error) Snackbar.make(binding.root, result.message, Snackbar.LENGTH_SHORT).show()
            load()
        }
        load()
    }

    private fun load() {
        if (societyId.isEmpty()) return
        viewModel.loadDeliveries(societyId = societyId)
    }

    private fun onList(result: ApiResult<List<VillaDeliveryDto>>) {
        binding.guardDeliveriesSwipe.isRefreshing = false
        when (result) {
            is ApiResult.Success -> {
                val list = result.dataOrNull().orEmpty()
                binding.guardDeliveriesError.visibility = android.view.View.GONE
                if (list.isEmpty()) {
                    binding.guardDeliveriesEmpty.visibility = android.view.View.VISIBLE
                    binding.guardDeliveriesEmpty.bindEmptyState("No deliveries")
                    binding.guardDeliveriesList.adapter = null
                } else {
                    binding.guardDeliveriesEmpty.visibility = android.view.View.GONE
                    binding.guardDeliveriesList.layoutManager = LinearLayoutManager(requireContext())
                    binding.guardDeliveriesList.adapter = GuardDeliveriesAdapter(list) { id -> viewModel.markReceived(id) }
                }
            }
            is ApiResult.Error -> {
                binding.guardDeliveriesEmpty.visibility = android.view.View.GONE
                binding.guardDeliveriesError.visibility = android.view.View.VISIBLE
                binding.guardDeliveriesError.findViewById<TextView>(R.id.error_message)?.text = result.message
            }
            else -> {}
        }
    }

    private class GuardDeliveriesAdapter(
        private val items: List<VillaDeliveryDto>,
        private val onMarkReceived: (String) -> Unit
    ) : RecyclerView.Adapter<GuardDeliveriesAdapter.Holder>() {
        override fun onCreateViewHolder(parent: android.view.ViewGroup, viewType: Int): Holder {
            val b = ItemDeliveryRowBinding.inflate(android.view.LayoutInflater.from(parent.context), parent, false)
            return Holder(b, onMarkReceived)
        }
        override fun onBindViewHolder(holder: Holder, position: Int) = holder.bind(items[position])
        override fun getItemCount() = items.size

        class Holder(private val b: ItemDeliveryRowBinding, private val onMarkReceived: (String) -> Unit) : RecyclerView.ViewHolder(b.root) {
            fun bind(d: VillaDeliveryDto) {
                b.deliveryDescription.text = d.description ?: "Delivery"
                b.deliveryDate.text = d.createdAt ?: ""
                StatusChipHelper.apply(b.deliveryStatus, d.status ?: "PENDING")
                b.root.setOnClickListener { d.id?.let { onMarkReceived(it) } }
            }
        }
    }
}
