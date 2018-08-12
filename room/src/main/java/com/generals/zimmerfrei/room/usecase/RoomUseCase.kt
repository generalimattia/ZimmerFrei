package com.generals.zimmerfrei.room.usecase

import com.generals.zimmerfrei.model.Room
import io.reactivex.Observable

interface RoomUseCase {

    fun save(room: Room)

    fun getAllRooms(): Observable<List<Room>>
}