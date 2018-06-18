package com.generals.zimmerfrei.features.overview.service

import com.generals.zimmerfrei.model.Day
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class CalendarServiceImpl @Inject constructor(): CalendarService {

    override fun loadCalendar(): List<Day> {
        val calendarHaolder = Calendar.getInstance()
        val numOfDays = calendarHaolder.getActualMaximum(Calendar.DAY_OF_MONTH)
        val dateFormat = SimpleDateFormat("EEEE", Locale.getDefault())
        return MutableList(numOfDays) { index: Int ->
            calendarHaolder.set(Calendar.DAY_OF_MONTH, index)
            Day(dateFormat.format(calendarHaolder.time))
        }
    }
}