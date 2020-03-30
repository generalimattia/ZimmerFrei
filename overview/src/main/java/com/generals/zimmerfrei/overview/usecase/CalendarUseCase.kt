package com.generals.zimmerfrei.overview.usecase

import arrow.core.Option
import com.generals.network.api.ReservationsAPI
import com.generals.network.api.RoomsAPI
import com.generals.network.model.Inbound
import com.generals.network.model.ReservationListInbound
import com.generals.network.model.RoomListInbound
import com.generals.zimmerfrei.common.extension.isWeekend
import com.generals.zimmerfrei.model.Day
import com.generals.zimmerfrei.model.Reservation
import com.generals.zimmerfrei.model.Room
import com.generals.zimmerfrei.model.RoomDay
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.threeten.bp.LocalDate
import org.threeten.bp.chrono.ChronoLocalDate
import javax.inject.Inject

interface CalendarUseCase {
    suspend fun loadReservationsByPeriod(date: LocalDate): List<Pair<Room, List<RoomDay>>>
}

class CalendarUseCaseImpl @Inject constructor(
        private val roomsAPI: RoomsAPI,
        private val reservationsAPI: ReservationsAPI
) : CalendarUseCase {

    override suspend fun loadReservationsByPeriod(date: LocalDate): List<Pair<Room, List<RoomDay>>> = withContext(Dispatchers.IO) {
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
                .sortedBy { pair: Pair<Room, List<RoomDay>> -> pair.first.name }
    }

    private suspend fun loadReservationsByRoomAndPeriod(room: Room, date: LocalDate): Pair<Room, List<RoomDay>> =
            withContext(Dispatchers.IO) {
                val defaultResult: Pair<Room, List<RoomDay>> = room to emptyList()

                val from: LocalDate = date.withDayOfMonth(1)
                val to: LocalDate = date.withDayOfMonth(date.lengthOfMonth())
                reservationsAPI.fetchByRoomAndFromDateToDate(
                        roomId = room.id.toInt(),
                        from = from,
                        to = to
                ).fold(
                        ifSuccess = { inbound: Option<Inbound<ReservationListInbound>> ->
                            inbound.fold(
                                    ifSome = { reservationsFromNetwork: Inbound<ReservationListInbound> ->
                                        val reservations: List<Reservation> = reservationsFromNetwork.embedded.reservations.map { Reservation(it, room) }
                                        val roomDays: List<RoomDay> = (from.dayOfMonth..to.dayOfMonth).toList().map { day: Int ->
                                            val currentDay: LocalDate = LocalDate.of(
                                                    from.year,
                                                    to.monthValue,
                                                    day
                                            )
                                            buildRoomDayForDay(
                                                    reservations,
                                                    currentDay,
                                                    room
                                            )
                                        }
                                        room to roomDays
                                    },
                                    ifEmpty = { defaultResult }
                            )
                        },
                        ifFailure = { defaultResult },
                        ifError = { defaultResult }
                )
            }

    private fun buildRoomDayForDay(
            reservations: List<Reservation>,
            currentDay: LocalDate,
            room: Room
    ): RoomDay {

        return reservations.firstOrNull { reservation: Reservation ->
            val startDate: ChronoLocalDate = ChronoLocalDate.from(reservation.startDate)
            val endDate: ChronoLocalDate = ChronoLocalDate.from(reservation.endDate)
            (currentDay.isAfter(startDate) && currentDay.isBefore(endDate)) ||
                    currentDay.isEqual(startDate) || currentDay.isEqual(endDate)
        }?.let { reservation: Reservation ->
            val startDate: ChronoLocalDate = ChronoLocalDate.from(reservation.startDate)
            val endDate: ChronoLocalDate = ChronoLocalDate.from(reservation.endDate)

            when {
                currentDay.isEqual(startDate) -> {
                    RoomDay.StartingReservation(
                            Day(date = currentDay),
                            reservation
                    )
                }
                currentDay.isEqual(endDate) -> RoomDay.EndingReservation(
                        Day(date = currentDay),
                        reservation
                )
                else -> RoomDay.Reserved(
                        Day(date = currentDay),
                        reservation
                )
            }
        } ?: buildEmpty(currentDay, room)
    }


    private fun buildEmpty(date: LocalDate, room: Room): RoomDay =
            if (date.isWeekend()) {
                RoomDay.EmptyWeekend(
                        Day(date = date),
                        room
                )
            } else {
                RoomDay.Empty(
                        Day(date = date),
                        room
                )
            }
}