package com.huyhieu.mydocuments.ui.fragments.calendar.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.huyhieu.mydocuments.databinding.WidgetCalendarMonthOfYearBinding
import com.huyhieu.mydocuments.ui.fragments.calendar.DayForm
import com.huyhieu.mydocuments.ui.fragments.calendar.MonthForm
import com.huyhieu.mydocuments.utils.extensions.*
import kotlinx.coroutines.*
import java.util.*

class MonthOfCalendarAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var dayClick: ((DayForm) -> Unit)? = null
    var startDay = ""
    var endDay = ""

    val cCurrent: Calendar = Calendar.getInstance()
    lateinit var cPrev: Calendar
    lateinit var cNext: Calendar

    private val lstMonths = mutableListOf<MonthForm>()

    inner class MonthViewHolder(val binding: WidgetCalendarMonthOfYearBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private val daysOfCalendarAdapter = DaysOfCalendarAdapter()

        fun bindViewHolder() {
            binding.apply {
                val item = lstMonths[layoutPosition]
                rcvDays.adapter = daysOfCalendarAdapter
                daysOfCalendarAdapter.dayClick = dayClick
                if (!item.lstDays.isNullOrEmpty()) {
                    daysOfCalendarAdapter.fillListDays(item.lstDays!!)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = WidgetCalendarMonthOfYearBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MonthViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder !is MonthViewHolder) return
        holder.bindViewHolder()
    }

    override fun getItemCount(): Int = lstMonths.size

    fun setupMonths(startDay: String, endDay: String) {
        try {
            this.startDay = startDay
            this.endDay = endDay
            cPrev = cCurrent.clone() as Calendar
            cPrev.prevMonth()
            cNext = cCurrent.clone() as Calendar
            cNext.nextMonth()
            lstMonths.add(cPrev.createMonthFrom())
            lstMonths.add(cCurrent.createMonthFrom())
            lstMonths.add(cNext.createMonthFrom())
        } catch (_: Exception) {

        }
    }

    private fun calculateDays(calendar: Calendar): MutableList<DayForm> {
        val lstDays = mutableListOf<DayForm>()
        CoroutineScope(Dispatchers.IO).launch {
            supervisorScope {
                val dateCurrent = Calendar.getInstance().formatToString()
                val numDayInMonth = calendar.getActualMaximum()

                //Days of previous month
                val firstDayOfWeekInMonth = calendar.getFirstDayOfWeekInMonth()
                val numDayInPrevMonth = (calendar.clone() as Calendar).getActualMaximum()
                val endDayInPrevMonth = firstDayOfWeekInMonth - 2
                val startDayOfPrevInCurrentMonth = numDayInPrevMonth - endDayInPrevMonth
                (startDayOfPrevInCurrentMonth..numDayInPrevMonth).forEach {
                    lstDays.add(DayForm("$it", isDayOfPrevMonth = true))
                }
                //Days of current month
                (1..numDayInMonth).forEach {
                    val cal = calendar.clone() as Calendar
                    cal.setDay(it)
                    val date = cal.formatToString()
                    val dayForm = DayForm(it.toString(), date, isSelected = date == dateCurrent)
                    lstDays.add(dayForm)
                }
                //Days of next month
                (1..42 - lstDays.size).forEach {
                    lstDays.add(DayForm("$it", isDayOfNextMonth = true))
                }
            }
        }
        return lstDays
    }

    //Default lstMonths have 3 items
    fun attachToRecyclerView(rcv: RecyclerView, onMonthChanged: ((MonthForm) -> Unit)? = null) {
        if (startDay.isNotEmpty()) {
            rcv.scrollToPosition(1)
        }
        rcv.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    //Dragging
                } else if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    val lm = rcv.layoutManager as LinearLayoutManager
                    val posCurrent = lm.findFirstVisibleItemPosition()
                    onMonthChanged?.invoke(lstMonths[posCurrent])

                    CoroutineScope(Dispatchers.IO).launch {
                        if (posCurrent == 0) {
                            cPrev.prevMonth()
                            lstMonths.add(0, cPrev.createMonthFrom())
                            withContext(Dispatchers.Main) {
                                notifyItemInserted(0)
                                rcv.context.showToastShort((cPrev.getMonth() + 1).toString())
                            }
                        } else {
                            val posLast = lm.findLastVisibleItemPosition()
                            if (posLast == lstMonths.size - 1) {
                                cNext.nextMonth()
                                lstMonths.add(cNext.createMonthFrom())
                                withContext(Dispatchers.Main) {
                                    notifyItemInserted(lstMonths.size)
                                    rcv.context.showToastShort((cPrev.getMonth() + 1).toString())
                                }
                            }
                        }
                    }

                }
            }
        })
    }

    private fun Calendar.createMonthFrom() =
        MonthForm(this.formatToString("MM/yyyy"), this, calculateDays(this))
}