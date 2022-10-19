package com.huyhieu.mydocuments.ui.fragments.calendar

import android.os.Bundle
import androidx.recyclerview.widget.PagerSnapHelper
import com.huyhieu.mydocuments.R
import com.huyhieu.mydocuments.base.BaseFragment
import com.huyhieu.mydocuments.databinding.FragmentCalendarBinding
import com.huyhieu.mydocuments.ui.fragments.calendar.adapters.MonthOfCalendarAdapter
import com.huyhieu.mydocuments.utils.extensions.*
import com.huyhieu.mydocuments.utils.logDebug
import java.util.*

class CalendarFragment : BaseFragment<FragmentCalendarBinding>() {
    private val monthAdapter = MonthOfCalendarAdapter()

    override fun initializeBinding() = FragmentCalendarBinding.inflate(layoutInflater)

    override fun FragmentCalendarBinding.addControls(savedInstanceState: Bundle?) {
        mActivity?.setDarkColorStatusBar()

        val cCurrent = Calendar.getInstance()
        cCurrent.setDay(7)
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
        val start = System.currentTimeMillis()
        monthAdapter.setupMonths("12/12/2021", "12/10/2023")
        logDebug("Time open calendar = ${(System.currentTimeMillis() - start)}")
        monthAdapter.attachToRecyclerView(rcvMonth) { monthForm, percentPrev, scrollDirection ->
            motionMonth.progress = percentPrev
            tvMonthPrev.text = monthForm.toDisplayMonth()
            tvMonth.text = monthForm.getNextMonth()
        }
        monthAdapter.dayClick = {
            tvDateSelected.text = String.format("Ngày ${it.date}")
        }
        imgNextMonth.setOnClickListener {
            rcvMonth.smoothScrollToPosition(monthAdapter.posSelected + 1)
        }
        imgPrvMonth.setOnClickListener {
            rcvMonth.smoothScrollToPosition(monthAdapter.posSelected - 1)
        }
    }

    override fun FragmentCalendarBinding.addEvents(savedInstanceState: Bundle?) {
    }
}

