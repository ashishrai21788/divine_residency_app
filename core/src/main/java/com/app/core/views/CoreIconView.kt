package com.app.core.views

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import android.webkit.URLUtil
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.app.core.R
import com.app.core.bindingadapter.CoreBindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target

class CoreIconView(context: Context, attrs: AttributeSet?) : FrameLayout(context, attrs) {

    private var coreIconValue: String? = null
    private var coreIconSize: String? = null
    private var coreIconSizeFactor: Float? = null
    private var coreIconColor: Int = Color.BLACK
    private var iconImageView: ImageView? = null
    private var iconTextView: TextView? = null
    private var overrideImageSize: Pair<Int, Int>? = null
    private var isCircularImage: Boolean = false
    private var imageLoadListener: CoreIconLoadListener?=null
    private var corePlaceHolderIcon:Drawable? = null

    init {
        inflate(context, R.layout.core_icon_view, this)
        iconImageView = findViewById(R.id.core_image_icon)
        iconTextView = findViewById(R.id.core_text_icon)
        renderViews()
    }


    fun provideCoreImageView():ImageView = iconImageView!!

    fun updateConfigs(
        icon: String? = null,
        iconSize: String? = null,
        iconSizeFactor: Float? = null,
        iconColor: Int? = null,
        overrideImageSize: Pair<Int, Int>? = null,
        isCircularImage:Boolean = false,
        corePlaceHolderIcon:Drawable? = null
    ) {
        this.coreIconValue = icon ?: this.coreIconValue
        this.coreIconSize = iconSize ?: this.coreIconSize
        this.coreIconSizeFactor = iconSizeFactor ?: this.coreIconSizeFactor
        this.coreIconColor = iconColor ?: this.coreIconColor
        this.overrideImageSize = overrideImageSize
        this.isCircularImage = isCircularImage
        this.corePlaceHolderIcon = corePlaceHolderIcon
        this.renderViews()
    }


    private fun renderViews() {
        val iconValue = coreIconValue ?: ""
        val iconSize = coreIconSize ?: "medium"
        val iconSizeFactor: Float = coreIconSizeFactor ?: 1f
        if (iconValue.isBlank())
            return

        if (URLUtil.isValidUrl(iconValue)) {
            iconTextView?.visibility = View.INVISIBLE
            iconImageView?.visibility = View.VISIBLE
            val iconWidthSize = CoreBindingAdapter.provideIconWidth(context, iconSize, iconSizeFactor).toInt()
            iconImageView?.layoutParams?.width = iconWidthSize
            iconImageView?.layoutParams?.height = iconWidthSize
            Glide.with(context).load(iconValue)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .apply {
                    corePlaceHolderIcon?.let {
                        placeholder(it)
                        error(it)
                    }
                }
                .apply {
                    //if (isCircularImage)
                       // transform(CoreCircularTransformation())
                }
                .apply {
                    overrideImageSize?.let {
                        this.override(it.first, it.second)
                    }
                }.listener(object : RequestListener<Drawable>{

                    override fun onResourceReady(
                        resource: Drawable,
                        model: Any,
                        target: Target<Drawable>?,
                        dataSource: DataSource,
                        isFirstResource: Boolean
                    ): Boolean {
                        imageLoadListener?.onImageLoad(true)
                        return false
                    }

                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>,
                        isFirstResource: Boolean
                    ): Boolean {
                        imageLoadListener?.onImageLoad(false)
                        return false
                    }


                })
                .into(iconImageView!!)

        } else {
            iconImageView?.visibility = View.GONE
            iconTextView?.visibility = View.VISIBLE
            iconTextView?.let { iconTV ->
                CoreBindingAdapter.setCustomFontText(textView = iconTV, fontCode = iconValue, iconSize = iconSize, _factor = iconSizeFactor)
                CoreBindingAdapter.setTextColor(view = iconTV, _textColor = coreIconColor)
            }
        }
    }

    fun setCoreIconListener(listener: CoreIconLoadListener){
        imageLoadListener=listener
    }


    interface CoreIconLoadListener{
        fun onImageLoad(isSuccess:Boolean)
    }

}