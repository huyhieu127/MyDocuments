package com.huyhieu.mydocuments.ui.fragments

import android.os.Bundle
import android.view.View
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
        btnNext.setOnClickListener(this@SplashFragment)
    }

    override fun FragmentSplashBinding.onClickViewBinding(v: View) {
        when (v.id) {
            btnNext.id -> {
                lifecycleScope.launch {
                    delay(1000)
                    mActivity?.navigate(MainDirections.toIntroduce)
                }
            }
        }
    }
}