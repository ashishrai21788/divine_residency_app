package com.app.divine.fragments.resident

import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.app.divine.R
import com.app.divine.api.ApiResult
import com.app.divine.api.dataOrNull
import com.app.divine.api.dto.VillaComplaintCreateRequest
import com.app.divine.api.dto.VillaComplaintDto
import com.app.divine.databinding.FragmentResidentComplaintsBinding
import com.app.divine.databinding.ItemComplaintBinding
import com.app.core.dagger.preference.AppPreferences
import com.app.core.extensions.ui.coreComponent
import com.app.divine.ui.common.StatusChipHelper
import com.app.divine.ui.common.bindEmptyState
import com.app.divine.villa.VillaViewModelFactory
import com.app.divine.villa.viewmodel.VillaComplaintsViewModel
import com.google.android.material.snackbar.Snackbar

class ResidentComplaintsFragment : Fragment() {

    private lateinit var binding: FragmentResidentComplaintsBinding
    private lateinit var viewModel: VillaComplaintsViewModel
    private var societyId: String = ""
    private val categories = arrayOf("Maintenance", "Security", "Amenities", "Noise", "Other")

    override fun onCreateView(inflater: android.view.LayoutInflater, container: android.view.ViewGroup?, savedInstanceState: android.os.Bundle?): android.view.View {
        binding = FragmentResidentComplaintsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: android.view.View, savedInstanceState: android.os.Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        societyId = coreComponent().appPreferences().getAllPreferenceTypeString(AppPreferences.VILLA_SOCIETY_ID_KEY)
        viewModel = ViewModelProvider(this, VillaViewModelFactory(requireContext()))[VillaComplaintsViewModel::class.java]

        binding.complaintsTabs.addTab(binding.complaintsTabs.newTab().setText("My Complaints"))
        binding.complaintsTabs.addTab(binding.complaintsTabs.newTab().setText("Raise Complaint"))

        binding.complaintsTabs.addOnTabSelectedListener(object : com.google.android.material.tabs.TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: com.google.android.material.tabs.TabLayout.Tab?) {
                val isList = (tab?.position ?: 0) == 0
                binding.complaintsSwipe.visibility = if (isList) android.view.View.VISIBLE else android.view.View.GONE
                binding.complaintsFormContainer.visibility = if (isList) android.view.View.GONE else android.view.View.VISIBLE
                if (isList) loadList()
            }
            override fun onTabUnselected(tab: com.google.android.material.tabs.TabLayout.Tab?) {}
            override fun onTabReselected(tab: com.google.android.material.tabs.TabLayout.Tab?) {}
        })

        binding.complaintsSwipe.setOnRefreshListener { loadList() }
        binding.complaintsError.findViewById<Button>(R.id.error_retry)?.setOnClickListener { loadList() }

        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, categories)
        binding.complaintCategory.adapter = adapter

        binding.complaintSubmit.setOnClickListener { submitComplaint() }

        viewModel.myComplaintsResult.observe(viewLifecycleOwner) { onList(it) }
        viewModel.createResult.observe(viewLifecycleOwner) { onCreateResult(it) }
        loadList()
    }

    private fun loadList() {
        if (societyId.isEmpty()) {
            showError("Not logged in")
            return
        }
        binding.complaintsLoading.visibility = android.view.View.VISIBLE
        binding.complaintsError.visibility = android.view.View.GONE
        viewModel.loadMyComplaints(societyId)
    }

    private fun onList(result: ApiResult<List<VillaComplaintDto>>) {
        binding.complaintsSwipe.isRefreshing = false
        binding.complaintsLoading.visibility = android.view.View.GONE
        when (result) {
            is ApiResult.Success -> {
                val list = result.dataOrNull().orEmpty()
                binding.complaintsListContainer.removeAllViews()
                if (list.isEmpty()) {
                    binding.complaintsEmpty.visibility = android.view.View.VISIBLE
                    binding.complaintsEmpty.bindEmptyState("No complaints yet")
                } else {
                    binding.complaintsEmpty.visibility = android.view.View.GONE
                    list.forEach { c ->
                        val item = ItemComplaintBinding.inflate(layoutInflater, binding.complaintsListContainer, false)
                        item.complaintTitle.text = c.title ?: ""
                        item.complaintCategory.text = c.category ?: ""
                        StatusChipHelper.apply(item.complaintStatus, c.status ?: "OPEN")
                        item.complaintDate.text = c.createdAt ?: ""
                        binding.complaintsListContainer.addView(item.root)
                    }
                }
            }
            is ApiResult.Error -> {
                binding.complaintsError.visibility = android.view.View.VISIBLE
                binding.complaintsError.findViewById<TextView>(R.id.error_message)?.text = result.message
            }
            else -> {}
        }
    }

    private fun submitComplaint() {
        val title = binding.complaintTitle.text?.toString()?.trim() ?: ""
        val description = binding.complaintDescription.text?.toString()?.trim() ?: ""
        val category = categories.getOrNull(binding.complaintCategory.selectedItemPosition) ?: "Other"
        if (title.isBlank()) {
            binding.complaintTitle.error = "Required"
            return
        }
        if (description.isBlank()) {
            binding.complaintDescription.error = "Required"
            return
        }
        binding.complaintTitle.error = null
        binding.complaintDescription.error = null
        val request = VillaComplaintCreateRequest(
            villaId = societyId,
            floorId = societyId,
            category = category,
            title = title,
            description = description,
            photoUrl = null
        )
        viewModel.createComplaint(request)
    }

    private fun onCreateResult(result: ApiResult<VillaComplaintDto>) {
        when (result) {
            is ApiResult.Success -> {
                Snackbar.make(binding.root, "Complaint submitted", Snackbar.LENGTH_SHORT).show()
                binding.complaintTitle.text?.clear()
                binding.complaintDescription.text?.clear()
                binding.complaintsTabs.getTabAt(0)?.select()
                loadList()
            }
            is ApiResult.Error -> Snackbar.make(binding.root, result.message, Snackbar.LENGTH_SHORT).show()
            else -> {}
        }
    }

    private fun showError(msg: String) {
        binding.complaintsError.visibility = android.view.View.VISIBLE
        binding.complaintsError.findViewById<TextView>(R.id.error_message)?.text = msg
    }
}
