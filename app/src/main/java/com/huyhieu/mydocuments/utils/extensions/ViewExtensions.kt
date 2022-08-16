package com.huyhieu.mydocuments.utils.extensions

import android.view.View
import androidx.navigation.NavDirections
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.findNavController
import androidx.navigation.navOptions
import com.huyhieu.mydocuments.utils.logDebug


fun View?.navigate(
    directions: NavDirections,
    navOptionBuilder: (NavOptionsBuilder.() -> Unit)? = null
) {
    this ?: return
    val navOpt = navOptionBuilder?.let { navOptions { it() } }
    findNavController().navigate(directions, navOpt)
}

fun View.setOnClickMyListener(delay: Long = 1000L, onClick: () -> Unit) {
    var time = System.currentTimeMillis()
    setOnClickListener {
        val currentTime = System.currentTimeMillis()
        if (currentTime - time > delay) {
            time = currentTime
            logDebug("setOnClickMyListener: ${this.getNameById(id)}")
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