package com.generals.zimmerfrei.reservation.usecase

import com.generals.zimmerfrei.model.Reservation
import com.generals.zimmerfrei.model.Room
import io.reactivex.Observable
import io.reactivex.Single

interface ReservationUseCase {

    fun save(reservation: Reservation): Single<Unit>

    fun getRoomByName(name: String): Observable<Room>
}