package com.generals.zimmerfrei.overview.service.calendar

import com.generals.zimmerfrei.model.Day
import io.reactivex.Observable
import org.threeten.bp.LocalDate

interface CalendarService {

    fun monthDays(): Int

    fun loadDays(date: LocalDate): Observable<Pair<List<Day>, String>>

    fun loadCalendar(): Observable<Day>

}