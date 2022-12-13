package com.huyhieu.library.extensions

import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment

fun Fragment.handleBackPressedFragment(onBack: (() -> Unit) = {/*Do nothing*/ }) {
    activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            onBack()
        }
    })
}

fun Fragment.showToastShort(msg: String?) {
    context.showToastShort(msg)
}