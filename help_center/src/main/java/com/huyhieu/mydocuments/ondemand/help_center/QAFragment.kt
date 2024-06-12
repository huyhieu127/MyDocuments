package com.huyhieu.mydocuments.ondemand.help_center

import android.os.Bundle
import com.huyhieu.mydocuments.base.BaseFragment
import com.huyhieu.mydocuments.ondemand.help_center.databinding.FragmentQABinding
import com.huyhieu.mydocuments.utils.Constants
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class QAFragment : BaseFragment<FragmentQABinding>() {

    override fun onMyViewCreated(savedInstanceState: Bundle?) {
        arguments?.getString(Constants.DYNAMIC_FEATURES)?.also {
            vb.tvResult.text = "QA: ".plus(it)
        }
        homeVM.title.value = "AAAA"
    }
}