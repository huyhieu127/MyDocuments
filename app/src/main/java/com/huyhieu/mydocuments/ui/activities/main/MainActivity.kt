package com.huyhieu.mydocuments.ui.activities.main

import android.os.Bundle
import android.view.View
import com.huyhieu.mydocuments.base.BaseActivity
import com.huyhieu.mydocuments.databinding.ActivityMainBinding
import com.huyhieu.mydocuments.utils.logDebug
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>() {

    override fun initializeBinding() = ActivityMainBinding.inflate(layoutInflater)

    override fun addControls(savedInstanceState1: Bundle?) {

    }

    override fun addEvents(savedInstanceState1: Bundle?) {
        logDebug("Test push Event Github action!")
    }

    override fun onClick(p0: View?) {
    }
}