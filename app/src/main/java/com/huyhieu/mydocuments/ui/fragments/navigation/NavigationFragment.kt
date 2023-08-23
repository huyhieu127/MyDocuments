package com.huyhieu.mydocuments.ui.fragments.navigation

import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.huyhieu.mydocuments.libraries.custom_views.UTab
import com.huyhieu.mydocuments.R
import com.huyhieu.mydocuments.base.BaseFragment
import com.huyhieu.mydocuments.databinding.FragmentNavigationBinding
import com.huyhieu.mydocuments.navigation.MyNavHost
import com.huyhieu.mydocuments.navigation.directions.HomeDirections
import com.huyhieu.mydocuments.navigation.navigate
import kotlinx.coroutines.launch

class NavigationFragment : BaseFragment<FragmentNavigationBinding>() {

    override fun onMyViewCreated(savedInstanceState: Bundle?) = with(vb) {
        navTab.setBackgroundBlur(root)
    }

    override fun onMyLiveData(savedInstanceState: Bundle?) = with(vb) {
        navTab.tabSelectedListener = {
            lifecycleScope.launch {
                when (it) {
                    UTab.TAB_SCAN -> {
                        navigate(MyNavHost.Home, HomeDirections.toQR) {
                            setPopUpTo(R.id.homeFragment, false)
                        }
                    }
                    UTab.TAB_HOME -> {
                        navigate(MyNavHost.Home, HomeDirections.toHome) {
                            setPopUpTo(R.id.homeFragment, true)
                        }
                    }
                    UTab.TAB_PROFILE -> {
                        navigate(MyNavHost.Home, HomeDirections.toProfile) {
                            setPopUpTo(R.id.homeFragment, false)
                        }
                    }
                }
            }
        }
    }
}