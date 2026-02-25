package com.app.core.extensions.ui

import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.app.core.extensions.showToastL
import com.app.core.extensions.showToastS

inline fun <reified V : ViewModel> Fragment.getViewModel(
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


fun Fragment.showToastL(message: String?) {
    context?.showToastL(message)
}

fun Fragment.showToastS(message: String?) {
    context?.showToastS(message)
}

fun Fragment.isSafe(): Boolean {
    return !(this.isRemoving || this.activity == null || this.isDetached || !this.isAdded || this.view == null)
}

fun Fragment.coreComponent() = requireActivity().coreComponent()

inline fun <I,O>Fragment.requestContractPermission(contract: ActivityResultContract<I, O>, crossinline action: (O)-> Unit): ActivityResultLauncher<I>{
    return registerForActivityResult(contract){
        if(it == null) return@registerForActivityResult
        if(it is Boolean && it == false) return@registerForActivityResult
        if(it is List<*> && it.isEmpty()) return@registerForActivityResult
        action(it)
    }
}
