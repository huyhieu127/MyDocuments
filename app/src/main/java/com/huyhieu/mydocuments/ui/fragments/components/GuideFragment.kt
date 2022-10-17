package com.huyhieu.mydocuments.ui.fragments.components

import android.os.Bundle
import com.huyhieu.mydocuments.base.BaseFragment
import com.huyhieu.mydocuments.databinding.FragmentGuideBinding
import com.huyhieu.mydocuments.utils.commons.HoleCircle

class GuideFragment : BaseFragment<FragmentGuideBinding>() {
    override fun initializeBinding() = FragmentGuideBinding.inflate(layoutInflater)

    override fun FragmentGuideBinding.addControls(savedInstanceState: Bundle?) {
        hvFrame.holeCircle = HoleCircle(mBinding.button)
        //mBinding.hvFrame.holeRectangle = HoleRectangle(mBinding.button)
    }

    override fun FragmentGuideBinding.addEvents(savedInstanceState: Bundle?) {
    }
}