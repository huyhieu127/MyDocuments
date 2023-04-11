package com.huyhieu.mydocuments.ui.fragments.navigation

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.huyhieu.library.custom_views.UTab
import com.huyhieu.mydocuments.R
import com.huyhieu.mydocuments.base.BaseFragment
import com.huyhieu.mydocuments.databinding.FragmentNavigationBinding
import com.huyhieu.mydocuments.navigation.MyNavHost
import com.huyhieu.mydocuments.navigation.directions.HomeDirections
import com.huyhieu.mydocuments.navigation.navigate
import kotlinx.coroutines.launch

class NavigationFragment : BaseFragment<FragmentNavigationBinding>() {

    override fun FragmentNavigationBinding.onMyViewCreated(savedInstanceState: Bundle?) {
        navTab.setBackgroundBlur(root)
    }

    override fun FragmentNavigationBinding.onMyLiveData(savedInstanceState: Bundle?) {
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