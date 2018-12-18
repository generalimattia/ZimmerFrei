package com.generals.roomrepository

import com.generals.zimmerfrei.model.Room
import io.reactivex.Maybe

interface RoomRepository {

    fun getAllRooms(): Maybe<List<Room>>

    fun save(room: Room): Maybe<Unit>

    fun update(room: Room): Maybe<Unit>

    fun delete(room: Room): Maybe<Unit>
}