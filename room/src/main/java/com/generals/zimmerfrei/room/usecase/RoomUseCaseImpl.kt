package com.generals.zimmerfrei.room.usecase

import com.generals.zimmerfrei.model.Room
import com.generals.zimmerfrei.room.service.RoomService
import io.reactivex.Observable
import io.reactivex.Single
import javax.inject.Inject

class RoomUseCaseImpl @Inject constructor(
    private val service: RoomService
) : RoomUseCase {

    override fun save(room: Room): Single<Unit> = service.save(room)

    override fun update(room: Room): Single<Unit> = service.update(room)

    override fun delete(room: Room): Single<Unit> = service.delete(room)

    override fun getAllRooms(): Observable<List<Room>> = service.fetchRooms().toObservable()
}