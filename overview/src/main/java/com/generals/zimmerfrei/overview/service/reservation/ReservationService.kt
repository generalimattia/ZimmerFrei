package com.generals.zimmerfrei.overview.service.reservation

import com.generals.zimmerfrei.model.*
import io.reactivex.Flowable
import io.reactivex.Observable
import org.threeten.bp.LocalDate

interface ReservationService {

    fun fetchReservationsByRoomAndDay(room: Room, day: Day): Flowable<List<Reservation>>

    fun fetchReservationsFromDayToDayGroupedByRoom(startPeriod: LocalDate, endPeriod: LocalDate): Observable<Pair<Room, List<RoomDay>>>
}