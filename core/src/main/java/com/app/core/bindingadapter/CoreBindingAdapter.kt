package com.app.core.bindingadapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.*
import android.graphics.drawable.*
import android.graphics.text.LineBreaker
import android.os.Build
import android.util.StateSet
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.*
import androidx.appcompat.widget.*
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.view.ViewCompat
import androidx.core.view.doOnPreDraw
import androidx.core.widget.ImageViewCompat
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.app.core.extensions.*
import com.app.core.views.ExpandableTextView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.card.MaterialCardView
import com.google.android.material.tabs.TabLayout
import com.google.android.material.textfield.TextInputLayout
import com.app.core.circularimageview.CoreCircleImageView
import com.app.core.utils.CoreAlignmentType
import com.app.core.utils.CoreMetaData
import com.app.core.utils.DesignUtil
import com.app.core.views.CoreIconView
import java.util.*

object CoreBindingAdapter {


    @BindingAdapter(
        value = ["core_content_text_size", "core_content_text_size_factor"],
        requireAll = false
    )
    @JvmStatic
    fun setCoreContentTextSize(view: View, _contentTextSize: String?, _contentFactor: Float? = 1f) {
        val contentSize: String =
            if (_contentTextSize.isNullOrEmpty()) "medium" else _contentTextSize.toLowerCase(Locale.US)
        val contentFactor: Float = _contentFactor ?: 1f

        var textSize = 0f
        when {
            "small" in contentSize -> {
                textSize = view.resources.getDimension(com.intuit.sdp.R.dimen._10sdp) * contentFactor
            }
            "medium" in contentSize -> {
                textSize = view.resources.getDimension(com.intuit.sdp.R.dimen._12sdp) * contentFactor
            }
            "xlarge" in contentSize -> {
                textSize = view.resources.getDimension(com.intuit.sdp.R.dimen._16sdp) * contentFactor
            }
            "large" in contentSize -> {
                textSize = view.resources.getDimension(com.intuit.sdp.R.dimen._14sdp) * contentFactor
            }
        }

        when (view) {
            is TextView -> {
                view.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize)
            }
            is EditText -> {

                view.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize)
            }
            is Button -> {

                view.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize)
            }
            is AppCompatCheckBox -> {
                view.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize)
            }

        }
    }


    @BindingAdapter(
        value = ["core_nav_text_size", "core_nav_text_size_factor"],
        requireAll = false
    )
    @JvmStatic
    fun setCoreNavTextSize(view: View, _navTextSize: String?, _navFactor: Float? = 1f) {
        val contentSize: String =
            if (_navTextSize.isNullOrEmpty()) "medium" else _navTextSize.toLowerCase(Locale.US)
        val contentFactor: Float = _navFactor ?: 1f

        var textSize = 0f
        when {
            "small" in contentSize -> {
                textSize = view.resources.getDimension(com.intuit.sdp.R.dimen._14sdp) * contentFactor
            }
            "medium" in contentSize -> {
                textSize = view.resources.getDimension(com.intuit.sdp.R.dimen._16sdp) * contentFactor
            }
            "xlarge" in contentSize -> {
                textSize = view.resources.getDimension(com.intuit.sdp.R.dimen._21sdp) * contentFactor
            }
            "large" in contentSize -> {
                textSize = view.resources.getDimension(com.intuit.sdp.R.dimen._19sdp) * contentFactor
            }
        }


        when (view) {
            is TextView -> {
                view.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize)
            }
            is EditText -> {

                view.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize)
            }
            is Button -> {

                view.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize)
            }
            is AppCompatCheckBox -> {
                view.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize)
            }

        }
    }

    @BindingAdapter(
        value = ["core_background_color", "core_background_color_factor", "core_divider_enable"],
        requireAll = false
    )
    @JvmStatic
    fun setBackgroundColor(view: View, _bgColor: Int?, _factor: Float? = null, _enableDivider: Boolean = true ) {
        val bgColor: Int = _bgColor ?: -1
        val color = _factor?.let {
            bgColor.transparent((it.coerceAtMost(1f)))
        } ?: kotlin.run { bgColor }

                view.background = ColorDrawable(color)

    }


    @BindingAdapter(
        value = ["core_circular_image_border_color", "core_circular_image_border_color_factor","core_circular_image_border_width"],
        requireAll = false
    )
    @JvmStatic
    fun setCircularImageViewBorderColor(
        view: CoreCircleImageView,
        _borderColor: Int?,
        _factor: Float? = null,
        _borderWidth: Int?=2,
    ) {
        val bgColor: Int = _borderColor ?: -1
        val color = _factor?.let {
            bgColor.transparent((it.coerceAtMost(1f)))
        } ?: kotlin.run { bgColor }

        view.borderColor = bgColor
        view.borderWidth = _borderWidth ?: 2
    }




    @BindingAdapter(
        value = ["core_material_card_background", "core_material_stroke_background", "is_corner_color_visible"],
        requireAll = false
    )
    @JvmStatic
    fun setMaterialCardDesign(
        materialCardView: MaterialCardView,
        bgColor: Int?,
        strokeColor: Int?,
        hideBorder: Int? = 0
    ) {
        bgColor?.let {
            materialCardView.setCardBackgroundColor(bgColor)
        }
        if (hideBorder == 0) {
            if (strokeColor != null) {
                materialCardView.strokeColor = strokeColor
                materialCardView.strokeWidth = 3
            }
        } else {
            materialCardView.strokeWidth = 0
        }

    }

    @BindingAdapter(value = ["core_tab_color", "core_selected_tab_color"], requireAll = false)
    @JvmStatic
    fun setTabLayoutColor(tabLayout: TabLayout, textColor: Int?, tabSelectedColor: Int?) {
        tabSelectedColor?.let { tabLayout.setSelectedTabIndicatorColor(it) }
        textColor?.let { tabSelectedColor?.let { it1 -> tabLayout.setTabTextColors(it, it1) } }
    }


    @BindingAdapter(
        value = ["core_heading_text_size", "core_heading_text_size_factor"],
        requireAll = false
    )
    @JvmStatic
    fun setHeadingTextSize(view: TextView, _contentTextSize: String?, _contentFactor: Float? = 1f) {
        val contentSize: String =
            if (_contentTextSize.isNullOrEmpty()) "medium" else _contentTextSize
        val contentFactor: Float = _contentFactor ?: 1f


        when {
            "small" in contentSize -> {
                view.setTextSize(
                    TypedValue.COMPLEX_UNIT_PX,
                    view.resources.getDimension(com.intuit.sdp.R.dimen._14sdp) * contentFactor
                )
            }
            "medium" in contentSize -> {
                view.setTextSize(
                    TypedValue.COMPLEX_UNIT_PX,
                    view.resources.getDimension(com.intuit.sdp.R.dimen._16sdp) * contentFactor
                )
            }
            "xlarge" in contentSize -> {
                view.setTextSize(
                    TypedValue.COMPLEX_UNIT_PX,
                    view.resources.getDimension(com.intuit.sdp.R.dimen._19sdp) * contentFactor
                )
            }
            "large" in contentSize -> {
                view.setTextSize(
                    TypedValue.COMPLEX_UNIT_PX,
                    view.resources.getDimension(com.intuit.sdp.R.dimen._17sdp) * contentFactor
                )
            }
        }
    }


    @BindingAdapter(
        value = ["core_sub_heading_text_size", "core_sub_heading_text_size_factor"],
        requireAll = false
    )
    @JvmStatic
    fun setSubHeadingTextSize(
        view: TextView,
        _contentTextSize: String?,
        _contentFactor: Float? = 1f
    ) {
        val contentSize: String =
            if (_contentTextSize.isNullOrEmpty()) "medium" else _contentTextSize
        val contentFactor: Float = _contentFactor ?: 1f
        when {
            "small" in contentSize -> {
                view.setTextSize(
                    TypedValue.COMPLEX_UNIT_PX,
                    view.resources.getDimension(com.intuit.sdp.R.dimen._12sdp) * contentFactor
                )
            }
            "medium" in contentSize -> {
                view.setTextSize(
                    TypedValue.COMPLEX_UNIT_PX,
                    view.resources.getDimension(com.intuit.sdp.R.dimen._14sdp) * contentFactor
                )
            }
            "xlarge" in contentSize -> {
                view.setTextSize(
                    TypedValue.COMPLEX_UNIT_PX,
                    view.resources.getDimension(com.intuit.sdp.R.dimen._17sdp) * contentFactor
                )
            }
            "large" in contentSize -> {
                view.setTextSize(
                    TypedValue.COMPLEX_UNIT_PX,
                    view.resources.getDimension(com.intuit.sdp.R.dimen._15sdp) * contentFactor
                )
            }
        }
    }

