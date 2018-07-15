package com.generals.zimmerfrei.overview.service.reservation

import com.generals.zimmerfrei.overview.model.Day
import com.generals.zimmerfrei.overview.model.Reservation
import com.generals.zimmerfrei.overview.model.Room
import io.reactivex.Flowable

interface ReservationService {

    fun fetchReservationsByDay(day: Day): Flowable<List<Reservation>>

    fun fetchReservationsByRoomAndDay(room: Room, day: Day): Flowable<List<Reservation>>
}