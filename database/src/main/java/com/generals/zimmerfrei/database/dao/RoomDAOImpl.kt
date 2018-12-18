package com.generals.zimmerfrei.database.dao

import com.generals.zimmerfrei.database.dao.room.RoomRoomDAO
import com.generals.zimmerfrei.database.entities.RoomEntity
import io.reactivex.Maybe
import javax.inject.Inject

class RoomDAOImpl @Inject constructor(
        private val dao: RoomRoomDAO
) : RoomDAO {

    override fun insert(room: RoomEntity) = dao.insert(room)

    override fun insert(rooms: List<RoomEntity>) = dao.insert(rooms)

    override fun update(room: RoomEntity) = dao.update(room)

    override fun delete(room: RoomEntity) = dao.delete(room)

    override fun findById(id: Long): Maybe<RoomEntity> = dao.findById(id)

    override fun getAllRooms(): Maybe<List<RoomEntity>> = dao.getAllRooms()
}