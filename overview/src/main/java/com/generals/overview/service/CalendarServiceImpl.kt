package com.generals.overview.service

import com.generals.overview.model.Day
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class CalendarServiceImpl @Inject constructor() : CalendarService {

    companion object {
        private val dayFormat = SimpleDateFormat("EEE", Locale.getDefault())
    }

    override fun loadCalendar(): List<Day> {
        val calendarHaolder = Calendar.getInstance()
        val numOfDays = calendarHaolder.getActualMaximum(Calendar.DAY_OF_MONTH)
        return MutableList(numOfDays) { index: Int ->
            calendarHaolder.set(Calendar.DAY_OF_MONTH, index)
            val num: Int = index + 1
            Day("""$num ${dayFormat.format(calendarHaolder.time).toUpperCase()}""")
        }
    }
}