package com.generals.zimmerfrei.reservation.usecase

import com.generals.zimmerfrei.model.Reservation
import com.generals.zimmerfrei.model.Room
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single

interface ReservationUseCase {

    fun save(reservation: Reservation): Single<Unit>

    fun getAllRooms(): Maybe<List<Room>>

    fun getRoomByListPosition(position: Int): Maybe<Room>
}