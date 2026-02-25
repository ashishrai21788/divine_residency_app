package com.app.core.extensions

import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.app.core.R
import kotlin.math.roundToLong

fun RecyclerView.applyLayoutRightInAnimation(factor: Float = 1f, loadAnimationWithAlpha: Boolean = true) {
    this.layoutAnimation = AnimationUtils.loadLayoutAnimation(context, if(loadAnimationWithAlpha) R.anim.core_layout_right_in_controller else R.anim.core_layout_right_in_controller_without_alpha)
    this.layoutAnimation.animation.duration = (this.layoutAnimation.animation.duration * factor).roundToLong()
}

fun RecyclerView.applyLayoutLeftInAnimation(factor: Float = 1f, loadAnimationWithAlpha: Boolean = true) {
    this.layoutAnimation = AnimationUtils.loadLayoutAnimation(context, if(loadAnimationWithAlpha) R.anim.core_layout_left_in_controller else R.anim.core_layout_left_in_controller_without_alpha)
    this.layoutAnimation.animation.duration = (this.layoutAnimation.animation.duration * factor).roundToLong()
}


fun RecyclerView.applyLayoutBottomInAnimation(factor: Float = 1f, loadAnimationWithAlpha: Boolean = true) {
    this.layoutAnimation = AnimationUtils.loadLayoutAnimation(context, if(loadAnimationWithAlpha) R.anim.core_layout_bottom_in_controller else R.anim.core_layout_bottom_in_controller_without_alpha)
    this.layoutAnimation.animation.duration = (this.layoutAnimation.animation.duration * factor).roundToLong()
}

fun RecyclerView.applyLayoutTopInAnimation(factor: Float = 1f, loadAnimationWithAlpha: Boolean = true) {
    this.layoutAnimation = AnimationUtils.loadLayoutAnimation(context, if(loadAnimationWithAlpha) R.anim.core_layout_top_in_controller else R.anim.core_layout_top_in_controller_without_alpha)
    this.layoutAnimation.animation.duration = (this.layoutAnimation.animation.duration * factor).roundToLong()
}

fun RecyclerView.applyLayoutMinAnimation(factor: Float = 1f, loadAnimationWithAlpha: Boolean = true) {
    this.layoutAnimation = AnimationUtils.loadLayoutAnimation(context, if(loadAnimationWithAlpha) R.anim.core_layout_min_controller else R.anim.core_layout_min_controller_without_alpha)
    this.layoutAnimation.animation.duration = (this.layoutAnimation.animation.duration * factor).roundToLong()
}

fun RecyclerView.applyLayoutMaxAnimation(factor: Float = 1f, loadAnimationWithAlpha: Boolean = true) {
    this.layoutAnimation = AnimationUtils.loadLayoutAnimation(context, if(loadAnimationWithAlpha) R.anim.core_layout_max_controller else R.anim.core_layout_max_controller_without_alpha)
    this.layoutAnimation.animation.duration = (this.layoutAnimation.animation.duration * factor).roundToLong()
}

fun RecyclerView.applyLayoutAnimation(type: String?, factor: Float = 1f, loadAnimationWithAlpha: Boolean = true) {
    when (type) {
        "right" -> {
            this.applyLayoutRightInAnimation(factor, loadAnimationWithAlpha)
        }
        "left" -> {
            this.applyLayoutLeftInAnimation(factor, loadAnimationWithAlpha)
        }
        "top" -> {
            this.applyLayoutTopInAnimation(factor, loadAnimationWithAlpha)
        }
        "bottom" -> {
            this.applyLayoutBottomInAnimation(factor, loadAnimationWithAlpha)
        }
        "min" -> {
            this.applyLayoutMinAnimation(factor, loadAnimationWithAlpha)
        }
        "max" -> {
            this.applyLayoutMaxAnimation(factor, loadAnimationWithAlpha)
        }

    }
}
