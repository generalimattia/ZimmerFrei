package com.generals.zimmerfrei.overview.service.calendar

import com.generals.zimmerfrei.overview.model.Day
import io.reactivex.Observable

interface CalendarService {

    fun loadCalendar(): Observable<Day>

}