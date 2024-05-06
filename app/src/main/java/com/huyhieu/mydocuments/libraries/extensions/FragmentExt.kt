package com.huyhieu.mydocuments.libraries.extensions

import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.fragment.app.Fragment

fun Fragment.setOnBackPressed(onBackPressed: (OnBackPressedCallback.() -> Unit) = {/*Do nothing*/ }) {
//    activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
//        override fun handleOnBackPressed() {
//            onBack()
//        }
//    })
    activity?.onBackPressedDispatcher?.addCallback(this.viewLifecycleOwner) {
        onBackPressed(this)
    }
}

fun Fragment.showToastShort(msg: String?) {
    context.showToastShort(msg)
}