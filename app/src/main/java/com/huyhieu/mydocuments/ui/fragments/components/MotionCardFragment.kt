package com.huyhieu.mydocuments.ui.fragments.components

import android.os.Bundle
import android.view.View
import com.huyhieu.library.extensions.setTransitionTo
import com.huyhieu.mydocuments.R
import com.huyhieu.mydocuments.base.BaseFragment
import com.huyhieu.mydocuments.databinding.FragmentMotionCardBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MotionCardFragment : BaseFragment<FragmentMotionCardBinding>() {

    override fun FragmentMotionCardBinding.onMyViewCreated(view: View, savedInstanceState: Bundle?) {
        //hideNavigationBottom()
        motionCard.setTransitionTo(R.id.startCard, R.id.endCard)
    }
}