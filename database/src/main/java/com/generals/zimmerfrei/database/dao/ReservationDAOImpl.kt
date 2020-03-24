package com.generals.zimmerfrei.database.dao

import com.generals.zimmerfrei.database.dao.room.RoomReservationDAO
import com.generals.zimmerfrei.database.entities.ReservationEntity
import io.reactivex.Flowable
import org.threeten.bp.LocalDate
import javax.inject.Inject

class ReservationDAOImpl @Inject constructor(
        private val dao: RoomReservationDAO
) : ReservationDAO {

    override fun insert(reservation: ReservationEntity) = dao.insert(reservation)

    override fun getAllReservations(): Flowable<List<ReservationEntity>> = dao.getAllReservations()

    override fun findReservationsByRoom(roomId: String): Flowable<List<ReservationEntity>> =
            dao.findReservationsByRoom(roomId)

    override fun findReservationsByDate(date: LocalDate): Flowable<List<ReservationEntity>> =
            dao.findReservationsByDate(date)

    override fun findReservationsByRoomAndDateBetweenStartDateAndEndDate(
            roomId: String, date: LocalDate
    ): Flowable<List<ReservationEntity>> =
            dao.findReservationsByRoomAndDateBetweenStartDateAndEndDate(
                    roomId,
                    date
            )

    override fun findReservationsByRoomAndFromDateToDate(
            roomId: Long, startDate: LocalDate, endDate: LocalDate
    ): Flowable<List<ReservationEntity>>? = dao.findReservationsByRoomAndFromDateToDate(
            roomId,
            startDate,
            endDate
    )

    override fun findReservationsFromDateToDate(
            startDate: LocalDate, endDate: LocalDate
    ): Flowable<List<ReservationEntity>> = dao.findReservationsFromDateToDate(
            startDate,
            endDate
    )

    override fun update(reservation: ReservationEntity) {
        dao.update(reservation)
    }

    override fun delete(reservation: ReservationEntity) {
        dao.delete(reservation)
    }

    override fun deleteAll() {
        dao.deleteAll()
    }
}