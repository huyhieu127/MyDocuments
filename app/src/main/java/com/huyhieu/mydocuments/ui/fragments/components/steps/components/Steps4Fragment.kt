package com.huyhieu.mydocuments.ui.fragments.components.steps.components

import android.os.Bundle
import android.view.View
import com.huyhieu.library.extensions.handleBackPressedFragment
import com.huyhieu.mydocuments.R
import com.huyhieu.mydocuments.base.BaseFragment
import com.huyhieu.mydocuments.databinding.FragmentSteps4Binding
import com.huyhieu.mydocuments.navigation.MyNavHost
import com.huyhieu.mydocuments.navigation.popBackStackTo
import dagger.hilt.android.AndroidEntryPoint

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

@AndroidEntryPoint
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

    override fun onMyViewCreated(savedInstanceState: Bundle?) = with(vb) {
        setClickViews(btnFinish)
        handleBackPressedFragment {
            popBackStackTo(MyNavHost.Home, R.id.navigationFragment)
        }
    }

    override fun FragmentSteps4Binding.onClickViewBinding(v: View, id: Int) {
        when (id) {
            btnFinish.id -> mActivity.finish()
        }
    }
}