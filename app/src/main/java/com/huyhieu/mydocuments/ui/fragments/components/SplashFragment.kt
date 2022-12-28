package com.huyhieu.mydocuments.ui.fragments.components

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.huyhieu.library.extensions.setDarkColorStatusBar
import com.huyhieu.mydocuments.base.BaseFragment
import com.huyhieu.mydocuments.databinding.FragmentSplashBinding
import com.huyhieu.mydocuments.navigation.directions.MainDirections
import com.huyhieu.mydocuments.navigation.navigate
import com.huyhieu.mydocuments.shared.appShared
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashFragment : BaseFragment<FragmentSplashBinding>() {

    override fun FragmentSplashBinding.onMyViewCreated(view: View, savedInstanceState: Bundle?) {
        mActivity.setDarkColorStatusBar(true)
        if (appShared.isLoadedIntroduce) {
            navigate(MainDirections.toNavigation)
        } else {
            handleViewClick(btnNext)
        }
    }

    override fun FragmentSplashBinding.onClickViewBinding(v: View, id: Int) {
        when (id) {
            btnNext.id -> {
                if (!appShared.isLoadedIntroduce) {
                    lifecycleScope.launch {
                        delay(1000)
                        appShared.isLoadedIntroduce = true
                        navigate(MainDirections.toIntroduce)
                    }
                } else {
                    navigate(MainDirections.toNavigation)
                }
            }
        }
    }
}