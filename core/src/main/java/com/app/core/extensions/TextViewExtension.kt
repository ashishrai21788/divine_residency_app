package com.app.core.extensions

import android.content.pm.ActivityInfo
import android.graphics.Paint
import android.view.Gravity
import android.view.View
import android.widget.TextView
import com.app.core.utils.DesignUtil

fun TextView.strikeOut(isStrike: Boolean) {
    this.apply {
        paintFlags = if (isStrike)
            paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        else {
            paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
        }
    }
}

fun TextView.setIconFont(name: String?) {
//    if (name.isNullOrBlank()) return
//    val font: Typeface = Typeface.SERIF ?: Typeface.DEFAULT
//    this.includeFontPadding = false
//    val updatedName: String = name.replace("-", "_")
//    when {
//        updatedName.contains("payement") -> {
//            this.typeface = try {
//                ResourcesCompat.getFont(context, R.font.appycustompayment_font) ?: font
//            } catch (e: Exception) {
//                return
//            }
//        }
//        updatedName.contains("iconz") -> {
//            this.typeface = try {
//                ResourcesCompat.getFont(context, R.font.iconz_font) ?: font
//            } catch (e: Exception) {
//                return
//            }
//            val cropIssueInIcons = arrayOf("iconz_hirecab", "iconz_hirecab", "iconz_catering", "iconz_catering", "iconz_user_add")
//
//            when {
//                updatedName.contains("iconz_newspaper") -> {
//                    this.setPadding(15, 15, 18, 15)
//                }
//                updatedName.contains("iconz_click_call") -> {
//                    this.setPadding(16, 12, 15, 15)
//                }
//                updatedName.contains("iconz-realestate") -> {
//                    this.setPadding(0, 0, 5, 0)
//                    logReport("this.paddingRight : " + this.paddingRight + "   ;  this.paddingLeft : " + this.paddingLeft)
//                }
//                updatedName.contains("iconz_scuba_diving") -> {
//                    this.setPadding(16, 15, 15, 15)
//                }
//                (cropIssueInIcons.any { ic ->
//                    updatedName.contains(ic)
//                }) -> {
//                    this.setPadding(0, 0, 0, 0)
//                    this.includeFontPadding = true
//                    this.layoutParams.let { layoutP ->
//                        layoutP.width = 300
//                        layoutP.height = 300
//                    }
//                }
//                else -> {
//                    // commenting this as told by Dharmendra sir
////                    this.setPadding(15, 0, 18, 0)
//                }
//            }
//
////        }
////        //1em 1.6%
////        updatedName.startsWith("icon", ignoreCase = true) && !updatedName.contains("iconz") -> this.typeface = try {
////            ResourcesCompat.getFont(context, R.font.icon_font) ?: font
////        } catch (e: Exception) {
////            return
////        }
////        //1em
////        updatedName.contains("appyicon") -> this.typeface = try {
////            ResourcesCompat.getFont(context, R.font.appyicon_font) ?: font
////        } catch (e: Exception) {
////            return
////        }
//        //fontsize
////        updatedName.contains("appyslim") -> this.typeface =
////            try {
////                ResourcesCompat.getFont(context, R.font.appyslim_font) ?: font
////            } catch (e: Exception) {
////                return
////            }
////        updatedName.contains("appynative") -> this.typeface = try {
////            ResourcesCompat.getFont(context, R.font.nativeui) ?: font
////        } catch (e: Exception) {
////            return
////        }
//
//    }
//
//
////    try {
////        var resourceId = resources.getIdentifier(name.replace("-", "_"), "string", context.packageName)
////        if (resourceId == 0) {
////            resourceId = resources.getIdentifier("fail", "string", context.packageName)
////        }
////        text = resources.getString(resourceId)
////    } catch (e: java.lang.Exception) {
////    } finally {
////        textDirection = View.TEXT_DIRECTION_LTR
////    }
//}
//
//
//@Deprecated("use error404 without drawable TextView.error404(optionalColor: Int? = null)")
//fun TextView.error404(drawable: Int, optionalColor: Int? = null) {
//    this.setIconFont("appyicon_no_data")
//    this.gravity = Gravity.CENTER
//    if (context?.resources?.configuration?.orientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
//        this.width = context?.getDeviceDimensions()?.second?.div(3) ?: 0
//        this.height = context?.getDeviceDimensions()?.second?.div(3) ?: 0
//        this.textSize = 140f
//    } else {
//        this.textSize = 70f
//        this.width = context?.getDeviceDimensions()?.first?.div(6) ?: 0
//        this.height = context?.getDeviceDimensions()?.first?.div(6) ?: 0
//    }
//
////    this.background = resources.getDrawable(drawable, context?.theme)
////        .changeDrawableColor(
////            optionalColor ?: ((this.context as Activity).coreManifest().appData.pageIconColor
////                ?: "#ffffff").getColor()
////        )
////    this.setTextColor(
////        optionalColor ?: ((this.context as Activity).coreManifest().appData.pageIconColor
////            ?: "#ffffff").getColor()
////    )
}


