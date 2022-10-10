package com.huyhieu.mydocuments.ui.fragments.steps

import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.huyhieu.mydocuments.R
import com.huyhieu.mydocuments.base.BaseFragment
import com.huyhieu.mydocuments.databinding.FragmentStepsBinding
import com.huyhieu.mydocuments.navigation.directions.StepDirections
import com.huyhieu.mydocuments.navigation.navigate
import com.huyhieu.mydocuments.utils.extensions.setDarkColorStatusBar
import com.huyhieu.mydocuments.utils.extensions.setTransitionTo
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class StepsFragment : BaseFragment<FragmentStepsBinding>() {

    @Inject
    lateinit var stepsVM: StepsVM

    override fun initializeBinding() = FragmentStepsBinding.inflate(layoutInflater)

    override fun FragmentStepsBinding.addControls(savedInstanceState: Bundle?) {
        mActivity?.setDarkColorStatusBar(false)
    }

    override fun FragmentStepsBinding.addEvents(savedInstanceState: Bundle?) {
    }

    override fun FragmentStepsBinding.onLiveData(savedInstanceState: Bundle?) {
        lifecycleScope.launch {
            stepsVM.stepsLiveData.observe(this@StepsFragment) {
                when (it) {
                    1 -> {
                        motionSteps.setTransitionTo(R.id.s1, R.id.s1)
                        navigate(StepDirections.toStep1)
                    }
                    2 -> {
                        motionSteps.setTransitionTo(R.id.s1, R.id.s2)
                        navigate(StepDirections.toStep2) {
                            setLaunchSingleTop(true)
                            setPopUpTo(StepDirections.toStep1.actionId, false)
                            setEnterAnim(R.anim.anim_enter_hrz)
                            setExitAnim(R.anim.anim_exit_hrz)
                        }
                    }
                    3 -> {
                        motionSteps.setTransitionTo(R.id.s2, R.id.s3)
                        navigate(StepDirections.toStep3) {
                            setLaunchSingleTop(true)
                            setPopUpTo(StepDirections.toStep1.actionId, false)
                            setEnterAnim(R.anim.anim_enter_hrz)
                            setExitAnim(R.anim.anim_exit_hrz)
                        }
                    }
                    4 -> {
                        motionSteps.setTransitionTo(R.id.s3, R.id.s4)
                        navigate(StepDirections.toStep4) {
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

    override fun onBackPressedFragment() {
        mActivity?.finish()
    }
}