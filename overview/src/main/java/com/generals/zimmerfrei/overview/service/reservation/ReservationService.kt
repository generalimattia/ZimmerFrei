package com.generals.zimmerfrei.overview.service.reservation

import com.generals.zimmerfrei.model.*
import io.reactivex.Flowable
import org.threeten.bp.LocalDate

interface ReservationService {

    fun fetchReservationsByDay(day: Day): Flowable<List<Reservation>>

    fun fetchReservationsByRoomAndDay(room: Room, day: Day): Flowable<List<Reservation>>

    fun fetchReservationsFromDayToDayGroupedByRoom(startPeriod: LocalDate, endPeriod: LocalDate): Flowable<Map<Room, List<RoomDay>>>
}