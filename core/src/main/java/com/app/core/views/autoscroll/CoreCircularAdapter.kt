package com.app.core.views.autoscroll


import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

abstract class CoreCircularAdapter<T,H:RecyclerView.ViewHolder> : RecyclerView.Adapter<H>() {
    private val items = arrayListOf<T>()

    abstract fun createItemViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): H

    abstract fun bindItemViewHolder(
        holder: H,
        item: T,
        actualPosition: Int,
        position: Int
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): H {
        return createItemViewHolder(parent, viewType)
    }

    override fun onBindViewHolder(holder: H, position: Int) {
        val positionInList = position % items.size
        bindItemViewHolder(holder, items[positionInList], position, positionInList)
    }

    fun getActualItemCount() = items.size

    override fun getItemCount(): Int {
        return if (items.size > 1) Int.MAX_VALUE else items.size
    }

    fun setItems(items: ArrayList<T>, clearPreviousElements: Boolean = false) {
        if (clearPreviousElements) {
            this.items.clear()
        }
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    fun clear() {
        items.clear()
        notifyDataSetChanged()
    }
}