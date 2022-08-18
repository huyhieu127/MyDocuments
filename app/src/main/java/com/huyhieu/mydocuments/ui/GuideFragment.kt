package com.huyhieu.mydocuments.ui

import android.os.Bundle
import android.view.View
import com.huyhieu.mydocuments.base.BaseFragment
import com.huyhieu.mydocuments.databinding.FragmentGuideBinding
import com.huyhieu.mydocuments.utils.commons.HoleCircle
import com.huyhieu.mydocuments.utils.commons.HoleRectangle

class GuideFragment : BaseFragment<FragmentGuideBinding>() {
    override fun initializeBinding() = FragmentGuideBinding.inflate(layoutInflater)

    override fun addControls(savedInstanceState: Bundle?) {
        mBinding.hvFrame.holeCircle = HoleCircle(mBinding.button)
        //mBinding.hvFrame.holeRectangle = HoleRectangle(mBinding.button)
    }

    override fun addEvents(savedInstanceState: Bundle?) {
    }

    override fun onClick(v: View?) {
    }
}