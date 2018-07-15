package com.generals.zimmerfrei.repository.dao.room

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import com.generals.zimmerfrei.repository.entities.ReservationEntity
import io.reactivex.Flowable
import org.threeten.bp.OffsetDateTime

@Dao
interface RoomReservationDAO {

    @Insert
    fun insert(reservation: ReservationEntity)

    @Query("SELECT * FROM reservations")
    fun getAllReservations(): Flowable<List<ReservationEntity>>

    @Query("SELECT * FROM reservations WHERE roomId LIKE :roomId")
    fun findReservationsByRoom(roomId: String): Flowable<List<ReservationEntity>>

    @Query("SELECT * FROM reservations WHERE :date BETWEEN startDate AND endDate")
    fun findReservationsByDate(
        date: OffsetDateTime
    ): Flowable<List<ReservationEntity>>

    @Query("SELECT * FROM reservations WHERE roomId LIKE :roomId AND :date BETWEEN startDate AND endDate")
    fun findReservationsByRoomAndDateBetweenStartDateAndEndDate(
        roomId: String, date: OffsetDateTime
    ): Flowable<List<ReservationEntity>>

    @Query("DELETE FROM reservations")
    fun deleteAll()

}