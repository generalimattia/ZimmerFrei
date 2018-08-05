package com.generals.zimmerfrei.overview.usecase

import com.generals.zimmerfrei.model.Day
import com.generals.zimmerfrei.model.DayWithReservations
import com.generals.zimmerfrei.model.Room
import com.generals.zimmerfrei.model.RoomDay
import io.reactivex.Observable
import org.threeten.bp.LocalDate

interface OverviewUseCase {

    fun loadRooms(): Observable<List<Room>>

    fun loadDays(date: LocalDate): Observable<Pair<List<Day>, String>>

    fun loadCalendar(): Observable<DayWithReservations>

    fun loadReservationsByRoom(startDate: LocalDate, endDate: LocalDate): Observable<Map<Room, List<RoomDay>>>
}