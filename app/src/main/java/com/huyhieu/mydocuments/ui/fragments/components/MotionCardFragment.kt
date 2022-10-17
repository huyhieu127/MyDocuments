package com.huyhieu.mydocuments.ui.fragments.components

import android.os.Bundle
import com.huyhieu.mydocuments.R
import com.huyhieu.mydocuments.base.BaseFragment
import com.huyhieu.mydocuments.databinding.FragmentMotionCardBinding
import com.huyhieu.mydocuments.utils.extensions.setTransitionTo
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MotionCardFragment : BaseFragment<FragmentMotionCardBinding>() {
    override fun initializeBinding() = FragmentMotionCardBinding.inflate(layoutInflater)

    override fun FragmentMotionCardBinding.addControls(savedInstanceState: Bundle?) {
        hideNavigationBottom()
    }

    override fun FragmentMotionCardBinding.addEvents(savedInstanceState: Bundle?) {
        mBinding.motionCard.setTransitionTo(R.id.startCard, R.id.endCard)
    }
}