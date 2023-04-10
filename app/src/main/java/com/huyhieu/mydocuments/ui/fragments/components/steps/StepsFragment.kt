package com.huyhieu.mydocuments.ui.fragments.components.steps

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.huyhieu.library.extensions.handleBackPressedFragment
import com.huyhieu.library.extensions.setDarkColorStatusBar
import com.huyhieu.library.extensions.setTransitionTo
import com.huyhieu.mydocuments.R
import com.huyhieu.mydocuments.base.BaseFragment
import com.huyhieu.mydocuments.databinding.FragmentStepsBinding
import com.huyhieu.mydocuments.navigation.MyNavHost
import com.huyhieu.mydocuments.navigation.directions.StepDirections
import com.huyhieu.mydocuments.navigation.navigate
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class StepsFragment : BaseFragment<FragmentStepsBinding>() {

    @Inject
    lateinit var stepsVM: StepsVM

    override fun FragmentStepsBinding.onMyViewCreated(view: View, savedInstanceState: Bundle?) {
        mActivity.setDarkColorStatusBar(false)
        handleBackPressedFragment {
            mActivity.finish()
        }
    }

    override fun FragmentStepsBinding.onMyLiveData(savedInstanceState: Bundle?) {
        lifecycleScope.launch {
            stepsVM.stepsLiveData.observe(this@StepsFragment) {
                when (it) {
                    1 -> {
                        motionSteps.setTransitionTo(R.id.s1, R.id.s1)
                        navigate(MyNavHost.Steps, StepDirections.toStep1)
                    }
                    2 -> {
                        motionSteps.setTransitionTo(R.id.s1, R.id.s2)
                        navigate(MyNavHost.Steps, StepDirections.toStep2) {
                            setLaunchSingleTop(true)
                            setPopUpTo(StepDirections.toStep1.actionId, false)
                            setEnterAnim(R.anim.anim_enter_hrz)
                            setExitAnim(R.anim.anim_exit_hrz)
                        }
                    }
                    3 -> {
                        motionSteps.setTransitionTo(R.id.s2, R.id.s3)
                        navigate(MyNavHost.Steps, StepDirections.toStep3) {
                            setLaunchSingleTop(true)
                            setPopUpTo(StepDirections.toStep1.actionId, false)
                            setEnterAnim(R.anim.anim_enter_hrz)
                            setExitAnim(R.anim.anim_exit_hrz)
                        }
                    }
                    4 -> {
                        motionSteps.setTransitionTo(R.id.s3, R.id.s4)
                        navigate(MyNavHost.Steps, StepDirections.toStep4) {
                            setLaunchSingleTop(true)
                            setPopUpTo(StepDirections.toStep1.actionId, false)
                            setEnterAnim(R.anim.anim_enter_hrz)
                            setExitAnim(R.anim.anim_exit_hrz)
                        }
                    }
                }
            }
            stepsVM.stepsLiveData.postValue(0)
        }
    }
}