package com.generals.zimmerfrei.repository.dao

import com.generals.zimmerfrei.repository.dao.room.RoomReservationDAO
import com.generals.zimmerfrei.repository.entities.ReservationEntity
import io.reactivex.Flowable
import org.threeten.bp.OffsetDateTime
import javax.inject.Inject

class ReservationDAOImpl @Inject constructor(
    private val dao: RoomReservationDAO
) : ReservationDAO {

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

    override fun findReservationsFromDateToDate(startDate: OffsetDateTime, endDate: OffsetDateTime): Flowable<List<ReservationEntity>> =
        dao.findReservationsFromDateToDate(startDate, endDate)

    override fun deleteAll() {
        dao.deleteAll()
    }
}