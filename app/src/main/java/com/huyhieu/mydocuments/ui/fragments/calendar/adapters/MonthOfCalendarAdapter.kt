package com.huyhieu.mydocuments.ui.fragments.calendar.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.huyhieu.mydocuments.databinding.WidgetCalendarMonthOfYearBinding
import com.huyhieu.mydocuments.ui.fragments.calendar.CalendarCst
import com.huyhieu.mydocuments.ui.fragments.calendar.DayForm
import com.huyhieu.mydocuments.ui.fragments.calendar.MonthForm
import com.huyhieu.mydocuments.utils.extensions.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class MonthOfCalendarAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    @Suppress("MemberVisibilityCanBePrivate")
    var minDay = ""

    @Suppress("MemberVisibilityCanBePrivate")
    var maxDay = ""

    private var timeMillisStart: Long = 0L
    private var timeMillisEnd: Long = 0L

    @Suppress("MemberVisibilityCanBePrivate")
    var daySelected = ""
    lateinit var cSelected: Calendar

    lateinit var cPrev: Calendar
    lateinit var cNext: Calendar

    var posSelected = 0
    var dayClick: ((DayForm) -> Unit)? = null
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

    fun setupMonths(daySelected: String = "", minDay: String = "", maxDay: String = "") {
        try {
            this.daySelected = daySelected
            this.minDay = minDay
            this.maxDay = maxDay

            val cMin = minDay.formatToCalendar().setDay(1)
            this.timeMillisStart = cMin.timeInMillis
            val cMax = maxDay.formatToCalendar().setDay(1)
            this.timeMillisEnd = cMax.timeInMillis

            cSelected = if (daySelected.isNotEmpty()) {
                daySelected.formatToCalendar()
            } else {
                Calendar.getInstance().clean(CalendarCst.FORMAT_MONTH_DEFAULT)
            }

            cPrev = cSelected.new().prevMonth()
            cNext = cSelected.new().nextMonth()
            lstMonths.add(cPrev.createMonthFrom())
            lstMonths.add(cSelected.createMonthFrom())
            lstMonths.add(cNext.createMonthFrom())
        } catch (_: Exception) {

        }
    }

    private fun calculateDays(calendar: Calendar): MutableList<DayForm> {
        var lstDays = mutableListOf<DayForm>()
        CoroutineScope(Dispatchers.IO).launch {
            lstDays = suspendCoroutine {
                val dateCurrent = Calendar.getInstance().formatToString()
                val numDayInMonth = calendar.getActualMaximum()

                //Days of previous month
                val cPrev = calendar.new().prevMonth()
                val numDayInPrevMonth = cPrev.getActualMaximum()
                val dayOfWeekInPrevMonth = cPrev.getDayOfWeekInMonth(numDayInPrevMonth) - 1
                val startDayOfPrevMonthInCalendar = numDayInPrevMonth - dayOfWeekInPrevMonth
                (startDayOfPrevMonthInCalendar..numDayInPrevMonth).forEach { day ->
                    lstDays.add(DayForm("$day", isDayOfPrevMonth = true))
                }
                //Days of current month
                (1..numDayInMonth).forEach { day ->
                    val cal = calendar.new().setDay(day)
                    val date = cal.formatToString()
                    val dayForm = DayForm(day.toString(), date, isSelected = date == dateCurrent)
                    lstDays.add(dayForm)
                }
                //Days of next month
                (1..42 - lstDays.size).forEach { day ->
                    lstDays.add(DayForm("$day", isDayOfNextMonth = true))
                }
                it.resume(lstDays)
            }
        }
        return lstDays
    }

    //Default lstMonths have 3 items
    fun attachToRecyclerView(
        rcv: RecyclerView,
        onMonthChanged: ((monthForm: MonthForm, percentPrev: Float, scrollDirection: Int) -> Unit)? = null
    ) {
        rcv.scrollToPosition(1)
        rcv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            var isMinMonth = false
            var isMaxMonth = false

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val lm = rcv.layoutManager as LinearLayoutManager
                posSelected = lm.findFirstVisibleItemPosition()
                val viewHolderCurrent =
                    rcv.findViewHolderForAdapterPosition(posSelected) as MonthViewHolder
                val percentPrevValue =
                    1F - (-(viewHolderCurrent.itemView.x.toInt()) / rcv.width.toFloat())

                val direction = if (dx > 0) {
                    //RIGHT
                    2
                } else if (dx < 0) {
                    //LEFT
                    1
                } else {
                    //NOTHING
                    0
                }
                onMonthChanged?.invoke(lstMonths[posSelected], percentPrevValue, direction)
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                val lm = rcv.layoutManager as LinearLayoutManager
                val posSelected = lm.findFirstVisibleItemPosition()
                if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    if (posSelected == 0 && !isMinMonth) {
                        cPrev.prevMonth()
                        isMinMonth = cPrev.timeInMillis == timeMillisStart
                        lstMonths.add(0, cPrev.createMonthFrom())
                        notifyItemInserted(0)
                    } else {
                        if (!isMaxMonth) {
                            val posLast = lm.findLastVisibleItemPosition()
                            if (posLast == lstMonths.size - 1) {
                                cNext.nextMonth()
                                isMaxMonth = cNext.timeInMillis == timeMillisEnd
                                lstMonths.add(cNext.createMonthFrom())
                                notifyItemInserted(lstMonths.size)
                            }
                        }
                    }
                }
            }
        })
    }

    private fun Calendar.createMonthFrom() =
        MonthForm(this.formatToString(CalendarCst.FORMAT_MONTH_DEFAULT), calculateDays(this))
}