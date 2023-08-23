package com.huyhieu.mydocuments.libraries.utils

import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.annotation.AnimRes

fun View.setAnimation(
    @AnimRes animId: Int,
    onStart: ((Animation?) -> Unit)? = null,
    onRepeat: ((Animation?) -> Unit)? = null,
    onEnd: ((Animation?) -> Unit)? = null
) {
    val animSlideDown = AnimationUtils.loadAnimation(context, animId)
    animSlideDown.onAnimationListener(onStart, onRepeat, onEnd)
    this.startAnimation(animSlideDown)
}

fun Animation.onAnimationListener(
    onStart: ((Animation?) -> Unit)? = null,
    onRepeat: ((Animation?) -> Unit)? = null,
    onEnd: ((Animation?) -> Unit)? = null
): Animation {
    this.setAnimationListener(object : Animation.AnimationListener {
        override fun onAnimationStart(animation: Animation?) {
            onStart?.invoke(animation)
        }

        override fun onAnimationEnd(animation: Animation?) {
            onEnd?.invoke(animation)
        }

        override fun onAnimationRepeat(animation: Animation?) {
            onRepeat?.invoke(animation)
        }

    })
    return this
}