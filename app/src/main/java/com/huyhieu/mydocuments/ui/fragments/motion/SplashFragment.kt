package com.huyhieu.mydocuments.ui.fragments.motion

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.huyhieu.mydocuments.libraries.extensions.setDarkColorStatusBar
import com.huyhieu.mydocuments.base.BaseFragment
import com.huyhieu.mydocuments.databinding.FragmentSplashBinding
import com.huyhieu.mydocuments.navigation.directions.MainDirections
import com.huyhieu.mydocuments.navigation.navigate
import com.huyhieu.mydocuments.shared.appShared
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashFragment : BaseFragment<FragmentSplashBinding>() {

    override fun onMyViewCreated(savedInstanceState: Bundle?) = with(vb) {
        mActivity.setDarkColorStatusBar(true)
        if (appShared.isLoadedIntroduce) {
            navigate(MainDirections.toNavigation)
        } else {
            setClickViews(btnNext)
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