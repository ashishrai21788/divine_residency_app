package com.app.core.utils

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.app.core.R


class CoreDividerItemDecoration(val context: Context, private val dividerColor: Int, val dividerOffSet: Int = 0, var lastItemDivider: Boolean = true) :
    RecyclerView.ItemDecoration() {
    private var divider: Drawable? = ContextCompat.getDrawable(context, R.drawable.core_list_divider_bg)?.let {
        val coloredBg = it as? GradientDrawable
        coloredBg?.setColor(dividerColor)
        coloredBg
    } ?: kotlin.run { null }


    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val left = parent.paddingLeft
        val right = parent.width - parent.paddingRight
        val childCount = parent.childCount
        for (i in 0 until childCount) {
            val child = parent.getChildAt(i)
            val params = child.layoutParams as RecyclerView.LayoutParams
            val top = child.bottom + params.bottomMargin + dividerOffSet
            val bottom = top + (divider?.intrinsicHeight ?: 0)
            divider?.setBounds(left, top, right, bottom)
            if (i != childCount - 1 || lastItemDivider)
                divider?.draw(c)
        }
    }


}