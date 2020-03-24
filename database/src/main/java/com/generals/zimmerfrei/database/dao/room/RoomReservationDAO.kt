package com.generals.zimmerfrei.database.dao.room

import androidx.room.*
import com.generals.zimmerfrei.database.entities.ReservationEntity
import io.reactivex.Flowable
import org.threeten.bp.LocalDate
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
        date: LocalDate
    ): Flowable<List<ReservationEntity>>

    @Query("SELECT * FROM reservations WHERE roomId LIKE :roomId AND :date BETWEEN startDate AND endDate")
    fun findReservationsByRoomAndDateBetweenStartDateAndEndDate(
        roomId: String, date: LocalDate
    ): Flowable<List<ReservationEntity>>

    @Query("SELECT * FROM reservations WHERE roomId LIKE :roomId AND (startDate BETWEEN :startDate AND :endDate OR endDate BETWEEN :startDate AND :endDate)")
    fun findReservationsByRoomAndFromDateToDate(
        roomId: Long, startDate: LocalDate, endDate: LocalDate
    ): Flowable<List<ReservationEntity>>

    @Query("SELECT * FROM reservations WHERE startDate BETWEEN :startDate AND :endDate OR endDate BETWEEN :startDate AND :endDate")
    fun findReservationsFromDateToDate(
            startDate: LocalDate, endDate: LocalDate
    ): Flowable<List<ReservationEntity>>

    @Update
    fun update(reservation: ReservationEntity)

    @Delete
    fun delete(reservation: ReservationEntity)

    @Query("DELETE FROM reservations")
    fun deleteAll()

}