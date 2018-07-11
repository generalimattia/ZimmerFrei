package com.generals.zimmerfrei.repository.dao

import android.arch.lifecycle.LiveData
import com.generals.zimmerfrei.repository.entities.Reservation

interface ReservationDAO {

    fun insert(reservation: Reservation)

    fun getAllReservations(): LiveData<List<Reservation>>

    fun findReservationsByRoom(roomId: String): LiveData<List<Reservation>>

    fun deleteAll()
}