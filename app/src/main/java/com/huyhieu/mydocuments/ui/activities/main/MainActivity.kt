package com.huyhieu.mydocuments.ui.activities.main

import android.os.Bundle
import android.view.View
import androidx.annotation.ColorRes
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.huyhieu.mydocuments.R
import com.huyhieu.mydocuments.base.BaseActivity
import com.huyhieu.mydocuments.databinding.ActivityMainBinding
import com.huyhieu.mydocuments.utils.commons.UTab
import com.huyhieu.mydocuments.utils.directions.MainDirections
import com.huyhieu.mydocuments.utils.extensions.navigate
import com.huyhieu.mydocuments.utils.extensions.setDarkColorStatusBar
import com.huyhieu.mydocuments.utils.extensions.setNavigationBarColor
import com.huyhieu.mydocuments.utils.extensions.setTransparentStatusBar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>() {

    @Inject
    lateinit var mainVM: MainVM

    override fun initializeBinding() = ActivityMainBinding.inflate(layoutInflater)

    override fun addControls(savedInstanceState1: Bundle?) {
        //setFullScreen()
        setDarkColorStatusBar(false)
        setTransparentStatusBar(true)
    }

    override fun addEvents(savedInstanceState1: Bundle?) {
        mBinding.navTab.tabSelectedListener = {
            lifecycleScope.launch {
                when (it) {
                    UTab.TAB_SCAN -> {
                        this@MainActivity.navigate(MainDirections.toScan)
                    }
                    UTab.TAB_HOME -> {
                        this@MainActivity.navigate(MainDirections.toProfile)
                    }
                    UTab.TAB_PROFILE -> {
                        this@MainActivity.navigate(MainDirections.toSteps)
                    }
                }
            }
        }
    }

    override fun onLiveData() {
        mainVM.pushNotify.observe(this) {
        }
    }

    override fun onClick(p0: View?) {
    }

    override fun setTabNavigationBottom(tab: UTab) {
        mBinding.navTab.setTabSelected(tab)
    }

    override fun showNavigationBottom() {
        mBinding.navTab.isVisible = true
        setNavigationBarColor(R.color.colorAccent_95)
    }

    override fun hideNavigationBottom(@ColorRes idColor: Int) {
        mBinding.navTab.isVisible = false
        setNavigationBarColor(idColor)
    }
}