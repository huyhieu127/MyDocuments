package com.huyhieu.mydocuments.ui.fragments.chart

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.huyhieu.library.extensions.setDarkColorStatusBar
import com.huyhieu.library.extensions.setNavigationBarColor
import com.huyhieu.mydocuments.R
import com.huyhieu.mydocuments.base.BaseFragment
import com.huyhieu.mydocuments.databinding.FragmentRadarChartBinding

class RadarChartFragment : BaseFragment<FragmentRadarChartBinding>() {
    override fun FragmentRadarChartBinding.onMyViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        mActivity.setDarkColorStatusBar(false)
        mActivity.setNavigationBarColor(R.color.black)
    }

}