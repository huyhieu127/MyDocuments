package com.huyhieu.mydocuments.ui.fragments.motion

import android.os.Bundle
import com.huyhieu.mydocuments.libraries.extensions.setTransitionTo
import com.huyhieu.mydocuments.R
import com.huyhieu.mydocuments.base.BaseFragment
import com.huyhieu.mydocuments.databinding.FragmentMotionCardBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MotionCardFragment : BaseFragment<FragmentMotionCardBinding>() {

    override fun onMyViewCreated(savedInstanceState: Bundle?) = with(vb) {
        //hideNavigationBottom()
        motionCard.setTransitionTo(R.id.startCard, R.id.endCard)
    }
}