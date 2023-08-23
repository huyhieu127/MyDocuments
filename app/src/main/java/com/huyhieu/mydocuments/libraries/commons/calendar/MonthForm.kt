package com.huyhieu.mydocuments.libraries.commons.calendar

import com.huyhieu.mydocuments.libraries.constants.CalendarCst
import com.huyhieu.mydocuments.libraries.extensions.formatToCalendar
import com.huyhieu.mydocuments.libraries.extensions.nextMonth
import com.huyhieu.mydocuments.libraries.extensions.prevMonth
import com.huyhieu.mydocuments.libraries.extensions.toDisplayMonth

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
