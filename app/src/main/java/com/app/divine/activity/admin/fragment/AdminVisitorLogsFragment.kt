package com.app.divine.activity.admin.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.divine.R
import com.app.divine.api.ApiResult
import com.app.divine.api.dataOrNull
import com.app.divine.api.dto.VillaVisitorLogDto
import com.app.divine.databinding.FragmentAdminVisitorLogsBinding
import com.app.divine.databinding.ItemVisitorLogBinding
import com.app.core.dagger.preference.AppPreferences
import com.app.core.extensions.ui.coreComponent
import com.app.divine.villa.VillaViewModelFactory
import com.app.divine.ui.common.StatusChipHelper
import com.app.divine.ui.common.bindEmptyState
import com.app.divine.villa.viewmodel.VillaVisitorsViewModel
import com.google.android.material.snackbar.Snackbar

class AdminVisitorLogsFragment : Fragment() {

    private lateinit var binding: FragmentAdminVisitorLogsBinding
    private lateinit var viewModel: VillaVisitorsViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentAdminVisitorLogsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val societyId = coreComponent().appPreferences().getAllPreferenceTypeString(AppPreferences.VILLA_SOCIETY_ID_KEY)
        viewModel = ViewModelProvider(this, VillaViewModelFactory(requireContext()))[VillaVisitorsViewModel::class.java]
        binding.adminLogsSwipe.setOnRefreshListener { loadLogs(societyId) }
        binding.adminLogsEmpty.bindEmptyState(getString(R.string.admin_logs_empty_title))
        viewModel.logsResult.observe(viewLifecycleOwner) { result ->
            binding.adminLogsSwipe.isRefreshing = false
            when (result) {
                is ApiResult.Success -> {
                    val list = result.dataOrNull().orEmpty()
                    if (list.isEmpty()) {
                        binding.adminLogsEmpty.visibility = View.VISIBLE
                        binding.adminLogsList.adapter = null
                    } else {
                        binding.adminLogsEmpty.visibility = View.GONE
                        if (binding.adminLogsList.layoutManager == null) {
                            binding.adminLogsList.layoutManager = LinearLayoutManager(requireContext())
                        }
                        binding.adminLogsList.adapter = AdminLogAdapter(list)
                    }
                }
                is ApiResult.Error -> Snackbar.make(binding.root, result.message, Snackbar.LENGTH_SHORT).show()
                else -> {}
            }
        }
        loadLogs(societyId)
    }

    private fun loadLogs(societyId: String) {
        if (societyId.isEmpty()) return
        viewModel.loadVisitorLogs(societyId, null, null)
    }

    private class AdminLogAdapter(private val items: List<VillaVisitorLogDto>) : RecyclerView.Adapter<AdminLogAdapter.Holder>() {
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
