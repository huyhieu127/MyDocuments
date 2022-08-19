package com.huyhieu.mydocuments.utils.extensions

import android.app.Activity
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import androidx.navigation.*
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.huyhieu.mydocuments.R
import com.huyhieu.mydocuments.utils.logDebug


fun Activity?.navigate(
    directions: NavDirections,
    navOptions: NavOptions? = null
) {
    this ?: return
    findNavController(R.id.navHostMain).navigate(directions, navOptions)
}

fun Activity?.popBackStack() {
    this ?: return
    findNavController(R.id.navHostMain).popBackStack()
}

fun FragmentContainerView?.childNavigate(
    directions: NavDirections,
    navOptions: NavOptions? = null
){
    this ?: return
    findNavController().navigate(directions, navOptions)
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