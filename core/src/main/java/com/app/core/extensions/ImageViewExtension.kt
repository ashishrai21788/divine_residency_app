package com.app.core.extensions

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.widget.ImageView
import com.app.core.bindingadapter.CoreBindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import java.io.File


fun ImageView.setImageUrl(url: String, radius: Int) {

    Glide.with(this.context)
        .load(url)
        .transform(CenterCrop(), RoundedCorners(radius))
        .into(this)
}

fun ImageView.setImageFileUri(file: File, radius: Int) {

    Glide.with(this.context)
        .load(file)
        .transform(CenterCrop(), RoundedCorners(radius))
        .into(this)
}


fun ImageView.setImageUrlForQuiz(url: String, radius: Int) {

    Glide.with(this.context)
        .load(url)
        .fitCenter()
        .into(this)
}


fun ImageView.setDefaultImage(url: String, defDrwable: Drawable, radius: Int) {

    Glide.with(this.context)
        .load(
            if (url != "") {
                url
            } else {
                defDrwable
            }
        )
        .fitCenter()
        .placeholder(defDrwable)
        .into(this)
}
fun ImageView.setImageWithDefault(url: String?, defDrwable: Drawable?) {
    Glide.with(this.context)
        .applyDefaultRequestOptions( RequestOptions()
        .placeholder(defDrwable)
        .error(defDrwable))
        .load(url)
        .into(this)
}

fun ImageView.setRoundCornerImage(url: String, defDrwable: Drawable?, radius: Int) {
    Glide.with(this.context)
        .load(
            if (url != "") {
                url
            } else {
                defDrwable
            }
        )
        .transform(CenterCrop(), RoundedCorners(radius))
        .placeholder(defDrwable)
        .into(this)
}

fun ImageView.setImageFileBitmap(bitmap: Bitmap, radius: Int) {

    Glide.with(this.context)
        .asBitmap()
        .load(bitmap)
        .transform(CenterCrop(), RoundedCorners(radius))
        .into(this)
}

//fun ImageView.setDefaultImage(url: String, defDrwable: Drawable, radius: Int) {
//
//    Glide.with(this.context)
//        .load(
//            if (url != "") {
//                url
//            } else {
//                defDrwable
//            }
//        )
//        .transform(CenterCrop(), RoundedCorners(radius))
//        .placeholder(defDrwable)
//        .into(this)
//}

fun ImageView.setDefaultImageCircle(url: String, defDrwable: Drawable) {

    Glide.with(this.context)
        .load(
            if (url != "") {
                url
            } else {
                defDrwable
            }
        )
        .apply(RequestOptions.circleCropTransform())
        .placeholder(defDrwable)
        .into(this)
}

fun ImageView.setDefaultImageCircle(url: Bitmap, defDrwable: Drawable) {
    Glide.with(this.context)
        .load(url)
        .apply(RequestOptions.circleCropTransform())
        .placeholder(defDrwable)
        .into(this)
}

fun ImageView.setDefaultImageIcon(url: String?) {
    Glide.with(this.context)
        .load(url)
        .into(this)
}

fun ImageView.setImageCircle(url: String) {
    Glide.with(this.context)
        .load(url)
        .apply(RequestOptions.circleCropTransform())
        .into(this)
}

fun ImageView.loadImageUrl(url: String?) {
//    CoreBindingAdapter.setImageFromUrlOrDrawable(imageView = this, imageUrl = url)
}

fun ImageView.loadImageUri(url: Uri?) {
    try {
        Glide.with(this)
            .load(url)
            .into(this)
    } catch (e: Exception) {
    }

}