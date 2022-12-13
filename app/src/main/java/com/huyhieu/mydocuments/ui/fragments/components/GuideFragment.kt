package com.huyhieu.mydocuments.ui.fragments.components

import android.os.Bundle
import android.view.View
import com.huyhieu.library.commons.HoleCircle
import com.huyhieu.mydocuments.base.BaseFragment
import com.huyhieu.mydocuments.databinding.FragmentGuideBinding

class GuideFragment : BaseFragment<FragmentGuideBinding>() {

    override fun FragmentGuideBinding.onMyViewCreated(view: View, savedInstanceState: Bundle?) {
        hvFrame.holeCircle = HoleCircle(button)
    }
}