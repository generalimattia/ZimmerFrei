package com.generals.overview.service.calendar

import com.generals.overview.model.Day
import io.reactivex.Observable

interface CalendarService {

    fun loadCalendar(): Observable<Day>

}