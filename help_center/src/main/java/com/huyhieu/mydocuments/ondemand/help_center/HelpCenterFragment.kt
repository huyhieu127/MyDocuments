package com.huyhieu.mydocuments.ondemand.help_center

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.huyhieu.mydocuments.base.BaseFragment
import com.huyhieu.mydocuments.ondemand.help_center.databinding.FragmentHelpCenterBinding
import com.huyhieu.mydocuments.utils.Constants

class HelpCenterFragment : BaseFragment<FragmentHelpCenterBinding>() {

    override fun onMyViewCreated(savedInstanceState: Bundle?) {
        arguments?.getString(Constants.DYNAMIC_FEATURES)?.also {
            vb.tvResult.text = "Help center: ".plus(it)
        }
        arguments?.getBoolean("isFrg")?.also {
            vb.btnQA.isVisible = !it
        }
        setClickViews(vb.btnQA)
    }

    override fun FragmentHelpCenterBinding.onClickViewBinding(v: View, id: Int) {
        when (v) {
            btnQA -> {
                findNavController().navigate(
                    R.id.qaFragment, null, NavOptions.Builder().apply {
                        //clearBackStack(findNavController(), true)
                        setPopUpTo(
                            com.huyhieu.mydocuments.R.id.navigationFragment,
                            inclusive = false,
                            saveState = false
                        )
                    }.build()
                )
            }
        }
    }
}