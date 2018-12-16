package com.generals.zimmerfrei.repository.dao

import com.generals.zimmerfrei.repository.entities.ReservationEntity
import io.reactivex.Flowable
import io.reactivex.Single
import org.threeten.bp.OffsetDateTime

interface ReservationDAO {

    fun insert(reservation: ReservationEntity)

    fun getAllReservations(): Flowable<List<ReservationEntity>>

    fun findReservationsByRoom(roomId: String): Flowable<List<ReservationEntity>>

    fun findReservationsByDate(date: OffsetDateTime): Flowable<List<ReservationEntity>>

    fun findReservationsByRoomAndDateBetweenStartDateAndEndDate(
        roomId: String, date: OffsetDateTime
    ): Flowable<List<ReservationEntity>>

    fun findReservationsByRoomAndFromDateToDate(
        roomId: Long, startDate: OffsetDateTime, endDate: OffsetDateTime
    ): Flowable<List<ReservationEntity>>?

    fun findReservationsFromDateToDate(
        startDate: OffsetDateTime, endDate: OffsetDateTime
    ): Flowable<List<ReservationEntity>>

    fun deleteAll()
}