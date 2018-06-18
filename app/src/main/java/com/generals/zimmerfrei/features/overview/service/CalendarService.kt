package com.generals.zimmerfrei.features.overview.service

import com.generals.zimmerfrei.model.Day

interface CalendarService {

    fun loadCalendar(): List<Day>

}