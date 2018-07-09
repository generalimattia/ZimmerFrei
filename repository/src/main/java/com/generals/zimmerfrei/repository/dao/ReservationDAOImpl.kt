package com.generals.zimmerfrei.repository.dao

import com.generals.zimmerfrei.repository.dao.room.RoomReservationDAO
import com.generals.zimmerfrei.repository.entities.Reservation
import javax.inject.Inject

class ReservationDAOImpl @Inject constructor(
    private val dao: RoomReservationDAO
) : ReservationDAO {

    override fun insert(reservation: Reservation) {
        dao.insert(reservation)
    }

    override fun getAllReservations(onComplete: (List<Reservation>) -> Unit) {
        onComplete(dao.getAllReservations())
    }

    override fun deleteAll() {
        dao.deleteAll()
    }
}