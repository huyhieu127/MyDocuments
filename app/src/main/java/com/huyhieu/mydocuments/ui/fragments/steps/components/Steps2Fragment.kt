package com.huyhieu.mydocuments.ui.fragments.steps.components

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.huyhieu.mydocuments.base.BaseFragment
import com.huyhieu.mydocuments.databinding.FragmentSteps2Binding
import com.huyhieu.mydocuments.ui.activities.steps.StepsViewModel
import com.huyhieu.mydocuments.utils.logDebug

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class Steps2Fragment : BaseFragment<FragmentSteps2Binding>() {

    lateinit var viewModel: StepsViewModel

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

    override fun initializeBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentSteps2Binding.inflate(layoutInflater)

    override fun addControls(savedInstanceState: Bundle?) {
        viewModel = ViewModelProvider(requireActivity())[StepsViewModel::class.java]
    }

    override fun addEvents(savedInstanceState: Bundle?) {
        viewModel.ldStep.observe(viewLifecycleOwner){
            logDebug("S2: $it")
        }
        mBinding.btnNext.setOnClickListener {
            viewModel.ldStep.value = 3
            val action = Steps2FragmentDirections.actionSteps2FragmentToSteps3Fragment()
            view?.findNavController()?.navigate(action)
        }
    }

    override fun onClick(v: View?) {
    }
}