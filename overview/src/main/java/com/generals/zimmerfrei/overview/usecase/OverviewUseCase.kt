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
import org.threeten.bp.Month
import org.threeten.bp.chrono.ChronoLocalDate
import org.threeten.bp.format.DateTimeFormatter
import javax.inject.Inject

interface OverviewUseCase {
    suspend fun loadDays(date: LocalDate): Pair<List<Day>, String>
    suspend fun loadRooms(): List<Room>
    suspend fun loadReservationsByPeriod(date: LocalDate): List<Pair<Room, List<RoomDay>>>
}

class OverviewUseCaseImpl @Inject constructor(
        private val roomsAPI: RoomsAPI,
        private val reservationsAPI: ReservationsAPI
) : OverviewUseCase {

    override suspend fun loadDays(date: LocalDate): Pair<List<Day>, String> = withContext(Dispatchers.Default) {
        val month: Month = date.month
        val monthLength: Int = month.length(date.isLeapYear)

        val days: List<Day> = (1..monthLength).toList()
                .map { index: Int ->

                    val accumulator: LocalDate = date.withDayOfMonth(index)

                    Day(
                            title = "${DateTimeFormatter.ofPattern(DAY_NAME_PATTERN)
                                    .format(accumulator).toUpperCase()}",
                            date = accumulator,
                            monthDays = monthLength
                    )
                }
        days to DateTimeFormatter.ofPattern(MONTH_NAME_PATTERN).format(date)
    }

    override suspend fun loadRooms(): List<Room> = withContext(Dispatchers.IO) {
        roomsAPI.fetchAll().fold(
                ifSuccess = { inbound: Option<Inbound<RoomListInbound>> ->
                    inbound.fold(
                            ifEmpty = { emptyList<Room>() },
                            ifSome = { it.embedded.rooms.map(::Room) }
                    )
                },
                ifFailure = { emptyList() },
                ifError = { emptyList() }
        ).sortedBy { it.name }
    }

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

    companion object {
        private const val DAY_NAME_PATTERN = "d EEEEE"
        private const val MONTH_NAME_PATTERN = "MMMM YYYY"
    }
}