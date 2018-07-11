package com.generals.zimmerfrei.repository.dao.room

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import com.generals.zimmerfrei.repository.entities.Reservation

@Dao
interface RoomReservationDAO {

    @Insert
    fun insert(reservation: Reservation)

    @Query("SELECT * FROM reservations")
    fun getAllReservations(): LiveData<List<Reservation>>

    @Query("SELECT * FROM reservations WHERE roomId LIKE :roomId")
    fun findReservationsByRoom(roomId: String): LiveData<List<Reservation>>

    @Query("DELETE FROM reservations")
    fun deleteAll()

}