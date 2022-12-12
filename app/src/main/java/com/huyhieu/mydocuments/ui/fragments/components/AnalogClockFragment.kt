package com.huyhieu.mydocuments.ui.fragments.components

import android.os.Bundle
import com.huyhieu.library.extensions.setDarkColorStatusBar
import com.huyhieu.mydocuments.base.BaseFragmentOld
import com.huyhieu.mydocuments.databinding.FragmentAnalogClockBinding

class AnalogClockFragment : BaseFragmentOld<FragmentAnalogClockBinding>() {

    override fun initializeBinding() = FragmentAnalogClockBinding.inflate(layoutInflater)

    override fun FragmentAnalogClockBinding.addControls(savedInstanceState: Bundle?) {
        mActivity.setDarkColorStatusBar()
    }

    override fun FragmentAnalogClockBinding.addEvents(savedInstanceState: Bundle?) {
    }

}