package com.huyhieu.mydocuments.ui.activities.steps

import android.os.Bundle
import android.view.View
import android.view.animation.AnticipateOvershootInterpolator
import androidx.constraintlayout.widget.ConstraintSet
import androidx.lifecycle.ViewModelProvider
import androidx.transition.ChangeBounds
import androidx.transition.Transition
import androidx.transition.TransitionManager
import com.huyhieu.mydocuments.R
import com.huyhieu.mydocuments.base.BaseActivity
import com.huyhieu.mydocuments.databinding.ActivityStepsBinding
import com.huyhieu.mydocuments.utils.logDebug
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class StepsActivity : BaseActivity<ActivityStepsBinding>() {

    //@Inject
    lateinit var viewModel: StepsVM

    override fun initializeBinding() = ActivityStepsBinding.inflate(layoutInflater)

    override fun addControls(savedInstanceState1: Bundle?) {
        viewModel = ViewModelProvider(this)[StepsVM::class.java]
    }

    override fun addEvents(savedInstanceState1: Bundle?) {
        viewModel.ldStep.observe(this) {
            logDebug("Step: $it ${viewModel.hashCode()}")
            updateViewStep(it)
        }
    }

    override fun onClick(v: View?) {
    }

    private fun updateViewStep(step: Int = 1) {
        val value = (1 / 4F) * step
        val set = ConstraintSet()
        set.clone(mBinding.lnStep)
        val transition: Transition = ChangeBounds()
        transition.interpolator = AnticipateOvershootInterpolator(1.0f)
        //transition.interpolator = BounceInterpolator()
        //transition.interpolator = CycleInterpolator(2.0F)
        //transition.interpolator = DecelerateInterpolator(100.0F)
        transition.duration = 1000
        TransitionManager.beginDelayedTransition(mBinding.lnStep, transition)
        set.constrainPercentWidth(R.id.cvStep, value)
        set.applyTo(mBinding.lnStep)
    }
}