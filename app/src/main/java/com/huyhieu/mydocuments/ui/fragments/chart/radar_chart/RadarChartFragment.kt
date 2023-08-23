package com.huyhieu.mydocuments.ui.fragments.chart.radar_chart

import android.os.Bundle
import com.huyhieu.mydocuments.libraries.extensions.setDarkColorStatusBar
import com.huyhieu.mydocuments.libraries.extensions.setNavigationBarColor
import com.huyhieu.mydocuments.R
import com.huyhieu.mydocuments.base.BaseFragment
import com.huyhieu.mydocuments.databinding.FragmentRadarChartBinding

class RadarChartFragment : BaseFragment<FragmentRadarChartBinding>() {
    override fun onMyViewCreated(savedInstanceState: Bundle?) {
        mActivity.setDarkColorStatusBar(false)
        mActivity.setNavigationBarColor(R.color.black)
    }

}