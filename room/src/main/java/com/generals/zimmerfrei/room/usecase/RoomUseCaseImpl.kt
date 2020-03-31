package com.generals.zimmerfrei.room.usecase

import arrow.core.Option
import com.generals.network.api.RoomsAPI
import com.generals.network.model.Inbound
import com.generals.network.model.RoomInbound
import com.generals.network.model.RoomListInbound
import com.generals.zimmerfrei.model.Room
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

interface RoomUseCase {
    suspend fun save(room: Room)
    suspend fun update(room: Room)
    suspend fun delete(room: Room)
    suspend fun getAllRooms(): List<Room>
}

class RoomUseCaseImpl @Inject constructor(
        private val api: RoomsAPI
) : RoomUseCase {

    override suspend fun save(room: Room) = withContext(Dispatchers.IO) {
        api.create(room.toInbound()).fold(
                ifSuccess = {},
                ifFailure = {},
                ifError = { Timber.e(it) }
        )
    }

    override suspend fun update(room: Room) = withContext(Dispatchers.IO) {
        api.update(room.id.toInt(), room.toInbound()).fold(
                ifSuccess = {},
                ifFailure = {},
                ifError = { Timber.e(it) }
        )
    }

    override suspend fun delete(room: Room) = withContext(Dispatchers.IO) {
        api.delete(room.id.toInt()).fold(
                ifSuccess = {},
                ifFailure = {},
                ifError = { Timber.e(it) }
        )
    }

    override suspend fun getAllRooms(): List<Room> = withContext(Dispatchers.IO) {
        api.fetchAll().fold(
                ifSuccess = { result: Option<Inbound<RoomListInbound>> ->
                    result.fold(
                            ifSome = { inbound: Inbound<RoomListInbound> -> inbound.embedded.rooms.map(::Room) },
                            ifEmpty = { emptyList() }
                    )
                },
                ifFailure = { emptyList() },
                ifError = {
                    Timber.e(it)
                    emptyList()
                }
        )
    }
}

fun Room.toInbound(): RoomInbound = RoomInbound(
        id = id.toInt(),
        name = name,
        roomCount = 0
)