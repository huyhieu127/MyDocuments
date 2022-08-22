package com.huyhieu.mydocuments.ui.fragments

import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.huyhieu.mydocuments.base.BaseFragment
import com.huyhieu.mydocuments.databinding.FragmentSplashBinding
import com.huyhieu.mydocuments.utils.directions.MainDirections
import com.huyhieu.mydocuments.utils.extensions.navigate
import com.huyhieu.mydocuments.utils.extensions.setDarkColorStatusBar
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashFragment : BaseFragment<FragmentSplashBinding>() {
    override fun initializeBinding() = FragmentSplashBinding.inflate(layoutInflater)

    override fun FragmentSplashBinding.addControls(savedInstanceState: Bundle?) {
        mActivity?.setDarkColorStatusBar(true)
    }

    override fun FragmentSplashBinding.addEvents(savedInstanceState: Bundle?) {
        lifecycleScope.launch {
            delay(2000)
            mActivity?.navigate(MainDirections.toIntroduce)
        }
    }

}