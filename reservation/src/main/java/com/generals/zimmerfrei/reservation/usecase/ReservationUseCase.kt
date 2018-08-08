package com.generals.zimmerfrei.reservation.usecase

import com.generals.zimmerfrei.model.Reservation

interface ReservationUseCase {

    fun save(reservation: Reservation)
}