//    @BindingAdapter(value = ["core_title_text_size", "core_title_text_size_factor"], requireAll = false)
//    @JvmStatic
//    fun setTitleTextSize(view: View, _contentTextSize: String?, _contentFactor: Float? = 1f) {
//        setCoreContentTextSize(view, _contentTextSize, _contentFactor)
//    }


    @BindingAdapter(
        value = ["core_label_text_size", "core_label_text_size_factor"],
        requireAll = false
    )
    @JvmStatic
    fun setLabelTextSize(view: TextView, _contentTextSize: String?, _contentFactor: Float? = 1f) {
        val contentSize: String =
            if (_contentTextSize.isNullOrEmpty()) "medium" else _contentTextSize
        val contentFactor: Float = _contentFactor ?: 1f
        when {
            "small" in contentSize -> {
                view.setTextSize(
                    TypedValue.COMPLEX_UNIT_PX,
                    view.resources.getDimension(com.intuit.sdp.R.dimen._12sdp) * contentFactor
                )
            }
            "medium" in contentSize -> {
                view.setTextSize(
                    TypedValue.COMPLEX_UNIT_PX,
                    view.resources.getDimension(com.intuit.sdp.R.dimen._14sdp) * contentFactor
                )
            }
            "xlarge" in contentSize -> {
                view.setTextSize(
                    TypedValue.COMPLEX_UNIT_PX,
                    view.resources.getDimension(com.intuit.sdp.R.dimen._17sdp) * contentFactor
                )
            }
            "large" in contentSize -> {
                view.setTextSize(
                    TypedValue.COMPLEX_UNIT_PX,
                    view.resources.getDimension(com.intuit.sdp.R.dimen._15sdp) * contentFactor
                )
            }
        }
    }


    @BindingAdapter(
        value = ["core_text_color", "core_text_color_factor", "core_text_click_enabled",
            "core_text_link_color"],
        requireAll = false
    )
    @JvmStatic
    fun setTextColor(
        view: View,
        _textColor: Int?,
        _alphaFactor: Float? = null,
        _clickEnabled: Boolean? = false,
        _textLinkColor: Int? = -1
    ) {
        val textColor = _textColor ?: -1
        val textLinkColor = _textLinkColor ?: textColor
        val color = _alphaFactor?.let {
            textColor.transparent((it.coerceAtMost(1f)))
        } ?: kotlin.run { textColor }

        val optColor = color.transparent(0.7f)
        val colorStateList = ColorStateList(
            arrayOf(
                intArrayOf(android.R.attr.state_pressed),
                intArrayOf(-android.R.attr.state_enabled),
                StateSet.WILD_CARD
            ),
            intArrayOf(optColor, optColor, color)
        )

        when (view) {
            is TextView -> {
                if (_clickEnabled == true) {
                    view.setTextColor(colorStateList)
                } else {
                    view.setTextColor(color)
                }
                view.setLinkTextColor(textLinkColor)
            }
            is Button -> {
                if (_clickEnabled == true) {
                    view.setTextColor(colorStateList)
                } else {
                    view.setTextColor(color)
                }
            }
            is EditText -> {
                view.setTextColor(color)
            }
            is AppCompatCheckBox -> {
                view.setTextColor(color)
            }

            is RadioButton -> {
                view.setTextColor(color)
            }

        }
    }





    @BindingAdapter(value = ["core_strike_text"], requireAll = true)
    @JvmStatic
    fun setStrikeOut(textView: TextView, isStrike: Boolean?) {
        textView.strikeOut(isStrike = isStrike ?: false)
    }


    @BindingAdapter(
        value = ["core_text_color_text", "core_text_color_factor", "core_text_click_enabled"],
        requireAll = false
    )
    @JvmStatic
    fun setTextColorText(
        view: View,
        _textColor: String?,
        _alphaFactor: Float? = null,
        _clickEnabled: Boolean? = false
    ) =
        setTextColor(view, _textColor.getColor(), _alphaFactor, _clickEnabled, null)

    @BindingAdapter(
        value = ["core_text_icon", "core_text_icon_size", "core_text_icon_size_factor"],
        requireAll = false
    )
    @JvmStatic
    fun setCustomFontText(
        textView: TextView,
        fontCode: String?,
        iconSize: String?,
        _factor: Float?
    ) {

        if (fontCode == "freestamp" || fontCode == "thankstamp"){
            var iconName = ""
            if(fontCode.equals("freestamp")){
                when(CoreMetaData.appLang){
                    "en" ->{
                        iconName = "Free"
                    }
                    "cn" ->{
                        iconName = "谢谢谢"
                    }
                    "sa" ->{
                        iconName = "حرر"
                    }
                    "ru" ->{
                        iconName = "Свободно"
                    }
                    "pt" ->{
                        iconName = "Grátis"
                    }
                    "nl" ->{
                        iconName = "Gratis"
                    }
                    "jp" ->{
                        iconName = "無料"
                    }
                    "it" ->{
                        iconName = "Gratuito"
                    }
                    "fr" ->{
                        iconName = "Gratuit"
                    }
                    "es" ->{
                        iconName = "Gratis"
                    }
                    "de" ->{
                        iconName = "Frei"
                    }
                }
            }
            else if(fontCode.equals("thankstamp")){
                when(CoreMetaData.appLang){
                    "en" ->{
                        iconName = "Thanks!"
                    }
                    "cn" ->{
                        iconName = "谢谢!"
                    }
                    "sa" ->{
                        iconName = "شكر!"
                    }
                    "ru" ->{
                        iconName = "благодаря!"
                    }
                    "pt" ->{
                        iconName = "Obrigado!"
                    }
                    "nl" ->{
                        iconName = "Bedankt!"
                    }
                    "jp" ->{
                        iconName = "ありがとう !"
                    }
                    "it" ->{
                        iconName = "Grazie!"
                    }
                    "fr" ->{
                        iconName = "Merci!"
                    }
                    "es" ->{
                        iconName = "Gracias!"
                    }
                    "de" ->{
                        iconName = "VielenDank!"
                    }
                }
            }
            textView.text = iconName
            return
        }

        textView.setIconFont(fontCode)
        iconSize?.let {
            val contentSize: String = if (it.isEmpty()) "medium" else it
            val factor = _factor ?: 1f
            when {
                "small" in contentSize -> {
                    textView.setTextSize(
                        TypedValue.COMPLEX_UNIT_PX,
                        textView.resources.getDimension(com.intuit.sdp.R.dimen._12sdp) * factor
                    )
                }
                "medium" in contentSize -> {
                    textView.setTextSize(
                        TypedValue.COMPLEX_UNIT_PX,
                        textView.resources.getDimension(com.intuit.sdp.R.dimen._14sdp) * factor
                    )
                }
                "xlarge" in contentSize -> {
                    textView.setTextSize(
                        TypedValue.COMPLEX_UNIT_PX,
                        textView.resources.getDimension(com.intuit.sdp.R.dimen._19sdp) * factor
                    )
                }
                "large" in contentSize -> {
                    textView.setTextSize(
                        TypedValue.COMPLEX_UNIT_PX,
                        textView.resources.getDimension(com.intuit.sdp.R.dimen._17sdp) * factor
                    )
                }
            }
        }
    }

    @JvmStatic
    fun provideIconWidth(context: Context, iconSize: String?, _factor: Float?): Float {
        val factor = _factor ?: 1f
        iconSize?.let {
            val contentSize: String = if (it.isEmpty()) "medium" else it
            when {
                "small" in contentSize -> {
                    return context.resources.getDimensionPixelSize(com.intuit.sdp.R.dimen._12sdp) * factor

                }
                "medium" in contentSize -> {
                    return context.resources.getDimensionPixelSize(com.intuit.sdp.R.dimen._14sdp) * factor
                }
                "xlarge" in contentSize -> {
                    return context.resources.getDimensionPixelSize(com.intuit.sdp.R.dimen._19sdp) * factor
                }
                "large" in contentSize -> {
                    return context.resources.getDimensionPixelSize(com.intuit.sdp.R.dimen._17sdp) * factor
                }

                else -> return context.resources.getDimensionPixelSize(com.intuit.sdp.R.dimen._19sdp) * factor
            }
        }
        return context.resources.getDimensionPixelSize(com.intuit.sdp.R.dimen._19sdp) * factor
    }


    @BindingAdapter(value = ["core_loading_progress_color"], requireAll = true)
    @JvmStatic
    fun setLoadingProgressStyle(view: ProgressBar, progressColor: Int?) {
        view.changeColor(progressColor ?: -1)
    }




    @BindingAdapter(value = ["app:viewIndent", "app:parentViewType"], requireAll = false)
    @JvmStatic
    fun setViewIndent(view: View, _indent: String?, parentViewType: String?) {
        val context = view.context
        val indent = _indent ?: "left"
        when (parentViewType ?: "") {
            "text" -> {
                (view as TextView).gravity =
                    when {
                        indent.equals("right", ignoreCase = true) -> {
                            Gravity.END
                        }
                        indent.equals("left", ignoreCase = true) -> {
                            Gravity.START
                        }
                        indent.equals("center", ignoreCase = true) -> {
                            Gravity.CENTER
                        }
                        indent.equals("justify", ignoreCase = true) -> {
                            Gravity.START
                        }
                        else -> {
                            Gravity.START
                        }
                    }
            }
            "linear" -> {
                val params = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                params.gravity =
                    when {
                        indent.equals("right", ignoreCase = true) -> {
                            Gravity.END
                        }
                        indent.equals("left", ignoreCase = true) -> {
                            Gravity.START
                        }
                        indent.equals("center", ignoreCase = true) -> {
                            Gravity.CENTER
                        }
                        indent.equals("justify", ignoreCase = true) -> {
                            Gravity.START
                        }
                        else -> {
                            Gravity.START
                        }
                    }

                (view as LinearLayout).layoutParams = params
            }
            "relative" -> {
                when {
                    indent.equals("right", ignoreCase = true) -> {
                        (view as RelativeLayout).gravity = Gravity.END
                    }
                    indent.equals("left", ignoreCase = true) -> {
                        (view as RelativeLayout).gravity = Gravity.START
                    }
                    indent.equals("center", ignoreCase = true) -> {
                        (view as RelativeLayout).gravity = Gravity.CENTER
                    }
                    indent.equals("justify", ignoreCase = true) -> {
                        (view as RelativeLayout).gravity = Gravity.START
                    }
                    else -> {
                        (view as RelativeLayout).gravity = Gravity.START
                    }
                }
            }
            "linear_match" -> {
                val params = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                params.gravity =
                    when {
                        indent.equals("right", ignoreCase = true) -> {
                            Gravity.END
                        }
                        indent.equals("left", ignoreCase = true) -> {
                            Gravity.START
                        }
                        indent.equals("center", ignoreCase = true) -> {
                            Gravity.CENTER
                        }
                        indent.equals("justify", ignoreCase = true) -> {
                            Gravity.START
                        }
                        else -> {
                            Gravity.START
                        }
                    }

                (view as LinearLayout).layoutParams = params
            }
        }
    }

    @BindingAdapter(
        value = ["app:coreIconValue", "app:coreIconSize", "app:coreIconSizeFactor", "app:coreIconColor", "app:coreIconColorFactor","app:coreIconCircular","app:corePlaceHolderValue"],
        requireAll = false
    )
    @JvmStatic
    fun setUpCoreIconView(
        view: CoreIconView,
        icon: String? = null,
        iconSize: String? = null,
        iconSizeFactor: Float? = null,
        iconColor: Int? = null,
        iconColorFactor: Float? = null,
        isCircular: Boolean? = null,
        placeholderIcon: String? = null
    ) {
        val drawable = view.context.getDrawableName(placeholderIcon?:"")
        val color = iconColorFactor?.let { factor -> iconColor?.transparent(factor) }
            ?: kotlin.run { iconColor }
        view.updateConfigs(
            icon = icon,
            iconSize = iconSize,
            iconSizeFactor = iconSizeFactor,
            iconColor = color,
            isCircularImage = isCircular ?:false,
            corePlaceHolderIcon = drawable
        )
    }

    @BindingAdapter(
        value = ["core_top_round_corner_border_color", "core_top_round_corner_bg_color", "core_top_round_corner_factor", "core_top_round_corner_bg_color_factor"],
        requireAll = false
    )
    @JvmStatic
    fun setTopRoundCornerViewWithBorder(
        view: View,
        borderColor: Int?,
        bgColor: Int?,
        roundCornerFactor: Float?,
        bgColorFactor: Float?
    ) {

        val borderSize = borderColor?.let {
            view.resources.getDimensionPixelSize(com.intuit.sdp.R.dimen._1sdp)
        } ?: kotlin.run {
            0
        }

        val bg =
            DesignUtil.getRectangularShapeWithTopCorner(
                view.resources.getDimensionPixelSize(com.intuit.sdp.R.dimen._5sdp).toFloat() * (roundCornerFactor
                    ?: 1f),
                borderColor ?: Color.TRANSPARENT,
                bgColor?.transparent(bgColorFactor ) ?: Color.TRANSPARENT,
                borderSize
            )
        view.background = bg

    }


    @BindingAdapter(
        value = ["core_right_round_corner_border_color", "core_right_round_corner_bg_color", "core_right_round_corner_factor"],
        requireAll = false
    )
    @JvmStatic
    fun setRightRoundCornerViewWithBorder(
        view: View,
        borderColor: Int?,
        bgColor: Int?,
        roundCornerFactor: Float?
    ) {

        val borderSize = borderColor?.let {
            view.resources.getDimensionPixelSize(com.intuit.sdp.R.dimen._1sdp)
        } ?: kotlin.run {
            0
        }

        val bg =
            DesignUtil.getRectangularShapeWithRightCorner(
                view.resources.getDimensionPixelSize(com.intuit.sdp.R.dimen._5sdp).toFloat() * (roundCornerFactor
                    ?: 1f),
                borderColor ?: Color.TRANSPARENT,
                bgColor ?: Color.TRANSPARENT,
                borderSize
            )
        view.background = bg
    }


    @BindingAdapter(
        value = ["core_button_background_color", "core_button_round_corner_factor"],
        requireAll = false
    )
    @JvmStatic
    fun setButtonStyle(view: View, buttonBackgroundColor: Int?, cornerFactor: Float? = 1f) {
        val normalBg =
            DesignUtil.getRectangularShape(
                view.resources.getDimensionPixelSize(com.intuit.sdp.R.dimen._5sdp).toFloat() * (cornerFactor
                    ?: 1f),
                Color.TRANSPARENT,
                buttonBackgroundColor ?: Color.TRANSPARENT,
                0
            )


        val selectedBg =
            DesignUtil.getRectangularShape(
                view.resources.getDimensionPixelSize(com.intuit.sdp.R.dimen._5sdp).toFloat() * (cornerFactor
                    ?: 1f),
                Color.TRANSPARENT,
                buttonBackgroundColor?.transparent(0.5f) ?: Color.TRANSPARENT,
                0
            )


        val stateListDrawable = StateListDrawable()
        stateListDrawable.addState(intArrayOf(android.R.attr.state_pressed), selectedBg)
        stateListDrawable.addState(intArrayOf(-android.R.attr.state_enabled), selectedBg)
        stateListDrawable.addState(StateSet.WILD_CARD, normalBg)
        view.background = stateListDrawable
    }

    @BindingAdapter(value = ["core_hint_color", "core_hint_color_factor"], requireAll = false)
    @JvmStatic
    fun setHintColor(view: View, _hintColor: Int?, _alphaFactor: Float? = null) {
        val hintColor = _hintColor ?: -1
        val color = _alphaFactor?.let {
            hintColor.transparent((it.coerceAtMost(1f)))
        } ?: kotlin.run { hintColor }
        when (view) {
            is EditText -> {
                view.setHintTextColor(color)
            }
            is TextView -> {
                view.setHintTextColor(color)
            }
        }
    }

    @BindingAdapter(
        value = ["core_round_corner_border_color", "core_round_corner_bg_color", "core_round_corner_factor", "core_round_corner_border_color_factor", "core_round_corner_bg_color_factor"],
        requireAll = false
    )
    @JvmStatic
    fun setRoundCornerViewWithBorder(
        view: View,
        borderColor: Int?,
        bgColor: Int?,
        roundCornerFactor: Float?,
        borderColorFactor: Float?,
        bgColorFactor: Float?
    ) {
        val borderSize = borderColor?.let {
            1.dpToPx()
        } ?: kotlin.run {
            0
        }

        val designBgColor:Int = bgColor?.transparent(bgColorFactor) ?: Color.TRANSPARENT

        val bg = DesignUtil.getRectangularShape(
            radius = view.resources.getDimensionPixelSize(com.intuit.sdp.R.dimen._5sdp).toFloat() * (roundCornerFactor ?: 1f),
            strokeColor = borderColor?.transparent(borderColorFactor) ?: Color.TRANSPARENT,
            bgColor = designBgColor,
            storkeSize = borderSize
        )
        view.background = bg
    }


    @BindingAdapter(
        value = ["core_radio_active_color", "core_radio_default_color", "core_radio_active_color_factor", "core_radio_default_color_factor"],
        requireAll = false
    )
    @JvmStatic
    fun setCompatRadioButtonStyle(
        view: View,
        activeColor: Int?,
        defaultColor: Int?,
        activeColorFactor: Float? = 1f,
        defaultColorFactor: Float? = 1f
    ) {
        when (view) {
            is AppCompatCheckBox -> {
                view.setColorStyle(
                    (defaultColor ?: -1).transparent(defaultColorFactor ),
                    (activeColor ?: -1).transparent(activeColorFactor)
                )
            }
            is AppCompatRadioButton -> {
                view.setColorStyle(
                    (defaultColor ?: -1).transparent(defaultColorFactor),
                    (activeColor ?: -1).transparent(activeColorFactor)
                )
            }
            is SwitchCompat -> {

                val states = arrayOf(
                    intArrayOf(-android.R.attr.state_checked),
                    intArrayOf(android.R.attr.state_checked)
                )
                val trackColors =
                    intArrayOf(defaultColor ?: Color.WHITE, activeColor ?: Color.GREEN)
//                val thumbColors= intArrayOf(Color.WHITE, Color.WHITE)
                val thumbColors =
                    intArrayOf(defaultColor ?: Color.WHITE, activeColor ?: Color.GREEN)

                DrawableCompat.setTintList(
                    DrawableCompat.wrap(view.thumbDrawable),
                    ColorStateList(states, thumbColors)
                )
                DrawableCompat.setTintList(
                    DrawableCompat.wrap(view.trackDrawable),
                    ColorStateList(states, trackColors)
                )
            }

            is BottomNavigationView -> {
                val activeColorInt = activeColor ?: -1
                val mActiveColor = activeColorFactor?.let {
                    activeColorInt.transparent((it.coerceAtMost(1f)))
                } ?: run { activeColorInt }

                val inactiveColorInt = defaultColor ?: -1
                val mInActiveColor = defaultColorFactor?.let {
                    inactiveColorInt.transparent((it.coerceAtMost(1f)))
                } ?: run { inactiveColorInt }

                val iconsColorStates = ColorStateList(
                    arrayOf(
                        intArrayOf(-android.R.attr.state_checked),
                        intArrayOf(android.R.attr.state_checked)
                    ),
                    intArrayOf(mActiveColor, mInActiveColor)
                )

                val textColorStates = ColorStateList(
                    arrayOf(
                        intArrayOf(-android.R.attr.state_checked),
                        intArrayOf(android.R.attr.state_checked)
                    ),
                    intArrayOf(mActiveColor, mInActiveColor)
                )

                view.itemIconTintList = iconsColorStates
                view.itemTextColor = textColorStates
            }


        }

    }


    @BindingAdapter(value = ["core_underline_text"], requireAll = true)
    @JvmStatic
    fun setUnderlineTextView(view: TextView, isUnderLine: Boolean?) {
        view.paintFlags =
            if (isUnderLine == true) (view.paintFlags or Paint.UNDERLINE_TEXT_FLAG) else (view.paintFlags and Paint.UNDERLINE_TEXT_FLAG.inv())
    }


    @BindingAdapter(
        value = ["app:drawableName", "app:drawableColor", "app:drawableAlpha"],
        requireAll = false
    )
    @JvmStatic
    fun setDrawableColor(view: View, name: String?, color: String?, alpha: Int?) {
        val drawable = view.context.getDrawableName(name ?: "")
        color?.let { drawable.changeDrawableColor(it.getColor()) }

        alpha?.let {
            drawable.setAlpha(it)
        }
        view.background = drawable
    }

    @BindingAdapter(
        value = ["app:card_view_bg_color", "app:card_view_is_rounded", "app:card_view_radius"],
        requireAll = false
    )
    @JvmStatic
    fun setCardViewStyle(cardView: CardView, bgColor: Int?, isRounded: Boolean?, cardRadius: Int?) {
        cardView.setCardBackgroundColor(bgColor ?: "#ffffff".getColor())
        cardView.cardElevation = 10f
//        cardView.background =
//            if (isRounded == true) getRoundedShape(bgColor ?: "#ffffff".getColor()) else getRectangularShape(bgColor ?: "#ffffff".getColor())
        val roundedRadius: Int // = if (isRounded == true) 90 else 18
        val density: Float = cardView.context.resources.displayMetrics.density

        roundedRadius = when {
            density == 0.75f -> {
                // LDPI
                110
            }
            density >= 1.0f && density < 1.5f -> {
                // MDPI
                150
            }
            density == 1.5f -> {
                // HDPI
                150
            }
            density > 1.5f && density <= 2.0f -> {
                // XHDPI
                180
            }
            density > 2.0f && density <= 3.0f -> {
                // XXHDPI
                220
            }
            else -> {
                // XXXHDPI
                330
            }
        }
//            if (Build.VERSION.SDK_INT < 23) 110 else if (Build.VERSION.SDK_INT == 23) 200 else 280
        cardView.radius = cardRadius?.pxToDp()?.toFloat() ?: if (isRounded == true) {
            roundedRadius.pxToDp().toFloat()
        } else {
            20.pxToDp().toFloat()
        }
    }


    @BindingAdapter(value = ["core_card_background", "core_card_circular"], requireAll = false)
    @JvmStatic
    fun setMaterialCardDesign(cardView: CardView, bgColor: Int?, isCircular: Boolean? = false) {
        bgColor?.let {
            cardView.setCardBackgroundColor(bgColor)
        }
        if (isCircular == true) {
            cardView.doOnPreDraw { drawView ->
                cardView.radius = (drawView.width / 2).toFloat()
            }
        }
    }

    @BindingAdapter(
        value = ["core_circular_bg_color", "core_circular_border_color", "core_circular_border_stroke"],
        requireAll = false
    )
    @JvmStatic
    fun setCircularBg(view: View, bgColor: Int?, borderColor: Int?, storke: Int?) {
        view.background = DesignUtil.getCircularBg(borderColor ?: -1, bgColor ?: -1, storke ?: 2)
    }

    @BindingAdapter(value = ["core_cursor_color", "core_cursor_color_factor"], requireAll = false)
    @JvmStatic
    fun setEditTextCursorColor(view: EditText, _cursorColor: Int?, factor: Float?) {
        val cursorColor = _cursorColor ?: Color.TRANSPARENT
        view.setCursorColor(factor?.let { cursorColor.lighter(it) } ?: kotlin.run { cursorColor })
    }



    @BindingAdapter(
        value = ["core_input_active_text_color", "core_input_hint_text_color", "core_input_active_text_color_factor", "core_input_hint_text_color_factor"],
        requireAll = false
    )

    @JvmStatic
    fun setUp(
        view: TextInputLayout,
        activeColor: Int?,
        hintColor: Int?,
        activeColorFactor: Float? = null,
        hintColorFactor: Float? = null
    ) {
        val activeColorToSet = activeColor?.transparent(activeColorFactor) ?: Color.BLACK
        val hintColorToSet = hintColor?.transparent(hintColorFactor) ?: Color.BLACK
        val colorStateList = ColorStateList(
            arrayOf(
                intArrayOf(android.R.attr.state_pressed),
                intArrayOf(android.R.attr.state_focused),
                intArrayOf(-android.R.attr.state_pressed)
            ),
            intArrayOf(activeColorToSet, activeColorToSet, hintColorToSet)
        )
        view.defaultHintTextColor = colorStateList

    }

    @BindingAdapter(
        value = ["app:core_round_icon_name", "app:core_round_icon_color", "app:core_round_icon_bg_color", "app:core_round_icon_bg_stroke_color"],
        requireAll = false
    )
    @JvmStatic
    fun iconWithCircleColor(
        view: TextView,
        iconName: String?,
        iconColor: Int?,
        bgColor: Int?,
        strokeColor: Int?
    ) {
        if (iconName.isNullOrEmpty())
            return
        view.setIconFont(iconName)
        view.setTextColor(iconColor ?: ("#000000").getColor())
        view.background = getRoundedShape(
            30f,
            strokeColor,
            bgColor
        )

    }




    @BindingAdapter(
        value = ["core_view_width"],
        requireAll = true
    )
    @JvmStatic
    fun changeViewWidth(
        view: View,
        viewWidth: String
    ) {

        when (view) {
            is TextView -> {
                if (viewWidth == "wrap") {
                    view.setLayoutParams(
                        LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                        )
                    )
                } else {
                    view.setLayoutParams(
                        LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                        )
                    )
                }
            }
        }
    }

    @BindingAdapter(
        value = ["core_view_margin", "core_view_margin_value"],
        requireAll = true
    )
    @JvmStatic
    fun provideMarginToView(
        view: View,
        isProvideMargin: Boolean? = false,
        marginValue: Int? = 0
    ) {

        if (isProvideMargin == false) {
            return
        }

        when (view) {
            is ConstraintLayout -> {
                val layoutParams: RecyclerView.LayoutParams =
                    view.layoutParams as RecyclerView.LayoutParams
                layoutParams.setMargins(
                    marginValue ?: 8,
                    marginValue ?: 8,
                    marginValue ?: 8,
                    marginValue ?: 8
                )
                view.setLayoutParams(layoutParams)
            }
        }
    }

    @BindingAdapter(value = ["core_deep_link_text"], requireAll = true)
    @JvmStatic
    fun registerDeeplinkOnView(
        view: TextView,
        text: String?
    ) {
        val sourceText = text?.replace("openEmail(", "javascript:openEmail(")?.replace("onclick=", "href=")
        view.text = (sourceText ?: "").stringToHtmlSpannable()
    }

    @BindingAdapter(
        value = ["core_error_view_color", "core_error_view_icon_code"],
        requireAll = false
    )
    @JvmStatic
    fun setErrorView(
        view: TextView,
        errorColor: Int?,
        errorCode: String? = null
    ) {
        view.error404(errorColor, errorCode)
    }

    @BindingAdapter(
        value = ["core_image_tint_color"],
        requireAll = true
    )
    @JvmStatic
    fun setImageTintColor(
        view: View,
        tintColor: Int?
    ) {
        val color = tintColor ?: return
        if (view is ImageView) {
            ImageViewCompat.setImageTintList(view, ColorStateList.valueOf(color))
        }
    }


    @BindingAdapter(
        value = ["core_text_direction"],
        requireAll = true
    )
    @JvmStatic
    fun setTextDirection(
        view: View,
        indent: String?
    ) {
        val alignment = CoreAlignmentType.parse(indent)
        when (view) {
            is TextView -> {
                view.textDirection = View.TEXT_DIRECTION_LTR
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && alignment == CoreAlignmentType.JUSTIFY) {
                    @SuppressLint("InlinedApi")
                    view.justificationMode = LineBreaker.JUSTIFICATION_MODE_INTER_WORD
                } else {
                    view.gravity = alignment.toGravity()
                }
            }
            is EditText -> {
                view.textDirection = View.TEXT_DIRECTION_LTR
                view.gravity = alignment.toGravity()
            }
            is CheckBox -> {
                view.textDirection = View.TEXT_DIRECTION_LTR
                view.gravity = alignment.toGravity()
            }
            is RadioButton -> {
                view.textDirection = View.TEXT_DIRECTION_LTR
                view.gravity = alignment.toGravity()
            }
            is LinearLayout -> {
                view.textDirection = View.TEXT_DIRECTION_LTR
                view.gravity = alignment.toGravity()
                (view.layoutParams as? LinearLayout.LayoutParams)?.gravity = alignment.toGravity()
            }
            is RelativeLayout -> {
                view.textDirection = View.TEXT_DIRECTION_LTR
                view.gravity = alignment.toGravity()
            }
            is FrameLayout -> {
                view.textDirection = View.TEXT_DIRECTION_LTR
                (view.layoutParams as? FrameLayout.LayoutParams)?.gravity = alignment.toGravity()
            }
        }
    }

    @BindingAdapter(value = ["core_string_html_text"], requireAll = false)
    @JvmStatic
    fun setHtmlStringText(view: View, strText: String?) {
        val displayText = strText?.stringToHtml()?.toString()
        when (view) {
            is TextView -> view.text = displayText
            is AppCompatTextView -> view.text = displayText
            is EditText -> view.setText(displayText)
            is AppCompatEditText -> view.setText(displayText)
            is Button -> view.text = displayText
            is AppCompatButton -> view.text = displayText
            is CheckBox -> view.text = displayText
            is AppCompatCheckBox -> view.text = displayText
            is RadioButton -> view.text = displayText
        }
    }

    /*@BindingAdapter(
        value = ["app:quizEditTextColor", "app:quizEditTextHintColor"],
        requireAll = true
    )
    @JvmStatic
    fun setTextColor(editText: EditText, iconColor: String?, hintColor: String?) {

        if (iconColor.isNullOrBlank()) {
            return
        }
        val context = editText.context
        if (!iconColor.isNullOrEmpty()) {
            editText.setTextColor(iconColor.getColor())
            editText.setHintTextColor(hintColor.getColor())
        }
    }*/


    @BindingAdapter(
        value = ["core_et_active_color", "core_et_default_color", "core_et_color_factor"],
        requireAll = false
    )
    @JvmStatic
    fun setEditTextColor(view: View, activeCol: Int?, defCol: Int?, _alphaFactor: Float? = null) {
        val activeCol = activeCol ?: "#000000".getColor()
        val defCol = defCol ?: "#000000".getColor()
        val alfaColor = _alphaFactor?.let {
            defCol.transparent((it.coerceAtMost(1f)))
        } ?: kotlin.run { defCol }

        when (view) {
            is TextInputLayout -> {
                val colorStateList = ColorStateList(
                    arrayOf(
                        intArrayOf(android.R.attr.state_pressed),
                        intArrayOf(android.R.attr.state_focused),
                        intArrayOf(-android.R.attr.state_pressed)
                    ),
                    intArrayOf(activeCol, activeCol, alfaColor)
                )
                view.setDefaultHintTextColor(colorStateList)
                view.editText?.setTextColor(defCol)
            }
            is EditText -> {
                view.setTextColor(alfaColor)
                val colorStateList = ColorStateList(
                    arrayOf(
                        intArrayOf(android.R.attr.state_pressed),
                        intArrayOf(android.R.attr.state_focused),
                        intArrayOf(-android.R.attr.state_pressed)
                    ),
                    intArrayOf(defCol, defCol, alfaColor)
                )
                ViewCompat.setBackgroundTintList(view, colorStateList)
            }
        }
    }

    @BindingAdapter(
        value = ["core_show_more_text", "core_show_less_text", "core_show_lines", "core_show_more_color", "core_show_more_dot_string"],
        requireAll = false
    )
    @JvmStatic
    fun setShowMoreTextViewStyle(
        view: ExpandableTextView,
        showMoreText: String?,
        showLessText: String?,
        lines: Int?,
        actionColor: Int?,
        dotStr: String?=" "
    ) {
        view.setShowMoreTextDot(dotStr ?: " ")
        view.addShowMoreText(showMoreText ?: "")
        view.addShowLessText(showLessText ?: "")
        view.setShowingLine(lines ?: 3)
        view.setShowMoreColor(actionColor ?: Color.BLACK)
        view.setShowLessTextColor(actionColor ?: Color.BLACK)
    }

    @BindingAdapter(value = ["core_shadow_color", "core_reverse_shadow"], requireAll = false)
    @JvmStatic
    fun setShadowBackground(view: View, color: String?, reverse: Boolean?) {
        val startColor = if (reverse == true) color.getColor().transparent(0.25f) else color.getColor().transparent(0.01f)
        val endColor = if (reverse == true) color.getColor().transparent(0.01f) else color.getColor().transparent(0.25f)
        view.background = DesignUtil.getShadowBackground(
            startColor,
            endColor
        )
    }



    @BindingAdapter(value = ["core_cursor_color"], requireAll = true)
    @JvmStatic
    fun setEditTextCursorColor(view: EditText, _cursorColor: Int?) {
        val cursorColor = _cursorColor ?: Color.TRANSPARENT
        view.setCursorColor(cursorColor)
    }


    @BindingAdapter(value = ["core_progress_color", "core_default_color"], requireAll = true)
    @JvmStatic
    fun setRatingProgressStyle(view: ProgressBar, progressColor: Int?, defaultColor: Int?) {
        val progressDrawable = view.progressDrawable as? LayerDrawable
        val bgLayer =
            progressDrawable?.findDrawableByLayerId(android.R.id.background) as? GradientDrawable
        val progressLayer =
            progressDrawable?.findDrawableByLayerId(android.R.id.progress) as? ClipDrawable
        bgLayer?.setColor(defaultColor ?: -1)
        progressLayer?.setColorFilter(progressColor ?: -1, android.graphics.PorterDuff.Mode.SRC_IN)
    }

    @BindingAdapter(
        value = ["core_should_have_margin_start", "core_should_have_margin"],
        requireAll = true
    )
    @JvmStatic
    fun setShouldHaveMarginStart(view: View, dimen: String?, shouldHave: Boolean?) {
        val dimenRaw = dimen ?: "1"
        val dimenResource = view.resources.getIdentifier(
            "_${dimenRaw}sdp",
            "dimen",
            view.context.applicationContext.packageName
        )
        when (val viewLP = view.layoutParams) {
            is ConstraintLayout.LayoutParams -> {
                viewLP.leftMargin =
                    if (dimenResource != 0 && shouldHave == true) view.resources.getDimensionPixelSize(
                        dimenResource
                    ) else 0
            }
        }
    }

    @BindingAdapter(
        value = ["core_border_color", "core_border_size"],
        requireAll = false
    )
    @JvmStatic
    fun setBorderColorToView(view: View, borderColor: Int?, borderSizeFactor: Int?) {
        val borderSize: Int = borderColor?.let {
            borderSizeFactor?.dpToPx() ?: 1.dpToPx()
        } ?: kotlin.run {
            0
        }

        val bg =
            DesignUtil.getRectangularShape(
                radius = 0f,
                strokeColor = borderColor ?: Color.TRANSPARENT,
                bgColor = Color.TRANSPARENT,
                storkeSize = borderSize
            )
        view.background = bg
    }

    @BindingAdapter(
        value = ["core_rating_bar_active_color", "core_rating_bar_inactive_color"],
        requireAll = false
    )
    @JvmStatic
    fun ratingBarColor(view: RatingBar, activeColorStr: Int?, inActiveColorStr: Int?) {
        view.progressBackgroundTintList =
            ColorStateList.valueOf(inActiveColorStr ?: "#ffffff".getColor())
        view.progressTintList = ColorStateList.valueOf(activeColorStr ?: "#000000".getColor())
    }


    @BindingAdapter(value = ["app:roundCardBgColor", "app:isBorderColor", "app:isHideBorder"], requireAll = false)
    @JvmStatic
    fun setCardRoundBgColor(view: View, cardColor: Int?, borderColor: Int?, hideBorder: Int) {

        if (hideBorder > 0) {
            view.background = getRectangularShapeWithStroke(
                radius = 30f,
                strokeWidth = 0,
                strokeColor = borderColor ?: android.R.color.transparent,
                bgColor = cardColor ?: android.R.color.transparent
            )
        } else {
            view.background = getRectangularShapeWithStroke(
                radius = 30f,
                strokeWidth = 5,
                strokeColor = borderColor ?: android.R.color.transparent,
                bgColor = cardColor ?: android.R.color.transparent
            )
        }
    }



    @BindingAdapter(
        value = ["core_rounded_button_background_color", "core_rounded_button_border_color"],
        requireAll = false
    )
    @JvmStatic
    fun setRoundButtonStyle(view: View, buttonBackgroundColor: Int?, borderColor: Int?) {
        view.doOnPreDraw { buttonView ->
            buttonView.background = getPartialRoundedShape(
                buttonView.height.toFloat(), buttonView.height.toFloat(), buttonView.height.toFloat(), buttonView.height.toFloat(),
                borderColor ?: (buttonBackgroundColor ?: Color.BLACK),
                buttonBackgroundColor ?: Color.BLACK
            )
        }
    }

    @BindingAdapter(value = ["core_image_drawable_color"])
    @JvmStatic
    fun setImageDrawableColor(view: ImageView, _cursorColor: Int?) {
        val layerDrawable: Drawable = view.drawable
        _cursorColor?.let {
            layerDrawable.setColorFilter(
                _cursorColor,
                PorterDuff.Mode.SRC_ATOP
            )
        }
    }

    @BindingAdapter(value = ["app:drawableColor"])
    @JvmStatic
    fun setCardRoundBgColor(view: TextView, drawableColor: Int?) {
        for (drawable in view.getCompoundDrawables()) {
            if (drawable != null) {
                drawable.colorFilter = drawableColor?.let { PorterDuffColorFilter(it, PorterDuff.Mode.SRC_IN) }
            }
        }
    }

    @BindingAdapter(value = ["core_drawable_color"])
    @JvmStatic
    fun setDrawableColor(view: View, _cursorColor: Int?) {
        val layerDrawable: Drawable = view.background as Drawable
        _cursorColor?.let {
            layerDrawable.setColorFilter(
                _cursorColor,
                PorterDuff.Mode.SRC_ATOP
            )
        }
    }

    @BindingAdapter(value = ["app:cardBgColor", "app:isBorderColor", "app:isHideBorder", "app:borderWidth"], requireAll = false)
    @JvmStatic
    fun setCardRoundBgColor(view: View, cardColor: Int?, borderColor: Int?, hideBorder: Int, borderWidth: Int?) {
        val radius = view.resources.getDimensionPixelSize(com.intuit.sdp.R.dimen._5sdp).toFloat() * 3
        if (hideBorder > 0) {
            view.background = getRectangularShapeWithStroke(
                radius,
                0,
                borderColor ?: android.R.color.transparent,
                cardColor ?: android.R.color.transparent
            )
        } else {
            view.background = getRectangularShapeWithStroke(
                radius,
                borderWidth ?: 1,
                borderColor ?: android.R.color.transparent,
                cardColor ?: android.R.color.transparent
            )
        }
    }


    @BindingAdapter(
        value = ["core_input_layout_icon_direction"],
        requireAll = true
    )
    @JvmStatic
    fun setTextInputLayoutIconDirection(
        view: TextView,
        indent: String?
    ) {
        when (indent) {
            "right" -> {
                val layoutParams = view?.getLayoutParams() as RelativeLayout.LayoutParams
                if (isRTLLocale()) {
                    layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE)
                } else {
                    layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE)
                }
                layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE)
                view?.setLayoutParams(layoutParams)
            }
            "left" -> {
                val layoutParams = view?.getLayoutParams() as RelativeLayout.LayoutParams
                if (isRTLLocale()) {
                    layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE)
                } else {
                    layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE)
                }
                layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE)
                view?.setLayoutParams(layoutParams)
            }
        }
    }

    @BindingAdapter(
        value = ["core_input_border_active_text_color", "core_input_border_hint_text_color", "core_input_border_active_text_color_factor", "core_input_border_hint_text_color_factor"],
        requireAll = false
    )

    @JvmStatic
    fun setUpTextInputWithBorderColor(
        view: TextInputLayout,
        activeColor: Int?,
        hintColor: Int?,
        activeColorFactor: Float? = null,
        hintColorFactor: Float? = null
    ) {
        val activeColorToSet = activeColor?.transparent(activeColorFactor) ?: Color.BLACK
        val hintColorToSet = hintColor?.transparent(hintColorFactor) ?: Color.BLACK
        val colorStateList = ColorStateList(
            arrayOf(
                intArrayOf(android.R.attr.state_pressed),
                intArrayOf(android.R.attr.state_focused),
                intArrayOf(-android.R.attr.state_pressed)
            ),
            intArrayOf(activeColorToSet, activeColorToSet, hintColorToSet)
        )
        view.defaultHintTextColor = colorStateList
        view.setBoxStrokeColorStateList(colorStateList)
    }

    @BindingAdapter("app:core_add_view_rule")
    @JvmStatic
    fun coreAddViewRules(view: RelativeLayout, indent: String?) {
        val lp = view.layoutParams as RelativeLayout.LayoutParams
        indent?.let {
            when (it) {
                "top" -> {
                    lp.addRule(RelativeLayout.ALIGN_PARENT_TOP)
                    view.layoutParams = lp
                }
                "bottom" -> {
                    lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM)
                    view.layoutParams = lp
                }
                "left" -> {
                    lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT)
                    view.layoutParams = lp
                }
                "right" -> {
                    lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT)
                    view.layoutParams = lp
                }
                "middle" -> {
                    lp.addRule(RelativeLayout.CENTER_IN_PARENT)
                    view.layoutParams = lp
                }
                "center" -> {
                    lp.addRule(RelativeLayout.CENTER_IN_PARENT)
                    view.layoutParams = lp
                }

            }
        }
    }

}