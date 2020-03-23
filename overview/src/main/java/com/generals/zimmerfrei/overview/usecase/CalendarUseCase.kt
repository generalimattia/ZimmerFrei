package com.generals.zimmerfrei.overview.usecase

import arrow.core.Option
import com.generals.network.api.ReservationsAPI
import com.generals.network.api.RoomsAPI
import com.generals.network.model.Inbound
import com.generals.network.model.ReservationInbound
import com.generals.network.model.ReservationListInbound
import com.generals.network.model.RoomListInbound
import com.generals.zimmerfrei.model.Reservation
import com.generals.zimmerfrei.model.Room
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.threeten.bp.LocalDate
import javax.inject.Inject

interface CalendarUseCase {
    suspend fun loadReservationsByPeriod(date: LocalDate): List<Pair<Room, List<Reservation>>>
}

class CalendarUseCaseImpl @Inject constructor(
        private val roomsAPI: RoomsAPI,
        private val reservationsAPI: ReservationsAPI
) : CalendarUseCase {

    override suspend fun loadReservationsByPeriod(date: LocalDate): List<Pair<Room, List<Reservation>>> = withContext(Dispatchers.IO) {
        val allRooms: List<Room> = roomsAPI.fetchAll().fold(
                ifSuccess = { inbound: Option<Inbound<RoomListInbound>> ->
                    inbound.fold(
                            ifEmpty = { emptyList() },
                            ifSome = { it.embedded.rooms.map(::Room) }
                    )
                },
                ifFailure = { emptyList() },
                ifError = { emptyList() }
        )
        allRooms.map { room: Room -> loadReservationsByRoomAndPeriod(room, date) }
    }

    private suspend fun loadReservationsByRoomAndPeriod(room: Room, date: LocalDate): Pair<Room, List<Reservation>> =
            withContext(Dispatchers.IO) {
                val defaultResult: Pair<Room, List<Reservation>> = room to emptyList()

                reservationsAPI.fetchByRoomAndFromDateToDate(
                        roomId = room.id.toInt(),
                        from = date.withDayOfMonth(1),
                        to = date.withDayOfMonth(date.lengthOfMonth())
                ).fold(
                        ifSuccess = { inbound: Option<Inbound<ReservationListInbound>> ->
                            inbound.fold(
                                    ifSome = { reservationsFromNetwork: Inbound<ReservationListInbound> ->
                                        room to reservationsFromNetwork.embedded.reservations.map { reservation: ReservationInbound ->
                                            Reservation(reservation, room)
                                        }
                                    },
                                    ifEmpty = { defaultResult }
                            )
                        },
                        ifFailure = { defaultResult },
                        ifError = { defaultResult }
                )
            }
}