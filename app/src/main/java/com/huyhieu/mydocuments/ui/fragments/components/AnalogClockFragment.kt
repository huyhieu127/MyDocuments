package com.huyhieu.mydocuments.ui.fragments.components

import android.os.Bundle
import android.view.View
import com.huyhieu.library.extensions.setDarkColorStatusBar
import com.huyhieu.mydocuments.base.BaseFragment
import com.huyhieu.mydocuments.databinding.FragmentAnalogClockBinding

class AnalogClockFragment : BaseFragment<FragmentAnalogClockBinding>() {

    override fun FragmentAnalogClockBinding.onMyViewCreated(view: View, savedInstanceState: Bundle?) {
        mActivity.setDarkColorStatusBar()
    }

}