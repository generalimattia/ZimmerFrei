package com.generals.overview.service

import com.generals.overview.model.Day

interface CalendarService {

    fun loadCalendar(): List<Day>

}