package com.generals.zimmerfrei.repository.dao

import android.arch.lifecycle.LiveData
import com.generals.zimmerfrei.repository.entities.RoomEntity

interface RoomDAO {

    fun insert(rooms: List<RoomEntity>)

    fun getAllReservations(): LiveData<List<RoomEntity>>
}