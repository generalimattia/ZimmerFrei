package com.generals.zimmerfrei.reservation.usecase

import arrow.core.Option
import com.generals.network.api.ReservationsAPI
import com.generals.network.api.RoomsAPI
import com.generals.network.model.CustomerInbound
import com.generals.network.model.Inbound
import com.generals.network.model.ReservationInbound
import com.generals.network.model.RoomListInbound
import com.generals.zimmerfrei.model.Reservation
import com.generals.zimmerfrei.model.Room
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.threeten.bp.LocalDate
import timber.log.Timber
import javax.inject.Inject

interface ReservationUseCase {
    suspend fun save(reservation: Reservation)
    suspend fun getAllRooms(): List<Room>
    suspend fun getRoomByListPosition(position: Int): Option<Room>
    suspend fun update(reservation: Reservation)
    suspend fun delete(reservation: Reservation)
}

class ReservationUseCaseImpl @Inject constructor(
        private val reservationsAPI: ReservationsAPI,
        private val roomsAPI: RoomsAPI
) : ReservationUseCase {

    override suspend fun save(reservation: Reservation) = withContext(Dispatchers.IO) {
        reservationsAPI.create(reservation.toInbound())
                .fold(
                        ifSuccess = {},
                        ifFailure = {},
                        ifError = {}
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

    override suspend fun update(reservation: Reservation) = withContext(Dispatchers.IO) {
        reservationsAPI.update(reservation.id.toInt(), reservation.toInbound())
                .fold(
                        ifSuccess = {},
                        ifFailure = {},
                        ifError = {}
                )
    }

    override suspend fun delete(reservation: Reservation) = withContext(Dispatchers.IO) {
        reservationsAPI.delete(reservation.id.toInt())
                .fold(
                        ifSuccess = {},
                        ifFailure = {},
                        ifError = {}
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
        customer = CustomerInbound(
                firstName = name,
                lastName = name,
                socialId = "",
                mobile = mobile,
                email = email,
                address = "",
                birthDate = LocalDate.now()
        )
)