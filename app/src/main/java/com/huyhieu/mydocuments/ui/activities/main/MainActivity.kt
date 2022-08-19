package com.huyhieu.mydocuments.ui.activities.main

import android.os.Bundle
import android.view.View
import com.huyhieu.mydocuments.base.BaseActivity
import com.huyhieu.mydocuments.databinding.ActivityMainBinding
import com.huyhieu.mydocuments.ui.fragments.steps.StepsVM
import com.huyhieu.mydocuments.utils.extensions.setDarkColorStatusBar
import com.huyhieu.mydocuments.utils.extensions.setTransparentStatusBar
import com.huyhieu.mydocuments.utils.extensions.showToastShort
import com.huyhieu.mydocuments.utils.logDebug
import dagger.hilt.android.AndroidEntryPoint
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
        logDebug("Test push Event Github action!")
    }

    override fun onLiveData() {
        mainVM.pushNotify.observe(this) {
        }
    }

    override fun onClick(p0: View?) {
    }
}