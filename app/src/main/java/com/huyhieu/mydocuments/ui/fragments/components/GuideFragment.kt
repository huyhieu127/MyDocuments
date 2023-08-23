package com.huyhieu.mydocuments.ui.fragments.components

import android.os.Bundle
import com.huyhieu.mydocuments.libraries.commons.HoleCircle
import com.huyhieu.mydocuments.base.BaseFragment
import com.huyhieu.mydocuments.databinding.FragmentGuideBinding

class GuideFragment : BaseFragment<FragmentGuideBinding>() {

    override fun onMyViewCreated(savedInstanceState: Bundle?) = with(vb){
        hvFrame.holeCircle = HoleCircle(button)
    }
}