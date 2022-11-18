package com.huyhieu.widget.commons.calendar

import com.huyhieu.library.constants.CalendarCst
import com.huyhieu.library.extensions.formatToCalendar
import com.huyhieu.library.extensions.nextMonth
import com.huyhieu.library.extensions.prevMonth
import com.huyhieu.library.extensions.toDisplayMonth

@Suppress("MemberVisibilityCanBePrivate")
class MonthForm(
    var month: String = "",
    var lstDays: MutableList<DayForm>? = mutableListOf()
) {
    fun getMonthOfYear() = month.substring(0, month.indexOf("/"))

    fun getYear() = month.substring(month.indexOf("/") + 1, month.length)

    fun toDisplayMonth(): String {
        return CalendarCst.DISPLAY_MONTH.format("${getMonthOfYear()}/${getYear()}")
    }

    fun toCalendar() = month.formatToCalendar(CalendarCst.FORMAT_MONTH_DEFAULT)

    fun getNextMonth(value: Int = 1): String {
        val c = toCalendar().nextMonth(value)
        return c.toDisplayMonth()
    }

    fun getPrevMonth(value: Int = 1): String {
        val c = toCalendar().prevMonth(value)
        return c.toDisplayMonth()
    }
}
