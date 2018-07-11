package com.generals.zimmerfrei.repository.dao

import android.arch.lifecycle.LiveData
import com.generals.zimmerfrei.repository.dao.room.RoomReservationDAO
import com.generals.zimmerfrei.repository.entities.Reservation
import javax.inject.Inject

class ReservationDAOImpl @Inject constructor(
    private val dao: RoomReservationDAO
) : ReservationDAO {

    override fun insert(reservation: Reservation) {
        dao.insert(reservation)
    }

    override fun getAllReservations(): LiveData<List<Reservation>> = dao.getAllReservations()

    override fun findReservationsByRoom(roomId: String): LiveData<List<Reservation>> =
        dao.findReservationsByRoom(roomId)

    override fun deleteAll() {
        dao.deleteAll()
    }
}