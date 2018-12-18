package com.generals.zimmerfrei.room.usecase

import com.generals.roomrepository.RoomRepository
import com.generals.zimmerfrei.model.Room
import io.reactivex.Maybe
import javax.inject.Inject

class RoomUseCaseImpl @Inject constructor(
        private val repository: RoomRepository
) : RoomUseCase {

    override fun save(room: Room): Maybe<Unit> = repository.save(room)

    override fun update(room: Room): Maybe<Unit> = repository.update(room)

    override fun delete(room: Room): Maybe<Unit> = repository.delete(room)

    override fun getAllRooms(): Maybe<List<Room>> = repository.getAllRooms()
}