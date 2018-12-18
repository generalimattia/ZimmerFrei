package com.generals.zimmerfrei.database.dao

import com.generals.zimmerfrei.database.entities.RoomEntity
import io.reactivex.Maybe

interface RoomDAO {

    fun insert(room: RoomEntity)

    fun insert(rooms: List<RoomEntity>)

    fun update(room: RoomEntity)

    fun delete(room: RoomEntity)

    fun findById(id: Long): Maybe<RoomEntity>

    fun getAllRooms(): Maybe<List<RoomEntity>>
}