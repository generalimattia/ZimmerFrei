package com.generals.zimmerfrei.repository.dao

import android.arch.lifecycle.LiveData
import com.generals.zimmerfrei.repository.dao.room.RoomRoomDAO
import com.generals.zimmerfrei.repository.entities.RoomEntity
import javax.inject.Inject

class RoomDAOImpl @Inject constructor(
    private val dao: RoomRoomDAO
): RoomDAO {

    override fun insert(rooms: List<RoomEntity>) {
        dao.insert(rooms)
    }

    override fun getAllReservations(): LiveData<List<RoomEntity>> = dao.getAllRooms()
}