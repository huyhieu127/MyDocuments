package com.huyhieu.mydocuments.ui.fragments.components

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.huyhieu.library.extensions.setDarkColorStatusBar
import com.huyhieu.mydocuments.base.BaseFragmentOld
import com.huyhieu.mydocuments.databinding.FragmentSplashBinding
import com.huyhieu.mydocuments.navigation.directions.MainDirections
import com.huyhieu.mydocuments.navigation.navigate
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashFragment : BaseFragmentOld<FragmentSplashBinding>() {
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
                    navigate(MainDirections.toIntroduce)
                }
            }
        }
    }
}