package com.huyhieu.mydocuments.navigation

import android.app.Activity
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
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
 * NavController*/
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

/**
 * @param destinationId is Fragment ID*/
fun Fragment.popBackStackTo(
    myNavHost: MyNavHost,
    @IdRes destinationId: Int = 0,
    inclusive: Boolean = false
) {
    activity.popBackStackTo(myNavHost, destinationId, inclusive)
}

fun Fragment.popBackStackTo(@IdRes destinationId: Int = 0, inclusive: Boolean = false) {
    if (destinationId != 0) {
        findNavController().popBackStack(destinationId, inclusive)
    } else {
        findNavController().popBackStack()
    }
}

fun Activity?.popBackStackTo(
    myNavHost: MyNavHost, @IdRes destinationId: Int = 0, inclusive: Boolean = false
) {
    this ?: return
    if (destinationId != 0) {
        findNavController(myNavHost.navHostId).popBackStack(destinationId, inclusive)
    } else {
        findNavController(myNavHost.navHostId).popBackStack()
    }
}

fun NavController.popBackStackTo(@IdRes destinationId: Int = 0, inclusive: Boolean = false) {
    if (destinationId != 0) {
        popBackStack(destinationId, inclusive)
    } else {
        popBackStack()
    }
}

fun NavOptions.Builder.clearBackStack(
    navController: NavController,
    inclusive: Boolean = true,
    saveState: Boolean = false
) {
    setLaunchSingleTop(true)
    setPopUpTo(navController.graph.id, inclusive, saveState)
}
