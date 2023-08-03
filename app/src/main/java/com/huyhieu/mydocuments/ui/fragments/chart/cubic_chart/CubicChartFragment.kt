package com.huyhieu.mydocuments.ui.fragments.chart.cubic_chart

import android.graphics.PointF
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.huyhieu.library.extensions.showToastShort
import com.huyhieu.mydocuments.R
import com.huyhieu.mydocuments.base.BaseFragment
import com.huyhieu.mydocuments.databinding.FragmentCubicChartBinding

class CubicChartFragment : BaseFragment<FragmentCubicChartBinding>() {
    override fun onMyViewCreated(savedInstanceState: Bundle?) {
        vb.canvasView.setOnChartClickListener(object :CubicChartView.OnChartClickListener{
            override fun onDotClicked(pointF: PointF) {
                showToastShort("Axis(${pointF.x}:${pointF.y})")
            }
        })
    }
}