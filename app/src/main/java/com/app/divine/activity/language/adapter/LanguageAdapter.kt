package com.app.divine.activity.language.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.app.divine.databinding.ItemLanguageBinding
import com.app.divine.activity.language.model.LanguageItem

class LanguageAdapter(
    private val onLanguageClick: (LanguageItem) -> Unit
) : ListAdapter<LanguageItem, LanguageAdapter.LanguageViewHolder>(LanguageDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LanguageViewHolder {
        val binding = ItemLanguageBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return LanguageViewHolder(binding, onLanguageClick)
    }

    override fun onBindViewHolder(holder: LanguageViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class LanguageViewHolder(
        private val binding: ItemLanguageBinding,
        private val onLanguageClick: (LanguageItem) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(languageItem: LanguageItem) {
            binding.apply {
                // Set data binding variables
                languageFlag = languageItem.flag
                languageName = languageItem.displayName
                languageNativeName = languageItem.nativeName
                isSelected = languageItem.isSelected
                
                // Execute pending bindings
                executePendingBindings()
                
                // Set click listener
                root.setOnClickListener {
                    onLanguageClick(languageItem)
                }
            }
        }
    }

    private class LanguageDiffCallback : DiffUtil.ItemCallback<LanguageItem>() {
        override fun areItemsTheSame(oldItem: LanguageItem, newItem: LanguageItem): Boolean {
            return oldItem.code == newItem.code
        }

        override fun areContentsTheSame(oldItem: LanguageItem, newItem: LanguageItem): Boolean {
            return oldItem == newItem
        }
    }
} 