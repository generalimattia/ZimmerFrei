package com.generals.zimmerfrei.reservation.usecase

import com.generals.zimmerfrei.model.Reservation
import io.reactivex.Single

interface ReservationUseCase {

    fun save(reservation: Reservation): Single<Unit>
}