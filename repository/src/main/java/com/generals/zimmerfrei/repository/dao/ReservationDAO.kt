package com.generals.zimmerfrei.repository.dao

import com.generals.zimmerfrei.repository.entities.Reservation

interface ReservationDAO {

    fun insert(reservation: Reservation)

    fun getAllReservations(onComplete: (List<Reservation>) -> Unit)

    fun deleteAll()
}