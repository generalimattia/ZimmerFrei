package com.generals.zimmerfrei.repository.dao

import com.generals.zimmerfrei.repository.dao.room.RoomRoomDAO
import com.generals.zimmerfrei.repository.entities.RoomEntity
import io.reactivex.Flowable
import javax.inject.Inject

class RoomDAOImpl @Inject constructor(
    private val dao: RoomRoomDAO
) : RoomDAO {

    override fun insert(room: RoomEntity) = dao.insert(room)

    override fun insert(rooms: List<RoomEntity>) = dao.insert(rooms)

    override fun findById(id: Long): Flowable<RoomEntity> = dao.findById(id)

    override fun getAllRooms(): Flowable<List<RoomEntity>> = dao.getAllRooms()
}