package com.generals.zimmerfrei.reservation.usecase

import com.generals.zimmerfrei.model.Reservation
import com.generals.zimmerfrei.repository.dao.ReservationDAO
import javax.inject.Inject

class ReservationUseCaseImpl @Inject constructor(
    private val dao: ReservationDAO
): ReservationUseCase {

    override fun save(reservation: Reservation) {
        dao.insert(reservation.toEntity())
    }
}