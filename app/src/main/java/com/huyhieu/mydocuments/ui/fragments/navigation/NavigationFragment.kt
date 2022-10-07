package com.huyhieu.mydocuments.ui.fragments.navigation

import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.huyhieu.mydocuments.base.BaseFragment
import com.huyhieu.mydocuments.databinding.FragmentNavigationBinding
import com.huyhieu.mydocuments.utils.commons.UTab
import com.huyhieu.mydocuments.utils.extensions.showToastShort
import kotlinx.coroutines.launch

class NavigationFragment : BaseFragment<FragmentNavigationBinding>() {
    override fun initializeBinding() = FragmentNavigationBinding.inflate(layoutInflater)

    override fun FragmentNavigationBinding.addControls(savedInstanceState: Bundle?) {
    }

    override fun FragmentNavigationBinding.addEvents(savedInstanceState: Bundle?) {
    }

    override fun FragmentNavigationBinding.onLiveData(savedInstanceState: Bundle?) {
        mBinding.navTab.tabSelectedListener = {
            lifecycleScope.launch {
                when (it) {
                    UTab.TAB_SCAN -> {
                        //navigate(MainDirections.toScan)
                        context.showToastShort("Scan")
                    }
                    UTab.TAB_HOME -> {
                        //this@MainActivity.navigate(MainDirections.toProfile)
                        context.showToastShort("Home")
                    }
                    UTab.TAB_PROFILE -> {
                        //this@MainActivity.navigate(MainDirections.toSteps)
                        context.showToastShort("Profile")
                    }
                }
            }
        }
    }
}