package com.huyhieu.mydocuments.base.interfaces

import androidx.navigation.NavDirections
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.navOptions
import androidx.viewbinding.ViewBinding

interface INavigationController<VB : ViewBinding> : IParams<VB>{

    fun childNavigate(
        navHostFragment: NavHostFragment,
        directions: NavDirections,
        navOptionBuilder: (NavOptionsBuilder.() -> Unit)? = null
    ) {
        val navOpt = navOptionBuilder?.let { navOptions { it() } }
        navHostFragment.navController.navigate(directions, navOpt)
    }
}