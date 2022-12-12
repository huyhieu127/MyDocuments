package com.huyhieu.mydocuments.ui.fragments.components

import android.os.Bundle
import com.huyhieu.library.commons.HoleCircle
import com.huyhieu.mydocuments.base.BaseFragmentOld
import com.huyhieu.mydocuments.databinding.FragmentGuideBinding

class GuideFragment : BaseFragmentOld<FragmentGuideBinding>() {
    override fun initializeBinding() = FragmentGuideBinding.inflate(layoutInflater)

    override fun FragmentGuideBinding.addControls(savedInstanceState: Bundle?) {
        hvFrame.holeCircle = HoleCircle(mBinding.button)
        //mBinding.hvFrame.holeRectangle = HoleRectangle(mBinding.button)
    }

    override fun FragmentGuideBinding.addEvents(savedInstanceState: Bundle?) {
    }
}