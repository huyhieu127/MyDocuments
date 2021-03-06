package com.huyhieu.mydocuments.ui.fragments.steps

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.huyhieu.mydocuments.base.BaseFragment
import com.huyhieu.mydocuments.databinding.FragmentSteps4Binding

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class Steps4Fragment : BaseFragment<FragmentSteps4Binding>() {
    private var param1: String? = null
    private var param2: String? = null

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Steps4Fragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun initializeBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentSteps4Binding.inflate(layoutInflater)

    override fun addControls(savedInstanceState: Bundle?) {
    }

    override fun addEvents(savedInstanceState: Bundle?) {
        mBinding.btnFinish.setOnClickListener {
            mActivity?.finish()
        }
    }

    override fun onClick(v: View?) {
    }
}