package com.generals.zimmerfrei.overview.usecase

import com.generals.roomrepository.RoomRepository
import com.generals.zimmerfrei.model.Day
import com.generals.zimmerfrei.model.Room
import com.generals.zimmerfrei.model.RoomDay
import com.generals.zimmerfrei.overview.service.calendar.CalendarService
import com.generals.zimmerfrei.overview.service.reservation.ReservationService
import io.reactivex.Maybe
import io.reactivex.Observable
import org.threeten.bp.LocalDate
import javax.inject.Inject

class OverviewUseCaseImpl @Inject constructor(
        private val calendarService: CalendarService,
        private val reservationService: ReservationService,
        private val roomRepository: RoomRepository
) : OverviewUseCase {

    override fun loadDays(date: LocalDate): Observable<Pair<List<Day>, String>> =
            calendarService.loadDays(date)

    override fun loadRooms(): Maybe<List<Room>> = roomRepository.getAllRooms()

    override fun loadReservations(
            startPeriod: LocalDate, endPeriod: LocalDate
    ): Observable<Pair<Room, List<RoomDay>>> =
            reservationService.fetchReservationsFromDayToDayGroupedByRoom(
                    startPeriod,
                    endPeriod
            )
}