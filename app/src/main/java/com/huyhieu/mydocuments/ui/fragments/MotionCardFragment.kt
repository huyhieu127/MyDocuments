package com.huyhieu.mydocuments.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.constraintlayout.motion.widget.MotionLayout
import com.huyhieu.mydocuments.R
import com.huyhieu.mydocuments.base.BaseFragment
import com.huyhieu.mydocuments.databinding.FragmentMotionCardBinding
import com.huyhieu.mydocuments.utils.extensions.setTransitionTo
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MotionCardFragment : BaseFragment<FragmentMotionCardBinding>() {
    override fun initializeBinding() = FragmentMotionCardBinding.inflate(layoutInflater)

    override fun addControls(savedInstanceState: Bundle?) {
    }

    override fun addEvents(savedInstanceState: Bundle?) {
        mBinding.motionCard.setTransitionTo(R.id.startCard, R.id.endCard)
    }

    override fun onClick(v: View?) {
    }

}