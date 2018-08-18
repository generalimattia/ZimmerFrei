package com.generals.zimmerfrei.room.usecase

import com.generals.zimmerfrei.model.Room
import io.reactivex.Observable
import io.reactivex.Single

interface RoomUseCase {

    fun save(room: Room): Single<Unit>

    fun update(room: Room): Single<Unit>

    fun delete(room: Room): Single<Unit>

    fun getAllRooms(): Observable<List<Room>>
}