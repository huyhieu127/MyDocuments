package com.huyhieu.mydocuments.ui.fragments.navigation

import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.huyhieu.mydocuments.R
import com.huyhieu.mydocuments.base.BaseFragment
import com.huyhieu.mydocuments.databinding.FragmentNavigationBinding
import com.huyhieu.mydocuments.navigation.MyNavHost
import com.huyhieu.mydocuments.navigation.directions.HomeDirections
import com.huyhieu.mydocuments.navigation.navigate
import kotlinx.coroutines.launch

class NavigationFragment : BaseFragment<FragmentNavigationBinding>() {
    override fun initializeBinding() = FragmentNavigationBinding.inflate(layoutInflater)

    override fun FragmentNavigationBinding.addControls(savedInstanceState: Bundle?) {
    }

    override fun FragmentNavigationBinding.addEvents(savedInstanceState: Bundle?) {
    }

    override fun FragmentNavigationBinding.onLiveData(savedInstanceState: Bundle?) {
        navTab.tabSelectedListener = {
            lifecycleScope.launch {
                when (it) {
                    com.huyhieu.widget.commons.UTab.TAB_SCAN -> {
                        navigate(MyNavHost.Home, HomeDirections.toQR) {
                            setPopUpTo(R.id.homeFragment, false)
                        }
                    }
                    com.huyhieu.widget.commons.UTab.TAB_HOME -> {
                        navigate(MyNavHost.Home, HomeDirections.toHome) {
                            setPopUpTo(R.id.homeFragment, true)
                        }
                    }
                    com.huyhieu.widget.commons.UTab.TAB_PROFILE -> {
                        navigate(MyNavHost.Home, HomeDirections.toProfile) {
                            setPopUpTo(R.id.homeFragment, false)
                        }
                    }
                }
            }
        }
    }
}