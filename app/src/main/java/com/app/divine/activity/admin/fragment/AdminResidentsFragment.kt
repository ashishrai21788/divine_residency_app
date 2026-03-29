package com.app.divine.activity.admin.fragment

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
import com.app.divine.api.dto.VillaAdminUserDto
import com.app.divine.databinding.FragmentAdminResidentsBinding
import com.app.core.dagger.preference.AppPreferences
import com.app.core.extensions.ui.coreComponent
import com.app.divine.ui.common.bindEmptyState
import com.app.divine.villa.VillaViewModelFactory
import com.app.divine.villa.viewmodel.VillaUsersViewModel
import com.google.android.material.snackbar.Snackbar

class AdminResidentsFragment : Fragment() {

    private lateinit var binding: FragmentAdminResidentsBinding
    private lateinit var viewModel: VillaUsersViewModel
    private val adapter = ResidentsAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: android.os.Bundle?): View {
        binding = FragmentAdminResidentsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: android.os.Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val societyId = coreComponent().appPreferences().getAllPreferenceTypeString(AppPreferences.VILLA_SOCIETY_ID_KEY)
        viewModel = ViewModelProvider(this, VillaViewModelFactory(requireContext()))[VillaUsersViewModel::class.java]

        binding.adminResidentsList.layoutManager = LinearLayoutManager(requireContext())
        binding.adminResidentsList.adapter = adapter

        binding.adminResidentsEmpty.bindEmptyState(getString(R.string.admin_residents_empty_title))

        binding.adminResidentsSwipe.setOnRefreshListener { load(societyId) }
        viewModel.listResult.observe(viewLifecycleOwner) { result ->
            binding.adminResidentsSwipe.isRefreshing = false
            when (result) {
                is ApiResult.Success -> render(result.dataOrNull().orEmpty())
                is ApiResult.Error -> Snackbar.make(binding.root, result.message, Snackbar.LENGTH_SHORT).show()
                else -> {}
            }
        }
        load(societyId)
    }

    private fun load(societyId: String) {
        viewModel.loadUsers(societyId.ifBlank { null })
    }

    private fun render(list: List<VillaAdminUserDto>) {
        if (list.isEmpty()) {
            binding.adminResidentsEmpty.visibility = View.VISIBLE
            adapter.submit(emptyList())
            return
        }
        binding.adminResidentsEmpty.visibility = View.GONE
        adapter.submit(list)
    }

    private class ResidentsAdapter : RecyclerView.Adapter<ResidentsAdapter.Holder>() {
        private var items: List<VillaAdminUserDto> = emptyList()

        fun submit(list: List<VillaAdminUserDto>) {
            items = list
            notifyDataSetChanged()
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
            val v = LayoutInflater.from(parent.context).inflate(R.layout.item_admin_resident_row, parent, false)
            return Holder(v)
        }

        override fun onBindViewHolder(holder: Holder, position: Int) = holder.bind(items[position])

        override fun getItemCount() = items.size

        class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            private val name: TextView = itemView.findViewById(R.id.resident_name)
            private val role: TextView = itemView.findViewById(R.id.resident_role)
            private val contact: TextView = itemView.findViewById(R.id.resident_contact)

            fun bind(u: VillaAdminUserDto) {
                name.text = u.name ?: "—"
                role.text = (u.role ?: "—").uppercase()
                contact.text = u.email ?: u.mobile ?: u.displayId()
            }
        }
    }
}
