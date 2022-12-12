package com.huyhieu.library.commons.calendar

class DayForm(
    var day: String = "",
    var date: String = "",
    var data: Any? = null,
    var isDayOfPrevMonth: Boolean = false,
    var isDayOfNextMonth: Boolean = false,
    var isToday: Boolean = false,
    var isSelected: Boolean = false
) {
    fun isDayAvailable() = !isDayOfPrevMonth && !isDayOfNextMonth
}