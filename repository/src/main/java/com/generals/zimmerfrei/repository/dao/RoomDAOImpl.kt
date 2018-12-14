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

    override fun update(room: RoomEntity) = dao.update(room)

    override fun delete(room: RoomEntity) = dao.delete(room)

    override fun findById(id: Long): Flowable<RoomEntity> = dao.findById(id)

    override fun findByName(name: String): Flowable<RoomEntity> = dao.findByName(name)

    override fun getAllRooms(): Flowable<List<RoomEntity>> = dao.getAllRooms()
}