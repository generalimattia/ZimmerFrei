package com.generals.zimmerfrei.overview.service.room

import com.generals.zimmerfrei.model.Room
import com.generals.zimmerfrei.repository.dao.RoomDAO
import com.generals.zimmerfrei.repository.entities.RoomEntity
import io.reactivex.Flowable
import javax.inject.Inject

class RoomServiceImpl @Inject constructor(
    private val dao: RoomDAO
): RoomService {

    override fun fetchRooms(): Flowable<List<Room>> {
        val rooms: Flowable<List<RoomEntity>> = dao.getAllRooms()

        return rooms.map { entities: List<RoomEntity> ->
            entities.map { Room(it) }
        }
    }
}