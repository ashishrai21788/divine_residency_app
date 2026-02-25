package com.app.core.extensions.ui

import android.app.Activity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.app.core.utils.CoreComponentProvider


inline fun <reified V : ViewModel> FragmentActivity.getViewModel(
    key: String? = null,
    noinline factory: () -> V
): V {
    @Suppress("UNCHECKED_CAST")
    val viewModelProviderFactory = object : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T = factory() as T
    }

    return if (key != null) {
        ViewModelProviders.of(this, viewModelProviderFactory).get(key, V::class.java)
    } else {
        ViewModelProviders.of(this, viewModelProviderFactory).get(V::class.java)
    }
}

fun Activity.showToastL(message: String?) {
    this.showToastL(message)
}
fun Activity.coreComponent() =
    (applicationContext as? CoreComponentProvider)?.provideCoreComponent()
        ?: throw IllegalStateException("CoreComponentProvider not implemented: $applicationContext")


fun Activity.showToastS(message: String?) {
    this.showToastS(message)
}
fun <T>  FragmentActivity.isFragmentExists(fragmentClass: Class<T>): Boolean {
    return supportFragmentManager
        .fragments
        .any { currentFragment->  currentFragment.tag.equals(fragmentClass.simpleName,true) }
}