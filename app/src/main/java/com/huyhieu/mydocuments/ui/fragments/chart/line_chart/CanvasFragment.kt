package com.huyhieu.mydocuments.ui.fragments.chart.line_chart

import android.os.Bundle
import android.view.View
import com.huyhieu.library.extensions.setDarkColorStatusBar
import com.huyhieu.library.extensions.setNavigationBarColor
import com.huyhieu.mydocuments.R
import com.huyhieu.mydocuments.base.BaseFragment
import com.huyhieu.mydocuments.databinding.FragmentCanvasBinding

class CanvasFragment : BaseFragment<FragmentCanvasBinding>() {
    override fun FragmentCanvasBinding.onMyViewCreated(savedInstanceState: Bundle?) {
        mActivity.setDarkColorStatusBar(false)
        mActivity.setNavigationBarColor(R.color.black)
    }
}