package com.huyhieu.mydocuments.utils.extensions

import java.text.SimpleDateFormat
import java.util.*

/**
 * Get values*/
fun Calendar.getDayCurrent() = this[Calendar.DAY_OF_MONTH]

fun Calendar.getDayOfWeek() = this[Calendar.DAY_OF_WEEK]

fun Calendar.getActualMaximum() = this.getActualMaximum(Calendar.DAY_OF_MONTH)

fun Calendar.getFirstDayOfWeekInMonth(): Int {
    val cal = this.clone() as Calendar
    cal.setDay(1)
    return cal.getDayOfWeek()
}

//0 -> 11
fun Calendar.getMonth() = this[Calendar.MONTH]

fun Calendar.getYear() = this[Calendar.YEAR]

/**
 * Set values*/
fun Calendar.setDay(day: Int) {
    this.set(Calendar.DAY_OF_MONTH, day)
}

fun Calendar.setMonth(month: Int) {
    this.set(Calendar.MONTH, month)
}

fun Calendar.setYear(year: Int) {
    this.set(Calendar.YEAR, year)
}

fun Calendar.nextMonth(value: Int = 1) {
    setMonth(getMonth() + value)
}

fun Calendar.prevMonth(value: Int = 1) {
    setMonth(getMonth() - value)
}

/**
 * Format*/
fun Calendar.formatToString(pattern: String = "dd/MM/yyyy"): String {
    val sdf = SimpleDateFormat(pattern, Locale.getDefault())
    return sdf.format(this.time)
}

fun String.formatToCalendar(pattern: String = "dd/MM/yyyy"): Calendar {
    val cal = Calendar.getInstance()
    val sdf = SimpleDateFormat(pattern, Locale.getDefault())
    cal.time = sdf.parse(this) as Date
    return cal
}