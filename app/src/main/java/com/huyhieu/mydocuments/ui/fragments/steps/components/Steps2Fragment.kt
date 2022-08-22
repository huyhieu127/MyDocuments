package com.huyhieu.mydocuments.ui.fragments.steps.components

import android.os.Bundle
import com.huyhieu.mydocuments.base.BaseFragment
import com.huyhieu.mydocuments.databinding.FragmentSteps2Binding
import com.huyhieu.mydocuments.ui.fragments.steps.StepsVM
import com.huyhieu.mydocuments.utils.extensions.popBackStack
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

@AndroidEntryPoint
class Steps2Fragment : BaseFragment<FragmentSteps2Binding>() {

    @Inject
    lateinit var stepsVM: StepsVM

    private var param1: String? = null
    private var param2: String? = null

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Steps2Fragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun initializeBinding() = FragmentSteps2Binding.inflate(layoutInflater)

    override fun FragmentSteps2Binding.addControls(savedInstanceState: Bundle?) {
    }

    override fun FragmentSteps2Binding.addEvents(savedInstanceState: Bundle?) {
        btnNext.setOnClickListener {
            stepsVM.setStep(3)
        }
    }

    override fun onBackPressedFragment() {
        mActivity?.popBackStack()
    }
}