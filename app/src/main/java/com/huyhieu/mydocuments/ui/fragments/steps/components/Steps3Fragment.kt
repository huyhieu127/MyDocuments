package com.huyhieu.mydocuments.ui.fragments.steps.components

import android.os.Bundle
import android.view.View
import com.huyhieu.mydocuments.base.BaseFragment
import com.huyhieu.mydocuments.databinding.FragmentSteps3Binding
import com.huyhieu.mydocuments.ui.fragments.steps.StepsVM
import com.huyhieu.mydocuments.utils.directions.StepDirections
import com.huyhieu.mydocuments.utils.extensions.navigate
import com.huyhieu.mydocuments.utils.extensions.popBackStack
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

@AndroidEntryPoint
class Steps3Fragment : BaseFragment<FragmentSteps3Binding>() {

    @Inject
    lateinit var stepsVM: StepsVM

    private var param1: String? = null
    private var param2: String? = null

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Steps3Fragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun initializeBinding() = FragmentSteps3Binding.inflate(layoutInflater)

    override fun addControls(savedInstanceState: Bundle?) {
    }

    override fun addEvents(savedInstanceState: Bundle?) {
        mBinding.btnNext.setOnClickListener {
            stepsVM.setStep(4)
        }
    }

    override fun onClick(v: View?) {
    }

    override fun onBackPressedFragment() {
        mActivity?.popBackStack()
    }
}