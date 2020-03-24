package com.generals.zimmerfrei.database.dao

import com.generals.zimmerfrei.database.entities.ReservationEntity
import io.reactivex.Flowable
import org.threeten.bp.LocalDate

interface ReservationDAO {

    fun insert(reservation: ReservationEntity)

    fun getAllReservations(): Flowable<List<ReservationEntity>>

    fun findReservationsByRoom(roomId: String): Flowable<List<ReservationEntity>>

    fun findReservationsByDate(date: LocalDate): Flowable<List<ReservationEntity>>

    fun findReservationsByRoomAndDateBetweenStartDateAndEndDate(
            roomId: String, date: LocalDate
    ): Flowable<List<ReservationEntity>>

    fun findReservationsByRoomAndFromDateToDate(
            roomId: Long, startDate: LocalDate, endDate: LocalDate
    ): Flowable<List<ReservationEntity>>?

    fun findReservationsFromDateToDate(
            startDate: LocalDate, endDate: LocalDate
    ): Flowable<List<ReservationEntity>>

    fun update(reservation: ReservationEntity)

    fun delete(reservation: ReservationEntity)

    fun deleteAll()
}