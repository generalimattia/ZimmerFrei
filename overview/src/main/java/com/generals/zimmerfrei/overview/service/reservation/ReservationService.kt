package com.generals.zimmerfrei.overview.service.reservation

import com.generals.zimmerfrei.model.Day
import com.generals.zimmerfrei.model.Reservation
import com.generals.zimmerfrei.model.Room
import io.reactivex.Flowable

interface ReservationService {

    fun fetchReservationsByDay(day: Day): Flowable<List<Reservation>>

    fun fetchReservationsByRoomAndDay(room: Room, day: Day): Flowable<List<Reservation>>
}