fun TextView.error404(optionalColor: Int? = null, iconCode: String? = null) {
    this.setIconFont(iconCode ?: "appyicon_no_data")
    this.gravity = Gravity.CENTER
    if (context?.resources?.configuration?.orientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
        this.width = context?.getDeviceDimensions()?.second?.div(3) ?: 0
        this.height = context?.getDeviceDimensions()?.second?.div(3) ?: 0
        this.textSize = 140f
    } else {
        this.textSize = 70f
        this.width = context?.getDeviceDimensions()?.first?.div(6) ?: 0
        this.height = context?.getDeviceDimensions()?.first?.div(6) ?: 0
    }

//    this.background = resources.getDrawable(R.drawable.core_404_circle, context?.theme)
//        .changeDrawableColor(
//            optionalColor ?: ((this.context as Activity).coreManifest().appData.pageIconColor
//                ?: "#ffffff").getColor()
//        )
//    this.setTextColor(
//        optionalColor ?: ((this.context as Activity).coreManifest().appData.pageIconColor
//            ?: "#ffffff").getColor()
//    )
}

fun TextView.error404SquareCrossSymbol(optionalColor: Int? = null) {
    this.setIconFont("appynative_nodata")
    this.gravity = Gravity.CENTER
    if (context?.resources?.configuration?.orientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
        this.width = context?.getDeviceDimensions()?.second?.div(3) ?: 0
        this.height = context?.getDeviceDimensions()?.second?.div(3) ?: 0
        this.textSize = 140f
    } else {
        this.textSize = 70f
        this.width = context?.getDeviceDimensions()?.first?.div(6) ?: 0
        this.height = context?.getDeviceDimensions()?.first?.div(6) ?: 0
    }

//    this.setTextColor(
//        optionalColor ?: ((this.context as Activity).coreManifest().appData.pageIconColor
//            ?: "#ffffff").getColor()
//    )
}

fun TextView.errorLock(optionalColor: Int? = null) {
    this.setIconFont("iconz_lock")
    this.gravity = Gravity.CENTER
    if (context?.resources?.configuration?.orientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
        this.width = context?.getDeviceDimensions()?.second?.div(3) ?: 0
        this.height = context?.getDeviceDimensions()?.second?.div(3) ?: 0
        this.textSize = 140f
    } else {
        this.textSize = 70f
        this.width = context?.getDeviceDimensions()?.first?.div(6) ?: 0
        this.height = context?.getDeviceDimensions()?.first?.div(6) ?: 0
    }

//    this.background = resources.getDrawable(R.drawable.core_404_circle, context?.theme)
//        .changeDrawableColor(
//            optionalColor ?: ((this.context as Activity).coreManifest().appData.pageIconColor
//                ?: "#ffffff").getColor()
//        )
//    this.setTextColor(
//        optionalColor ?: ((this.context as Activity).coreManifest().appData.pageIconColor
//            ?: "#ffffff").getColor()
//    )
}

fun TextView.applyToolBarTextSize(size: String?) {
    textSize = when {
        size.equals("largeHeaderBar", ignoreCase = true) -> 26f
        size.equals("mediumHeaderBar", ignoreCase = true) -> 20f
        size.equals("smallHeaderBar", ignoreCase = true) -> 19f
        size.equals("xlargeHeaderBar", ignoreCase = true) -> 31f
        else -> 20f
    }


}

//======================= Woocommerce =============================================================
fun View.setCircularBg(view: View, bgColor: Int?, borderColor: Int?, storke: Int?) {
    view.background = DesignUtil.getCircularBg(borderColor ?: -1, bgColor ?: -1, storke ?: 2)
}

fun TextView.setFont(textView: TextView, value: String) {
//    textView.context.getFont(value) { font, _ ->
//        textView.typeface = font
//    }
}


//.appynative-nodata:before {
//  content: "\3f";
//}
//.appynative-uservoice:before {
//  content: "\40";
//}





