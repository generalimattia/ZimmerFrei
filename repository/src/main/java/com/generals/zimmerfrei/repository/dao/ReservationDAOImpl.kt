package com.generals.zimmerfrei.repository.dao

import android.arch.lifecycle.LiveData
import com.generals.zimmerfrei.repository.dao.room.RoomReservationDAO
import com.generals.zimmerfrei.repository.entities.ReservationEntity
import org.threeten.bp.OffsetDateTime
import javax.inject.Inject

class ReservationDAOImpl @Inject constructor(
    private val dao: RoomReservationDAO
) : ReservationDAO {

    override fun insert(reservation: ReservationEntity) {
        dao.insert(reservation)
    }

    override fun getAllReservations(): LiveData<List<ReservationEntity>> = dao.getAllReservations()

    override fun findReservationsByRoom(roomId: String): LiveData<List<ReservationEntity>> =
        dao.findReservationsByRoom(roomId)

    override fun findReservationsByDate(date: OffsetDateTime): LiveData<List<ReservationEntity>> =
        dao.findReservationsByDate(date)

    override fun findReservationsByRoomAndDateBetweenStartDateAndEndDate(
        roomId: String, date: OffsetDateTime
    ): LiveData<List<ReservationEntity>> =
        dao.findReservationsByRoomAndDateBetweenStartDateAndEndDate(roomId, date)

    override fun deleteAll() {
        dao.deleteAll()
    }
}