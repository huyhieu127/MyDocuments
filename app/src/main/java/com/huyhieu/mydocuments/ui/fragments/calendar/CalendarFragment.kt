package com.huyhieu.mydocuments.ui.fragments.calendar

import android.os.Bundle
import androidx.recyclerview.widget.PagerSnapHelper
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

        val pagerSnapHelper = PagerSnapHelper()
        pagerSnapHelper.attachToRecyclerView(rcvMonth)

        rcvMonth.adapter = monthAdapter
        val start = System.currentTimeMillis()
        monthAdapter.setupMonths("12/12/2021", "12/10/2023")
        logDebug("Time open calendar = ${(System.currentTimeMillis() - start)}")
        monthAdapter.attachToRecyclerView(rcvMonth) { monthForm, percentPrev, scrollDirection ->
            //tvMonthPrev.text = String.format("Tháng ${calPrev.getMonth()}")
            motionMonth.progress = percentPrev
            logDebug("${monthForm.month} - ${monthForm.calendar.formatToString("MM/yyyy")}")
            when (scrollDirection) {
                1 -> {
                    //LEFT
                    tvMonthPrev.text = String.format("Tháng ${monthForm.month}")

                    val calSelected = monthForm.calendar.clone() as Calendar
                    calSelected.nextMonth()
                    tvMonth.text = calSelected.getMonthOfYear()
                }
                2 -> {
                    //RIGHT
                    tvMonthPrev.text = String.format("Tháng ${monthForm.month}")

                    val calSelected = monthForm.calendar.clone() as Calendar
                    calSelected.nextMonth()
                    val next = monthForm.month.formatToCalendar("MM/yyyy")
                    next.nextMonth()
                    tvMonth.text = String.format("Tháng ${next.formatToString("MM/yyyy")}")
                }
                else -> {
                    //NOTHING
                    tvMonthPrev.text = String.format("Tháng ${monthForm.month}")
                }
            }
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

    fun Calendar.getMonthOfYear(): String {
        return String.format("Tháng ${this.getMonth() + 1}/${this.getYear()}")
    }

    override fun FragmentCalendarBinding.addEvents(savedInstanceState: Bundle?) {
    }
}

