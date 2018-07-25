package com.generals.zimmerfrei.overview.service.calendar

import com.generals.zimmerfrei.model.Day
import io.reactivex.Observable

interface CalendarService {

    fun monthDays(): Int

    fun loadCalendar(): Observable<Day>

}