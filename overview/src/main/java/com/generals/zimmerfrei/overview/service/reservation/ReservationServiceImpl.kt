package com.generals.zimmerfrei.overview.service.reservation

import com.generals.roomrepository.RoomRepository
import com.generals.zimmerfrei.common.extension.isWeekend
import com.generals.zimmerfrei.database.dao.ReservationDAO
import com.generals.zimmerfrei.database.entities.ReservationEntity
import com.generals.zimmerfrei.model.Day
import com.generals.zimmerfrei.model.Reservation
import com.generals.zimmerfrei.model.Room
import com.generals.zimmerfrei.model.RoomDay
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import org.threeten.bp.LocalDate
import org.threeten.bp.chrono.ChronoLocalDate
import javax.inject.Inject

class ReservationServiceImpl @Inject constructor(
        private val reservationDao: ReservationDAO,
        private val roomRepository: RoomRepository
) : ReservationService {

    override fun fetchReservationsByRoomAndDay(room: Room, day: Day): Flowable<List<Reservation>> {

        val reservations: Flowable<List<ReservationEntity>> =
                reservationDao.findReservationsByRoomAndDateBetweenStartDateAndEndDate(
                        roomId = room.name, date = day.date
                )

        return reservations.map { entities: List<ReservationEntity> ->
            entities.map {
                Reservation(
                        it, room.toEntity()
                )
            }
        }
    }

    override fun fetchReservationsFromDayToDayGroupedByRoom(
            startPeriod: LocalDate, endPeriod: LocalDate
    ): Observable<Pair<Room, List<RoomDay>>> =
            Observable.create<Pair<Room, List<RoomDay>>> { externalEmitter: ObservableEmitter<Pair<Room, List<RoomDay>>> ->

                roomRepository.getAllRooms().subscribe({ rooms: List<Room> ->

                    val observables: List<Observable<Pair<Room, List<RoomDay>>>> =
                            buildObservablesToFetchReservationsByRoom(
                                    rooms, startPeriod, endPeriod
                            )

                    Observable.merge(observables)
                            .sorted { pair1: Pair<Room, List<RoomDay>>?, pair2: Pair<Room, List<RoomDay>>? ->
                                if (pair1 != null && pair2 != null) {
                                    when {
                                        pair1.first.id > pair2.first.id -> 1
                                        pair1.first.id == pair2.first.id -> 0
                                        else -> -1
                                    }
                                } else {
                                    0
                                }
                            }
                            .subscribe({ pair: Pair<Room, List<RoomDay>>? ->
                                pair?.let {
                                    externalEmitter.onNext(
                                            it
                                    )
                                }
                            }, { t: Throwable? ->
                                t?.let {
                                    externalEmitter.onError(
                                            it
                                    )
                                }
                            }, {
                                externalEmitter.onComplete()
                            })
                }, { t: Throwable ->
                    externalEmitter.onError(t)
                })
            }

    private fun buildObservablesToFetchReservationsByRoom(
            rooms: List<Room>,
            startPeriod: LocalDate, endPeriod: LocalDate
    ): List<Observable<Pair<Room, List<RoomDay>>>> {
        return rooms.map { room: Room ->

            Observable.create<Pair<Room, List<RoomDay>>> { emitter: ObservableEmitter<Pair<Room, List<RoomDay>>> ->

                val allReservations: Flowable<List<ReservationEntity>>? =
                        reservationDao.findReservationsByRoomAndFromDateToDate(
                                room.id,
                                startPeriod,
                                endPeriod
                        )

                allReservations?.subscribe({ reservationEntities: List<ReservationEntity> ->

                    val roomDays: List<RoomDay> =
                            (startPeriod.dayOfMonth..endPeriod.dayOfMonth).toList()
                                    .map { day: Int ->
                                        val currentDay: LocalDate = LocalDate.of(
                                                startPeriod.year,
                                                startPeriod.monthValue,
                                                day
                                        )
                                        val roomDay: RoomDay =
                                                buildRoomDayForDay(
                                                        reservationEntities,
                                                        currentDay,
                                                        room
                                                )
                                        roomDay
                                    }
                    emitter.onNext(room to roomDays)
                    emitter.onComplete()
                }, { t: Throwable? ->
                    t?.let {
                        emitter.onError(t)
                        emitter.onComplete()
                    }
                }) ?: let {
                    emitter.onNext(room to MutableList(endPeriod.dayOfMonth) { day: Int ->
                        val date: LocalDate = LocalDate.of(endPeriod.year, endPeriod.month, day + 1)
                        buildEmpty(date, room)
                    })
                    emitter.onComplete()
                }
            }
        }
    }

    private fun buildRoomDayForDay(
            reservationEntities: List<ReservationEntity>,
            currentDay: LocalDate,
            room: Room
    ): RoomDay {

        val date: LocalDate = currentDay

        return reservationEntities.firstOrNull { reservationEntity: ReservationEntity ->
            val startDate: ChronoLocalDate = ChronoLocalDate.from(reservationEntity.startDate)
            val endDate: ChronoLocalDate = ChronoLocalDate.from(reservationEntity.endDate)
            (currentDay.isAfter(startDate) && currentDay.isBefore(endDate)) ||
                    currentDay.isEqual(startDate) || currentDay.isEqual(endDate)
        }?.let {
            val startDate: ChronoLocalDate = ChronoLocalDate.from(it.startDate)
            val endDate: ChronoLocalDate = ChronoLocalDate.from(it.endDate)

            when {
                currentDay.isEqual(startDate) -> {
                    RoomDay.StartingReservation(
                            Day(date = date),
                            Reservation(it, room)
                    )
                }
                currentDay.isEqual(endDate) -> RoomDay.EndingReservation(
                        Day(date = date),
                        Reservation(it, room)
                )
                else -> RoomDay.Reserved(
                        Day(date = date), Reservation(it, room)
                )
            }
        } ?: buildEmpty(date, room)
    }


    private fun buildEmpty(date: LocalDate, room: Room): RoomDay {
        return if (date.isWeekend()) {
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
}