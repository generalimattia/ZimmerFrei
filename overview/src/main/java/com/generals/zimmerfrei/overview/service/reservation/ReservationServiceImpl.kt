package com.generals.zimmerfrei.overview.service.reservation

import com.generals.zimmerfrei.common.extension.offsetDateTimeFromLocalDate
import com.generals.zimmerfrei.model.Day
import com.generals.zimmerfrei.model.Reservation
import com.generals.zimmerfrei.model.Room
import com.generals.zimmerfrei.model.RoomDay
import com.generals.zimmerfrei.repository.dao.ReservationDAO
import com.generals.zimmerfrei.repository.entities.ReservationEntity
import io.reactivex.Flowable
import org.threeten.bp.LocalDate
import org.threeten.bp.OffsetDateTime
import org.threeten.bp.chrono.ChronoLocalDate
import javax.inject.Inject

class ReservationServiceImpl @Inject constructor(
    private val dao: ReservationDAO
) : ReservationService {

    override fun fetchReservationsByDay(day: Day): Flowable<List<Reservation>> {
        val reservations: Flowable<List<ReservationEntity>> = dao.findReservationsByDate(day.date)

        return reservations.map { entities: List<ReservationEntity> ->
            entities.map {
                Reservation(it)
            }
        }
    }

    override fun fetchReservationsByRoomAndDay(room: Room, day: Day): Flowable<List<Reservation>> {

        val reservations: Flowable<List<ReservationEntity>> =
            dao.findReservationsByRoomAndDateBetweenStartDateAndEndDate(
                roomId = room.name,
                date = day.date
            )

        return reservations.map { entities: List<ReservationEntity> ->
            entities.map {
                Reservation(it)
            }
        }
    }

    override fun fetchReservationsFromDayToDayGroupedByRoom(
        startPeriod: LocalDate, endPeriod: LocalDate
    ): Flowable<Map<Room, List<RoomDay>>> {

        val reservations: Flowable<List<ReservationEntity>> = dao.findReservationsFromDateToDate(
            offsetDateTimeFromLocalDate(startPeriod),
            offsetDateTimeFromLocalDate(endPeriod)
        )

        return reservations.map { entities: List<ReservationEntity> ->
            entities.groupBy { entity: ReservationEntity ->
                entity.roomId
            }
                .map { entry: Map.Entry<String, List<ReservationEntity>> ->

                    val roomDays: List<RoomDay> =
                        (startPeriod.dayOfMonth..endPeriod.dayOfMonth).toList()
                            .map { day: Int ->
                                val currentDay: LocalDate = LocalDate.of(
                                    startPeriod.year,
                                    startPeriod.monthValue,
                                    day
                                )
                                val roomDay: RoomDay = entry.value.firstOrNull {
                                    val startDate: ChronoLocalDate = ChronoLocalDate.from(it.startDate)
                                    val endDate: ChronoLocalDate = ChronoLocalDate.from(it.endDate)
                                    (currentDay.isAfter(startDate) && currentDay.isBefore(endDate)) || currentDay.isEqual(startDate) || currentDay.isEqual(endDate)
                                }?.let {
                                    val startDate = ChronoLocalDate.from(it.startDate)
                                    val endDate = ChronoLocalDate.from(it.endDate)

                                    when {
                                        currentDay.isEqual(startDate) -> RoomDay.StartingReservationDay(
                                            Day(date = OffsetDateTime.from(currentDay)),
                                            Reservation(it)
                                        )
                                        currentDay.isEqual(endDate) -> RoomDay.EndingReservationDay(
                                            Day(date = OffsetDateTime.from(currentDay)),
                                            Reservation(it)
                                        )
                                        else -> RoomDay.BookedDay(
                                            Day(date = OffsetDateTime.from(currentDay)),
                                            Reservation(it)
                                        )
                                    }
                                } ?: RoomDay.EmptyDay(Day(date = OffsetDateTime.from(currentDay)))
                                roomDay
                            }
                    Room(entry.key) to roomDays
                }
                .toMap()
        }
    }

    /*override fun fetchReservationsStartingFromDayGroupedByRoom(day: Day): Flowable<Map<Room, List<RoomDay>>> {
        val reservations: Flowable<List<ReservationEntity>> =
            dao.findReservationsFromDateToDate(day.date)

        return reservations.map { entities: List<ReservationEntity> ->
            entities.groupBy { reservationEntity: ReservationEntity ->
                reservationEntity.roomId
            }
                .map { entry: Map.Entry<String, List<ReservationEntity>> ->
                    if(entry.value.isEmpty()) {
                        Room(entry.key) to entry.value.map { if(it.) }
                    }
                }
        }
    }*/
}