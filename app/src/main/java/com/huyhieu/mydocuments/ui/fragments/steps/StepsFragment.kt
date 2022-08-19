package com.huyhieu.mydocuments.ui.fragments.steps

import android.os.Bundle
import android.view.View
import androidx.navigation.NavOptions
import com.huyhieu.mydocuments.R
import com.huyhieu.mydocuments.base.BaseFragment
import com.huyhieu.mydocuments.databinding.FragmentStepsBinding
import com.huyhieu.mydocuments.utils.directions.StepDirections
import com.huyhieu.mydocuments.utils.extensions.childNavigate
import com.huyhieu.mydocuments.utils.extensions.navigate
import com.huyhieu.mydocuments.utils.extensions.setTransitionTo
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class StepsFragment : BaseFragment<FragmentStepsBinding>() {

    @Inject
    lateinit var stepsVM: StepsVM

    override fun initializeBinding() = FragmentStepsBinding.inflate(layoutInflater)

    override fun addControls(savedInstanceState: Bundle?) {
    }

    override fun addEvents(savedInstanceState: Bundle?) {
    }

    override fun onClick(v: View?) {
    }

    override fun onLiveData(savedInstanceState: Bundle?) {
        stepsVM.stepsLiveData.observe(this) {
            when (it) {
                1 -> {
                    mBinding.motionSteps.setTransitionTo(R.id.s1, R.id.s1)
                    mActivity?.navigate(StepDirections.toStep1)
                }
                2 -> {
                    mBinding.motionSteps.setTransitionTo(R.id.s1, R.id.s2)
                    mBinding.navHostSteps.childNavigate(
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
                    mBinding.motionSteps.setTransitionTo(R.id.s2, R.id.s3)
                    mBinding.navHostSteps.childNavigate(
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
                    mBinding.motionSteps.setTransitionTo(R.id.s3, R.id.s4)
                    mBinding.navHostSteps.childNavigate(
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
    }

    override fun onBackPressedFragment() {
        mActivity?.finish()
    }
}