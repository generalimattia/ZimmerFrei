package com.generals.zimmerfrei.room.usecase

import arrow.core.Option
import com.generals.network.api.RoomsAPI
import com.generals.network.model.Inbound
import com.generals.network.model.RoomInbound
import com.generals.network.model.RoomListInbound
import com.generals.zimmerfrei.listeners.ActionResult
import com.generals.zimmerfrei.model.Room
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

interface RoomUseCase {
    suspend fun save(room: Room): ActionResult<Room>
    suspend fun update(room: Room): ActionResult<Room>
    suspend fun delete(room: Room): ActionResult<Unit>
    suspend fun getAllRooms(): List<Room>
}

class RoomUseCaseImpl @Inject constructor(
        private val api: RoomsAPI
) : RoomUseCase {

    override suspend fun save(room: Room): ActionResult<Room> = withContext(Dispatchers.IO) {
        api.create(room.toInbound()).fold(
                ifSuccess = { result: Option<RoomInbound> ->
                    result.fold(
                            ifSome = { room: RoomInbound -> ActionResult.Success(data = Room(room), message = "Camera creata!") },
                            ifEmpty = { ActionResult.Error<Room>(message = "Errore!") }
                    )
                },
                ifFailure = { ActionResult.Error<Room>(message = "Errore!") },
                ifError = {
                    Timber.e(it)
                    ActionResult.Error<Room>(message = "Errore!")
                }
        )
    }

    override suspend fun update(room: Room): ActionResult<Room> = withContext(Dispatchers.IO) {
        api.update(room.id.toInt(), room.toInbound()).fold(
                ifSuccess = { result: Option<RoomInbound> ->
                    result.fold(
                            ifSome = { room: RoomInbound -> ActionResult.Success(data = Room(room), message = "Camera creata!") },
                            ifEmpty = { ActionResult.Error<Room>(message = "Errore!") }
                    )
                },
                ifFailure = { ActionResult.Error<Room>(message = "Errore!") },
                ifError = {
                    Timber.e(it)
                    ActionResult.Error<Room>(message = "Errore!")
                }
        )
    }

    override suspend fun delete(room: Room): ActionResult<Unit> = withContext(Dispatchers.IO) {
        api.delete(room.id.toInt()).fold(
                ifSuccess = { result: Option<Unit> ->
                    result.fold(
                            ifSome = { ActionResult.Success(message = "Camera cancellata!", data = null) },
                            ifEmpty = { ActionResult.Error<Unit>(message = "Errore!") }
                    )
                },
                ifFailure = { ActionResult.Error<Unit>(message = "Errore!") },
                ifError = {
                    Timber.e(it)
                    ActionResult.Error<Unit>(message = "Errore!")
                }
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
        maxPersons = personsCount
)