package com.generals.zimmerfrei.repository.dao

import com.generals.zimmerfrei.repository.entities.RoomEntity
import io.reactivex.Flowable

interface RoomDAO {

    fun insert(room: RoomEntity)

    fun insert(rooms: List<RoomEntity>)

    fun getAllRooms(): Flowable<List<RoomEntity>>
}