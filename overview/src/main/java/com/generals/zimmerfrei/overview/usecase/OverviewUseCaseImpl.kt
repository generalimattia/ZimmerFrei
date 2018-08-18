package com.generals.zimmerfrei.overview.usecase

import com.generals.zimmerfrei.model.Day
import com.generals.zimmerfrei.model.Room
import com.generals.zimmerfrei.model.RoomDay
import com.generals.zimmerfrei.overview.service.calendar.CalendarService
import com.generals.zimmerfrei.overview.service.reservation.ReservationService
import com.generals.zimmerfrei.service.RoomFetcherService
import io.reactivex.Observable
import org.threeten.bp.LocalDate
import javax.inject.Inject

class OverviewUseCaseImpl @Inject constructor(
    private val calendarService: CalendarService,
    private val reservationService: ReservationService,
    private val roomService: RoomFetcherService
) : OverviewUseCase {

    override fun loadDays(date: LocalDate): Observable<Pair<List<Day>, String>> =
        calendarService.loadDays(date)

    override fun loadRooms(): Observable<List<Room>> = roomService.fetchRooms().toObservable()

    override fun loadReservations(
        startPeriod: LocalDate, endPeriod: LocalDate
    ): Observable<Pair<Room, List<RoomDay>>> =
        reservationService.fetchReservationsFromDayToDayGroupedByRoom(
            startPeriod,
            endPeriod
        )
}