package com.huyhieu.mydocuments.ui.fragments.calendar

import android.os.Bundle
import androidx.recyclerview.widget.PagerSnapHelper
import com.huyhieu.mydocuments.base.BaseFragment
import com.huyhieu.mydocuments.databinding.FragmentCalendarBinding
import com.huyhieu.mydocuments.ui.fragments.calendar.adapters.MonthOfCalendarAdapter
import com.huyhieu.mydocuments.utils.extensions.getMonth
import com.huyhieu.mydocuments.utils.extensions.getYear
import com.huyhieu.mydocuments.utils.extensions.setDarkColorStatusBar
import com.huyhieu.mydocuments.utils.logDebug

class CalendarFragment : BaseFragment<FragmentCalendarBinding>() {
    private val monthAdapter = MonthOfCalendarAdapter()

    override fun initializeBinding() = FragmentCalendarBinding.inflate(layoutInflater)

    override fun FragmentCalendarBinding.addControls(savedInstanceState: Bundle?) {
        mActivity?.setDarkColorStatusBar()

        val pagerSnapHelper = PagerSnapHelper()
        pagerSnapHelper.attachToRecyclerView(rcvMonth)

        val calendar = monthAdapter.cCurrent
        tvMonth.text = String.format("Tháng ${calendar.getMonth() + 1}/${calendar.getYear()}")

        rcvMonth.adapter = monthAdapter
        val start = System.currentTimeMillis()
        monthAdapter.setupMonths("12/12/2021", "12/10/2023")
        logDebug((System.currentTimeMillis() - start).toString())
        monthAdapter.attachToRecyclerView(rcvMonth) {
            tvMonth.text = String.format("Tháng ${it.month}")
        }
        monthAdapter.dayClick = {
            tvDateSelected.text = String.format("Ngày ${it.date}")
        }
    }

    override fun FragmentCalendarBinding.addEvents(savedInstanceState: Bundle?) {
    }
}

