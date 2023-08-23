package com.huyhieu.mydocuments.libraries.commons.calendar.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.huyhieu.mydocuments.libraries.commons.calendar.DayForm
import com.huyhieu.mydocuments.libraries.commons.calendar.MonthForm
import com.huyhieu.mydocuments.libraries.constants.CalendarCst
import com.huyhieu.mydocuments.databinding.WidgetCalendarMonthOfYearBinding
import com.huyhieu.mydocuments.libraries.extensions.*
import com.huyhieu.mydocuments.libraries.utils.logError
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

    private var timeMillisSelected: Long = 0L
    private var timeMillisStart: Long = 0L
    private var timeMillisEnd: Long = 0L

    private var isMinMonth = false
    private var isMaxMonth = false

    @Suppress("MemberVisibilityCanBePrivate")
    var daySelected = ""
    lateinit var cSelected: Calendar

    lateinit var cPrev: Calendar
    lateinit var cNext: Calendar

    var posSelected = 0
    var dayClick: ((DayForm) -> Unit)? = null
    private val lstMonths = mutableListOf<MonthForm>()
    private var dayAdapterSelected: DaysOfCalendarAdapter? = null

    inner class MonthViewHolder(val binding: WidgetCalendarMonthOfYearBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private val daysOfCalendarAdapter = DaysOfCalendarAdapter()

        fun bindViewHolder() {
            binding.apply {
                val item = lstMonths[layoutPosition]
                rcvDays.adapter = daysOfCalendarAdapter
                daysOfCalendarAdapter.dayClick = dayClick
                daysOfCalendarAdapter.onDaySelected = {
                    if (dayAdapterSelected != it) {
                        updateDayOld()
                        dayAdapterSelected = it
                    }
                }
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

    fun updateDayOld() {
        dayAdapterSelected?.updateDayFromOtherMonth()
    }

    /********************************************************************************************/

    fun setupMonths(daySelected: String = "", minDay: String = "", maxDay: String = "") {
        try {
            this.daySelected = daySelected
            this.minDay = minDay
            this.maxDay = maxDay

            if (minDay.isNotEmpty()) {
                val cMin = minDay.formatToCalendar().setDay(1)
                this.timeMillisStart = cMin.timeInMillis
            }
            if (maxDay.isNotEmpty()) {
                val cMax = maxDay.formatToCalendar().setDay(1)
                this.timeMillisEnd = cMax.timeInMillis
            }

            if (timeMillisStart > timeMillisEnd) {
                throw IllegalStateException("minDay cannot be greater than maxDay")
            }

            cSelected = if (daySelected.isNotEmpty()) {
                daySelected.formatToCalendar().clean(CalendarCst.FORMAT_MONTH_DEFAULT)
            } else {
                Calendar.getInstance().clean(CalendarCst.FORMAT_MONTH_DEFAULT)
            }
            this.timeMillisSelected = cSelected.timeInMillis

            if (timeMillisSelected < timeMillisStart || timeMillisSelected > timeMillisEnd) {
                throw IllegalStateException("daySelected is not in the range minDay - maxDay")
            }
            //Add previous month's calendar
            if (timeMillisStart < timeMillisSelected) {
                cPrev = cSelected.new().prevMonth()
                isMinMonth = cPrev.timeInMillis == timeMillisStart
                lstMonths.add(cPrev.createMonthFrom())
            } else {
                isMinMonth = true
            }
            //Add the calendar of the selected month
            lstMonths.add(cSelected.createMonthFrom())
            posSelected = lstMonths.size - 1
            //Add next month's calendar
            if (timeMillisEnd > timeMillisSelected) {
                cNext = cSelected.new().nextMonth()
                isMaxMonth = cNext.timeInMillis == timeMillisEnd
                lstMonths.add(cNext.createMonthFrom())
            } else {
                isMaxMonth = true
            }
        } catch (ex: Exception) {
            logError("setupMonths: ${ex.message}")
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
                    val dayForm = DayForm(
                        day = day.toString(),
                        date = date,
                        isToday = date == dateCurrent,
                        isSelected = date == daySelected
                    )
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
        rcv.scrollToPosition(posSelected)
        rcv.addOnScrollListener(object : RecyclerView.OnScrollListener() {

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
                posSelected = lm.findFirstVisibleItemPosition()
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    addNewCalendar(posSelected)
                }
            }
        })
    }

    fun addNewCalendar(posSelected: Int) {
        if (posSelected == 0 && !isMinMonth) {
            cPrev.prevMonth()
            isMinMonth = cPrev.timeInMillis == timeMillisStart
            lstMonths.add(0, cPrev.createMonthFrom())
            notifyItemInserted(0)
        } else {
            if (!isMaxMonth) {
                if (posSelected == lstMonths.size - 1) {
                    cNext.nextMonth()
                    isMaxMonth = cNext.timeInMillis == timeMillisEnd
                    lstMonths.add(cNext.createMonthFrom())
                    notifyItemInserted(lstMonths.size)
                }
            }
        }
    }

    private fun Calendar.createMonthFrom() =
        MonthForm(this.formatToString(CalendarCst.FORMAT_MONTH_DEFAULT), calculateDays(this))
}