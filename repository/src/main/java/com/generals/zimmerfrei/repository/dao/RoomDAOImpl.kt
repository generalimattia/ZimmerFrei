package com.generals.zimmerfrei.repository.dao

import android.arch.lifecycle.LiveData
import com.generals.zimmerfrei.repository.dao.room.RoomRoomDAO
import com.generals.zimmerfrei.repository.entities.Room
import javax.inject.Inject

class RoomDAOImpl @Inject constructor(
    private val dao: RoomRoomDAO
): RoomDAO {

    override fun insert(rooms: List<Room>) {
        dao.insert(rooms)
    }

    override fun getAllReservations(): LiveData<List<Room>> = dao.getAllRooms()
}