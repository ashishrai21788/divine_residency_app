package com.app.divine.activity.admin.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.divine.R
import com.app.divine.api.ApiResult
import com.app.divine.api.dataOrNull
import com.app.divine.api.dto.VillaNoticeDto
import com.app.divine.databinding.FragmentAdminNoticesBinding
import com.app.core.dagger.preference.AppPreferences
import com.app.core.extensions.ui.coreComponent
import com.app.divine.ui.common.bindEmptyState
import com.app.divine.villa.VillaViewModelFactory
import com.app.divine.villa.viewmodel.VillaNoticesViewModel
import com.google.android.material.snackbar.Snackbar

class AdminNoticesFragment : Fragment() {

    private lateinit var binding: FragmentAdminNoticesBinding
    private lateinit var viewModel: VillaNoticesViewModel
    private val noticeAdapter = NoticesAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentAdminNoticesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val societyId = coreComponent().appPreferences().getAllPreferenceTypeString(AppPreferences.VILLA_SOCIETY_ID_KEY)
        viewModel = ViewModelProvider(this, VillaViewModelFactory(requireContext()))[VillaNoticesViewModel::class.java]

        binding.adminNoticesList.layoutManager = LinearLayoutManager(requireContext())
        binding.adminNoticesList.adapter = noticeAdapter

        binding.adminNoticesEmpty.bindEmptyState(
            getString(R.string.admin_notices_empty_title),
            getString(R.string.admin_notices_empty_subtitle)
        )

        binding.adminNoticePublish.setOnClickListener { showPublishDialog() }
        binding.adminNoticesSwipe.setOnRefreshListener { load(societyId) }
        viewModel.listResult.observe(viewLifecycleOwner) { result ->
            binding.adminNoticesSwipe.isRefreshing = false
            when (result) {
                is ApiResult.Success -> renderNotices(result.dataOrNull().orEmpty())
                is ApiResult.Error -> Snackbar.make(binding.root, result.message, Snackbar.LENGTH_SHORT).show()
                else -> {}
            }
        }
        viewModel.createResult.observe(viewLifecycleOwner) { result ->
            when (result) {
                is ApiResult.Success -> {
                    Snackbar.make(binding.root, "Notice published", Snackbar.LENGTH_SHORT).show()
                    load(societyId)
                }
                is ApiResult.Error -> Snackbar.make(binding.root, result.message, Snackbar.LENGTH_SHORT).show()
                else -> {}
            }
        }
        load(societyId)
    }

    private fun load(societyId: String) {
        if (societyId.isEmpty()) return
        viewModel.loadNotices(societyId)
    }

    private fun renderNotices(list: List<VillaNoticeDto>) {
        if (list.isEmpty()) {
            binding.adminNoticesEmpty.visibility = View.VISIBLE
            noticeAdapter.submit(emptyList())
            return
        }
        binding.adminNoticesEmpty.visibility = View.GONE
        noticeAdapter.submit(list)
    }

    private fun showPublishDialog() {
        val ctx = requireContext()
        val title = EditText(ctx).apply { hint = "Title" }
        val body = EditText(ctx).apply {
            hint = "Message"
            minLines = 3
        }
        val layout = LinearLayout(ctx).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(48, 24, 48, 8)
            addView(title)
            addView(body)
        }
        AlertDialog.Builder(ctx, com.app.core.R.style.AppCompatAlertDialogStyle)
            .setTitle("New notice")
            .setView(layout)
            .setPositiveButton("Publish") { _, _ ->
                val t = title.text?.toString()?.trim() ?: ""
                val c = body.text?.toString()?.trim() ?: ""
                if (t.isBlank() || c.isBlank()) {
                    Snackbar.make(binding.root, "Title and message required", Snackbar.LENGTH_SHORT).show()
                    return@setPositiveButton
                }
                viewModel.createNotice(t, c)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private class NoticesAdapter : RecyclerView.Adapter<NoticesAdapter.Holder>() {
        private var items: List<VillaNoticeDto> = emptyList()

        fun submit(list: List<VillaNoticeDto>) {
            items = list
            notifyDataSetChanged()
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
            val v = LayoutInflater.from(parent.context).inflate(R.layout.item_notice_row, parent, false)
            return Holder(v)
        }

        override fun onBindViewHolder(holder: Holder, position: Int) = holder.bind(items[position])

        override fun getItemCount() = items.size

        class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            private val title: TextView = itemView.findViewById(R.id.notice_title)
            private val content: TextView = itemView.findViewById(R.id.notice_content)
            private val date: TextView = itemView.findViewById(R.id.notice_date)

            fun bind(n: VillaNoticeDto) {
                title.text = n.title ?: "Notice"
                content.text = n.content ?: ""
                date.text = n.createdAt ?: ""
            }
        }
    }
}
