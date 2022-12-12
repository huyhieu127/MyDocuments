package com.huyhieu.mydocuments.ui.fragments.components

import android.os.Bundle
import com.huyhieu.library.extensions.setTransitionTo
import com.huyhieu.mydocuments.R
import com.huyhieu.mydocuments.base.BaseFragmentOld
import com.huyhieu.mydocuments.databinding.FragmentMotionCardBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MotionCardFragment : BaseFragmentOld<FragmentMotionCardBinding>() {
    override fun initializeBinding() = FragmentMotionCardBinding.inflate(layoutInflater)

    override fun FragmentMotionCardBinding.addControls(savedInstanceState: Bundle?) {
        hideNavigationBottom()
    }

    override fun FragmentMotionCardBinding.addEvents(savedInstanceState: Bundle?) {
        mBinding.motionCard.setTransitionTo(R.id.startCard, R.id.endCard)
    }
}