package com.generals.zimmerfrei.repository.dao

import android.arch.lifecycle.LiveData
import com.generals.zimmerfrei.repository.entities.Room

interface RoomDAO {

    fun insert(rooms: List<Room>)

    fun getAllReservations(): LiveData<List<Room>>
}