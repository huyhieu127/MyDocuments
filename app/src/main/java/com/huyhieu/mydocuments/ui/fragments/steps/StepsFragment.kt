package com.huyhieu.mydocuments.ui.fragments.steps

import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import com.huyhieu.mydocuments.R
import com.huyhieu.mydocuments.base.BaseFragment
import com.huyhieu.mydocuments.databinding.FragmentStepsBinding
import com.huyhieu.mydocuments.utils.directions.StepDirections
import com.huyhieu.mydocuments.utils.extensions.childNavigate
import com.huyhieu.mydocuments.utils.extensions.navigate
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
                        mActivity?.navigate(StepDirections.toStep1)
                    }
                    2 -> {
                        motionSteps.setTransitionTo(R.id.s1, R.id.s2)
                        navHostSteps.childNavigate(
                            StepDirections.toStep2,
                            NavOptions.Builder().apply {
                                setLaunchSingleTop(true)
                                setPopUpTo(StepDirections.toStep1.actionId, false)
                                setEnterAnim(R.anim.anim_enter_hrz)
                                setExitAnim(R.anim.anim_exit_hrz)
                            }.build()
                        )
                    }
                    3 -> {
                        motionSteps.setTransitionTo(R.id.s2, R.id.s3)
                        navHostSteps.childNavigate(
                            StepDirections.toStep3,
                            NavOptions.Builder().apply {
                                setLaunchSingleTop(true)
                                setPopUpTo(StepDirections.toStep1.actionId, false)
                                setEnterAnim(R.anim.anim_enter_hrz)
                                setExitAnim(R.anim.anim_exit_hrz)
                            }.build()
                        )
                    }
                    4 -> {
                        motionSteps.setTransitionTo(R.id.s3, R.id.s4)
                        navHostSteps.childNavigate(
                            StepDirections.toStep4,
                            NavOptions.Builder().apply {
                                setLaunchSingleTop(true)
                                setPopUpTo(StepDirections.toStep1.actionId, false)
                                setEnterAnim(R.anim.anim_enter_hrz)
                                setExitAnim(R.anim.anim_exit_hrz)
                            }.build()
                        )
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