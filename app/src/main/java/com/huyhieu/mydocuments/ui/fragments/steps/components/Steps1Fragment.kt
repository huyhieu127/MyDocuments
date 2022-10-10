package com.huyhieu.mydocuments.ui.fragments.steps.components

import android.os.Bundle
import com.huyhieu.mydocuments.base.BaseFragment
import com.huyhieu.mydocuments.databinding.FragmentSteps1Binding
import com.huyhieu.mydocuments.navigation.popBackStackTo
import com.huyhieu.mydocuments.ui.fragments.steps.StepsVM
import com.huyhieu.mydocuments.utils.logDebug
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

@AndroidEntryPoint
class Steps1Fragment : BaseFragment<FragmentSteps1Binding>() {

    @Inject
    lateinit var stepsVM: StepsVM

    private var param1: String? = null
    private var param2: String? = null

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Steps1Fragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun initializeBinding() = FragmentSteps1Binding.inflate(layoutInflater)

    override fun FragmentSteps1Binding.addControls(savedInstanceState: Bundle?) {
    }

    override fun FragmentSteps1Binding.addEvents(savedInstanceState: Bundle?) {
        btnNext.setOnClickListener {
            stepsVM.setStep(2)
        }
    }

    override fun onDestroyView() {
        logDebug("S1: onDestroyView")
        super.onDestroyView()
    }

    override fun onBackPressedFragment() {
        popBackStackTo()
    }
}