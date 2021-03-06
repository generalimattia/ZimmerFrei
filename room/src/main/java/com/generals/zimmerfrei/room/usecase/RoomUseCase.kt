package com.generals.zimmerfrei.room.usecase

import com.generals.zimmerfrei.model.Room
import io.reactivex.Maybe

interface RoomUseCase {

    fun save(room: Room): Maybe<Unit>

    fun update(room: Room): Maybe<Unit>

    fun delete(room: Room): Maybe<Unit>

    fun getAllRooms(): Maybe<List<Room>>
}