package com.generals.zimmerfrei.repository.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import com.generals.zimmerfrei.repository.entities.Reservation

@Dao
interface RoomReservationDAO {

    @Insert
    fun insert(reservation: Reservation)

    @Query("SELECT * FROM reservations")
    fun getAllReservations(): List<Reservation>

    @Query("DELETE FROM reservations")
    fun deleteAll()

}