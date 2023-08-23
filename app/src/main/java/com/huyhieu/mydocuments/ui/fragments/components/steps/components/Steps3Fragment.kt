package com.huyhieu.mydocuments.ui.fragments.components.steps.components

import android.os.Bundle
import android.view.View
import com.huyhieu.mydocuments.libraries.extensions.handleBackPressedFragment
import com.huyhieu.mydocuments.R
import com.huyhieu.mydocuments.base.BaseFragment
import com.huyhieu.mydocuments.databinding.FragmentSteps3Binding
import com.huyhieu.mydocuments.navigation.MyNavHost
import com.huyhieu.mydocuments.navigation.popBackStackTo
import com.huyhieu.mydocuments.ui.fragments.components.steps.StepsVM
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

    override fun onMyViewCreated(savedInstanceState: Bundle?) = with(vb) {
        setClickViews(btnNext)
        handleBackPressedFragment {
            popBackStackTo(MyNavHost.Home, R.id.navigationFragment)
        }
    }

    override fun FragmentSteps3Binding.onClickViewBinding(v: View, id: Int) {
        when (id) {
            btnNext.id -> stepsVM.setStep(4)
        }
    }
}