package com.huyhieu.mydocuments.ui.fragments.calendar

import java.util.*

class MonthForm(
    var month: String = "",
    var calendar: Calendar,
    var lstDays: MutableList<DayForm>? = mutableListOf()
)