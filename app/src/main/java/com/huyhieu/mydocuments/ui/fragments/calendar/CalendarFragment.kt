package com.huyhieu.mydocuments.ui.fragments.calendar

import android.os.Bundle
import androidx.recyclerview.widget.PagerSnapHelper
import com.huyhieu.library.constants.CalendarCst
import com.huyhieu.library.extensions.*
import com.huyhieu.mydocuments.R
import com.huyhieu.mydocuments.base.BaseFragment
import com.huyhieu.mydocuments.databinding.FragmentCalendarBinding
import com.huyhieu.widget.commons.calendar.adapters.MonthOfCalendarAdapter
import java.util.*

class CalendarFragment : BaseFragment<FragmentCalendarBinding>() {
    private val monthAdapter = MonthOfCalendarAdapter()

    override fun initializeBinding() = FragmentCalendarBinding.inflate(layoutInflater)

    override fun FragmentCalendarBinding.addControls(savedInstanceState: Bundle?) {
        mActivity?.setDarkColorStatusBar()
    }

    override fun FragmentCalendarBinding.addEvents(savedInstanceState: Bundle?) {
        val cCurrent = Calendar.getInstance()
        val dayOfWeek = cCurrent.getDisplayDayOfWeek()
        val date = cCurrent.formatToString(CalendarCst.FORMAT_DATE_DEFAULT)
        val today = String.format("Hôm nay,\n%s, %s", dayOfWeek, date)

        tvToday.setSpannable(
            content = today,
            subText = "Hôm nay,",
            colorId = R.color.blue
        )

        val pagerSnapHelper = PagerSnapHelper()
        pagerSnapHelper.attachToRecyclerView(rcvMonth)

        rcvMonth.adapter = monthAdapter
        monthAdapter.setupMonths(
            "19/11/2022",
            "01/01/${cCurrent.getYear() - 10}",
            "01/01/${cCurrent.getYear() + 10}"
        )
        monthAdapter.attachToRecyclerView(rcvMonth) { monthForm, percentPrev, scrollDirection ->
            motionMonth.progress = percentPrev
            tvMonthPrev.text = monthForm.toDisplayMonth()
            tvMonth.text = monthForm.getNextMonth()
        }
        monthAdapter.dayClick = {
            tvDateSelected.text = String.format("Ngày ${it.date}")
        }
        imgNextMonth.setOnClickListener {
            val pos = monthAdapter.posSelected + 1
            rcvMonth.smoothScrollToPosition(pos)
        }
        imgPrvMonth.setOnClickListener {
            val pos = monthAdapter.posSelected - 1
            rcvMonth.smoothScrollToPosition(pos)
        }
    }
}

