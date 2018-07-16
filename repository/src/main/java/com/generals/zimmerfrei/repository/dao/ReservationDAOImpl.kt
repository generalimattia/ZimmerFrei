package com.generals.zimmerfrei.repository.dao

import android.content.Context
import com.generals.zimmerfrei.repository.dao.room.RoomReservationDAO
import com.generals.zimmerfrei.repository.database.ReservationDatabase
import com.generals.zimmerfrei.repository.entities.ReservationEntity
import io.reactivex.Flowable
import org.threeten.bp.OffsetDateTime
import javax.inject.Inject

class ReservationDAOImpl @Inject constructor(
    context: Context
) : ReservationDAO {

    private val dao: RoomReservationDAO = ReservationDatabase.getDatabase(context).reservationDAO()

    override fun insert(reservation: ReservationEntity) {
        dao.insert(reservation)
    }

    override fun getAllReservations(): Flowable<List<ReservationEntity>> = dao.getAllReservations()

    override fun findReservationsByRoom(roomId: String): Flowable<List<ReservationEntity>> =
        dao.findReservationsByRoom(roomId)

    override fun findReservationsByDate(date: OffsetDateTime): Flowable<List<ReservationEntity>> =
        dao.findReservationsByDate(date)

    override fun findReservationsByRoomAndDateBetweenStartDateAndEndDate(
        roomId: String, date: OffsetDateTime
    ): Flowable<List<ReservationEntity>> =
        dao.findReservationsByRoomAndDateBetweenStartDateAndEndDate(roomId, date)

    override fun deleteAll() {
        dao.deleteAll()
    }
}