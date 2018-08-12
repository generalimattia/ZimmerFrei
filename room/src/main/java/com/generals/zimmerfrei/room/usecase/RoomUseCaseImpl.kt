package com.generals.zimmerfrei.room.usecase

import com.generals.zimmerfrei.model.Room
import com.generals.zimmerfrei.room.service.RoomService
import io.reactivex.Observable
import javax.inject.Inject

class RoomUseCaseImpl @Inject constructor(
    private val service: RoomService
): RoomUseCase {

    override fun save(room: Room) = service.save(room)

    override fun getAllRooms(): Observable<List<Room>> = service.fetchRooms().toObservable()
}