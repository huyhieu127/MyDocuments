package com.huyhieu.mydocuments.utils.extensions

import com.huyhieu.mydocuments.ui.fragments.calendar.CalendarCst
import java.text.SimpleDateFormat
import java.util.*

/**
 * Get values*/
fun Calendar.getDayCurrent() = this[Calendar.DAY_OF_MONTH]

fun Calendar.getDayOfWeek() = this[Calendar.DAY_OF_WEEK]

fun Calendar.getActualMaximum() = this.getActualMaximum(Calendar.DAY_OF_MONTH)

fun Calendar.getFirstDayOfWeekInMonth(isKeep: Boolean = true): Int {
    val cal = if (isKeep) this else this.new()
    cal.setDay(1)
    return cal.getDayOfWeek()
}

fun Calendar.getEndDayOfWeekInMonth(isKeep: Boolean = true): Int {
    val cal = if (isKeep) this else this.new()
    cal.setDay(getActualMaximum())
    return cal.getDayOfWeek()
}

//0 -> 11
fun Calendar.getMonth() = this[Calendar.MONTH]

fun Calendar.getYear() = this[Calendar.YEAR]

fun Calendar.getDisplayDayOfWeek() = when (getDayOfWeek()) {
    1 -> "CN"
    2 -> "Thứ 2"
    3 -> "Thứ 3"
    4 -> "Thứ 4"
    5 -> "Thứ 5"
    6 -> "Thứ 6"
    else -> {
        "Thứ 7"
    }
}

/**
 * Set values*/
fun Calendar.setDay(day: Int): Calendar {
    this.set(Calendar.DAY_OF_MONTH, day)
    return this
}

fun Calendar.setMonth(month: Int): Calendar {
    this.set(Calendar.MONTH, month)
    return this
}

fun Calendar.setYear(year: Int): Calendar {
    this.set(Calendar.YEAR, year)
    return this
}

fun Calendar.nextMonth(value: Int = 1): Calendar {
    setMonth(getMonth() + value)
    return this
}

fun Calendar.prevMonth(value: Int = 1): Calendar {
    setMonth(getMonth() - value)
    return this
}

fun Calendar.new() = (this.clone() as Calendar)

/**
 * Format*/
fun Calendar.formatToString(pattern: String = CalendarCst.FORMAT_DATE_DEFAULT): String {
    val sdf = SimpleDateFormat(pattern, Locale.getDefault())
    return sdf.format(this.time)
}

fun String.formatToCalendar(pattern: String = CalendarCst.FORMAT_DATE_DEFAULT): Calendar {
    val cal = Calendar.getInstance()
    val sdf = SimpleDateFormat(pattern, Locale.getDefault())
    cal.time = sdf.parse(this) as Date
    return cal
}

fun Calendar.toDisplayMonth(): String {
    return CalendarCst.DISPLAY_MONTH.format(
        "${this.getMonth() + 1}/${this.getYear()}".padStart(
            7,
            '0'
        )
    )
}