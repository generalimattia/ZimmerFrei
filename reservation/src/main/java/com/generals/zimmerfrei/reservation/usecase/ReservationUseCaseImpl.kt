package com.generals.zimmerfrei.reservation.usecase

import arrow.core.Option
import com.generals.network.api.ReservationsAPI
import com.generals.network.api.RoomsAPI
import com.generals.network.model.*
import com.generals.zimmerfrei.listeners.ActionResult
import com.generals.zimmerfrei.model.Customer
import com.generals.zimmerfrei.model.Reservation
import com.generals.zimmerfrei.model.Room
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

interface ReservationUseCase {
    suspend fun get(url: String): Option<Reservation>
    suspend fun save(reservation: Reservation): ActionResult<Reservation>
    suspend fun getAllRooms(): List<Room>
    suspend fun getRoomByListPosition(position: Int): Option<Room>
    suspend fun update(reservation: Reservation): ActionResult<Reservation>
    suspend fun delete(reservation: Reservation): ActionResult<Reservation>
}

class ReservationUseCaseImpl @Inject constructor(
        private val reservationsAPI: ReservationsAPI,
        private val roomsAPI: RoomsAPI
) : ReservationUseCase {

    override suspend fun get(url: String): Option<Reservation> = withContext(Dispatchers.IO) {
        reservationsAPI.get(url).fold(
                ifSuccess = { result: Option<ReservationInbound> ->
                    result.map {
                        Reservation(
                                reservation = it,
                                room = it.room?.let { Room(it) } ?: Room()
                        )
                    }
                },
                ifFailure = { Option.empty() },
                ifError = {
                    Timber.e(it)
                    Option.empty()
                }
        )
    }

    override suspend fun save(reservation: Reservation): ActionResult<Reservation> = withContext(Dispatchers.IO) {
        reservationsAPI.create(reservation.toInbound())
                .fold(
                        ifSuccess = { result: Option<ReservationInbound> ->
                            result.fold(ifSome = {
                                ActionResult.Success(message = "Prenotazione creata!", data = Reservation(it, it.room?.let { Room(it) }
                                        ?: Room()))
                            },
                                    ifEmpty = { ActionResult.Error("Errore") }
                            )
                        },
                        ifFailure = { ActionResult.Error("Errore") },
                        ifError = { ActionResult.Error("Errore") }
                )
    }

    override suspend fun getAllRooms(): List<Room> = withContext(Dispatchers.IO) {
        roomsAPI.fetchAll().fold(
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

    override suspend fun getRoomByListPosition(position: Int): Option<Room> = withContext(Dispatchers.IO) {
        roomsAPI.fetchAll().fold(
                ifSuccess = { result: Option<Inbound<RoomListInbound>> ->
                    result.fold(
                            ifSome = { inbound: Inbound<RoomListInbound> ->
                                val room: Room? = inbound.embedded.rooms.getOrNull(position)?.let(::Room)
                                Option.fromNullable(room)
                            },
                            ifEmpty = { Option.empty() }
                    )
                },
                ifFailure = { Option.empty() },
                ifError = { Option.empty() }
        )
    }

    override suspend fun update(reservation: Reservation): ActionResult<Reservation> = withContext(Dispatchers.IO) {
        reservationsAPI.update(reservation.id.toInt(), reservation.toInbound())
                .fold(
                        ifSuccess = { result: Option<ReservationInbound> ->
                            result.fold(ifSome = {
                                ActionResult.Success(message = "Prenotazione aggiornata!", data = Reservation(it, it.room?.let { Room(it) }
                                        ?: Room()))
                            },
                                    ifEmpty = { ActionResult.Error("Errore") }
                            )
                        },
                        ifFailure = { ActionResult.Error("Errore") },
                        ifError = { ActionResult.Error("Errore") }
                )
    }

    override suspend fun delete(reservation: Reservation): ActionResult<Reservation> = withContext(Dispatchers.IO) {
        reservationsAPI.delete(reservation.id.toInt())
                .fold(
                        ifSuccess = {
                            it.fold(
                                    ifSome = {
                                        ActionResult.Success(message = "Prenotazione cancellata!", data = null)
                                    },
                                    ifEmpty = { ActionResult.Error("Errore") }
                            )
                        },
                        ifFailure = { ActionResult.Error("Errore") },
                        ifError = { ActionResult.Error("Errore") }
                )
    }
}

fun Reservation.toInbound(): ReservationInbound = ReservationInbound(
        id = id.toInt(),
        name = name,
        persons = persons,
        startDate = startDate,
        endDate = endDate,
        adults = adults,
        children = children,
        babies = babies,
        notes = notes,
        color = color,
        room = room.toInbound(),
        customer = customer?.toInbound()
)

fun Room.toInbound(): RoomInbound = RoomInbound(
        id = id.toInt(),
        name = name,
        maxPersons = personsCount
)

fun Customer.toInbound(): CustomerInbound = CustomerInbound(
        id = id,
        firstName = firstName,
        lastName = lastName,
        socialId = socialId,
        mobile = mobile,
        email = email,
        address = address,
        city = city,
        province = province,
        state = state,
        zip = zip,
        gender = gender,
        birthDate = birthDate,
        birthPlace = birthPlace
)