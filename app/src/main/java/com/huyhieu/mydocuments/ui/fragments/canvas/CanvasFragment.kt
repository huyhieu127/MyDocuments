package com.huyhieu.mydocuments.ui.fragments.canvas

import android.os.Bundle
import com.huyhieu.library.extensions.setDarkColorStatusBar
import com.huyhieu.library.extensions.setNavigationBarColor
import com.huyhieu.mydocuments.R
import com.huyhieu.mydocuments.base.BaseFragment
import com.huyhieu.mydocuments.databinding.FragmentCanvasBinding

class CanvasFragment : BaseFragment<FragmentCanvasBinding>() {
    override fun initializeBinding() = FragmentCanvasBinding.inflate(layoutInflater)

    override fun FragmentCanvasBinding.addControls(savedInstanceState: Bundle?) {
        mActivity.setDarkColorStatusBar(false)
        mActivity.setNavigationBarColor(R.color.black)
    }

    override fun FragmentCanvasBinding.addEvents(savedInstanceState: Bundle?) {
    }

}