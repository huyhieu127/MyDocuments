package com.huyhieu.mydocuments.navigation

import android.app.Activity
import androidx.fragment.app.Fragment
import androidx.navigation.*
import androidx.navigation.fragment.findNavController
import com.huyhieu.mydocuments.R


/**
 * Navigation in fragment*/
enum class MyNavHost(val navHostId: Int) {
    Main(R.id.navHostMain),
    Home(R.id.navHostHome)
}

/**Navigate extensions*/
fun Fragment?.navigate(
    myNavHost: MyNavHost,
    navDirections: NavDirections,
    navOptionsBuilder: (NavOptions.Builder.(navController: NavController) -> Unit)? = null
) {
    this ?: return
    activity?.navigate(myNavHost, navDirections, navOptionsBuilder)
}

fun Fragment?.navigate(
    navDirections: NavDirections,
    navOptionsBuilder: (NavOptions.Builder.(navController: NavController) -> Unit)? = null
) {
    this ?: return
    findNavController().navigate(navDirections, navOptionsBuilder)
}

/**
 * Navigation in activity*/
fun Activity?.navigate(
    myNavHost: MyNavHost,
    navDirections: NavDirections,
    navOptionsBuilder: (NavOptions.Builder.(navController: NavController) -> Unit)? = null
) {
    this ?: return
    findNavController(myNavHost.navHostId).navigate(navDirections, navOptionsBuilder)
}

/**
 * Navigation*/
fun NavController.navigate(
    navDirections: NavDirections,
    navOptionsBuilder: (NavOptions.Builder.(navController: NavController) -> Unit)?
) {
    var navOptions: NavOptions.Builder? = null
    if (navOptionsBuilder != null) {
        navOptions = NavOptions.Builder()

        navOptionsBuilder.invoke(navOptions, this@navigate)
    }
    navigate(navDirections, navOptions?.build())
}

fun NavOptionsBuilder.clearBackStack(
    navController: NavController,
    inclusive: Boolean = true,
    saveState: Boolean = false
) {
    launchSingleTop = true
    popUpTo(navController.graph.id) {
        this.inclusive = inclusive
        this.saveState = saveState
    }
}
