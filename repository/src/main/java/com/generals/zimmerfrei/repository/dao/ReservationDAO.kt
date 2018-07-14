package com.generals.zimmerfrei.repository.dao

import android.arch.lifecycle.LiveData
import com.generals.zimmerfrei.repository.entities.ReservationEntity
import org.threeten.bp.OffsetDateTime

interface ReservationDAO {

    fun insert(reservation: ReservationEntity)

    fun getAllReservations(): LiveData<List<ReservationEntity>>

    fun findReservationsByRoom(roomId: String): LiveData<List<ReservationEntity>>

    fun findReservationsByDate(date: OffsetDateTime): LiveData<List<ReservationEntity>>

    fun findReservationsByRoomAndDateBetweenStartDateAndEndDate(
        roomId: String, date: OffsetDateTime
    ): LiveData<List<ReservationEntity>>

    fun deleteAll()
}