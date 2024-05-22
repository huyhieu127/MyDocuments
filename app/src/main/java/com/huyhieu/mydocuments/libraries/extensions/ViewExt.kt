package com.huyhieu.mydocuments.libraries.extensions

import android.animation.ValueAnimator
import android.view.View
import android.view.ViewGroup
import androidx.core.view.updateLayoutParams

internal var time = 0L
fun View.setOnClickMyListener(delay: Long = 500L, onClick: () -> Unit) {
    setOnClickListener {
        val currentTime = System.currentTimeMillis()
        if (currentTime - time > delay) {
            time = currentTime
            //logDebug("setOnClickMyListener: ${this.getNameById(id)}")
            onClick()
        }
    }
}

fun View.getNameById(id: Int): String {
    return try {
        this.resources.getResourceEntryName(this.id)
    } catch (ex: Exception) {
        ""
    }
}


fun View.animateHeight(toHeight: Int, fromHeight: Int = height, duration: Long = 0L) {
    val valueAnimator = ValueAnimator.ofInt(fromHeight, toHeight)
    if (duration != 0L) valueAnimator.duration = duration
    valueAnimator.addUpdateListener { animator ->
        val value = animator.animatedValue as Int
        updateLayoutParams<ViewGroup.LayoutParams> {
            height = value
        }
    }

    valueAnimator.start()
}