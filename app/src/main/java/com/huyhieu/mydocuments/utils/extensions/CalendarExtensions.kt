package com.huyhieu.mydocuments.utils.extensions

import com.huyhieu.mydocuments.ui.fragments.calendar.CalendarCst
import java.text.SimpleDateFormat
import java.util.*

/**
 * Get values*/
fun Calendar.getDayOfMonth() = this[Calendar.DAY_OF_MONTH]

fun Calendar.getDayOfWeek() = this[Calendar.DAY_OF_WEEK]

fun Calendar.getActualMaximum() = this.getActualMaximum(Calendar.DAY_OF_MONTH)

fun Calendar.getDayOfWeekInMonth(day: Int = 1, isKeep: Boolean = true): Int {
    val cal = if (isKeep) this.setDay(day) else this.new(day)
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
fun Calendar.setDay(day: Int) = this.apply { set(Calendar.DAY_OF_MONTH, day) }

fun Calendar.setMonth(month: Int) = this.apply { set(Calendar.MONTH, month) }

fun Calendar.setYear(year: Int) = this.apply { set(Calendar.YEAR, year) }

fun Calendar.nextMonth(value: Int = 1) = this.apply { setMonth(getMonth() + value) }

fun Calendar.prevMonth(value: Int = 1) = this.apply { setMonth(getMonth() - value) }

fun Calendar.new(day: Int = 1): Calendar {
    val cNew = (this.clone() as Calendar)
    cNew.setDay(day)
    return cNew
}

fun Calendar.clean(
    pattern: String = CalendarCst.FORMAT_DATE_DEFAULT,
    patternTo: String = ""
): Calendar {
    val patternOutput = patternTo.ifEmpty { pattern }
    return formatToString(pattern).formatToCalendar(patternOutput)
}

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