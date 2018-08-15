package com.generals.zimmerfrei.overview.service.reservation

import com.generals.zimmerfrei.common.extension.offsetDateTimeFromLocalDate
import com.generals.zimmerfrei.model.Day
import com.generals.zimmerfrei.model.Reservation
import com.generals.zimmerfrei.model.Room
import com.generals.zimmerfrei.model.RoomDay
import com.generals.zimmerfrei.repository.dao.ReservationDAO
import com.generals.zimmerfrei.repository.dao.RoomDAO
import com.generals.zimmerfrei.repository.entities.ReservationEntity
import com.generals.zimmerfrei.repository.entities.RoomEntity
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.schedulers.Schedulers
import org.threeten.bp.LocalDate
import org.threeten.bp.OffsetDateTime
import org.threeten.bp.chrono.ChronoLocalDate
import java.util.concurrent.CountDownLatch
import javax.inject.Inject

class ReservationServiceImpl @Inject constructor(
    private val reservationDao: ReservationDAO, private val roomDAO: RoomDAO
) : ReservationService {

    override fun fetchReservationsByRoomAndDay(room: Room, day: Day): Flowable<List<Reservation>> {

        val reservations: Flowable<List<ReservationEntity>> =
            reservationDao.findReservationsByRoomAndDateBetweenStartDateAndEndDate(
                roomId = room.name,
                date = day.date
            )

        return reservations.map { entities: List<ReservationEntity> ->
            entities.map {
                Reservation(
                    it,
                    room.toEntity()
                )
            }
        }
    }

    override fun fetchReservationsFromDayToDayGroupedByRoom(
        startPeriod: LocalDate, endPeriod: LocalDate
    ): Observable<Pair<Room, List<RoomDay>>> =
        Observable.create<Pair<Room, List<RoomDay>>> { emitter: ObservableEmitter<Pair<Room, List<RoomDay>>> ->

            roomDAO.getAllRooms()
                .observeOn(Schedulers.computation())
                .subscribeOn(Schedulers.computation())
                .subscribe { roomEntities: List<RoomEntity> ->

                    val countDownLatch = CountDownLatch(roomEntities.size)

                    roomEntities.forEach { nullableRoomEntity: RoomEntity? ->

                        nullableRoomEntity?.let { roomEntity: RoomEntity ->

                            val allReservations: Flowable<List<ReservationEntity>> =
                                reservationDao.findReservationsByRoomAndFromDateToDate(
                                    roomEntity.id,
                                    offsetDateTimeFromLocalDate(startPeriod),
                                    offsetDateTimeFromLocalDate(endPeriod)
                                )

                            allReservations.subscribeOn(Schedulers.computation())
                                .observeOn(Schedulers.computation())
                                .subscribe { nullableReservationEntities: List<ReservationEntity>? ->

                                    nullableReservationEntities?.let { reservationEntities: List<ReservationEntity> ->

                                        val roomDays: List<RoomDay> =
                                            (startPeriod.dayOfMonth..endPeriod.dayOfMonth).toList()
                                                .map { day: Int ->
                                                    val currentDay: LocalDate = LocalDate.of(
                                                        startPeriod.year,
                                                        startPeriod.monthValue,
                                                        day
                                                    )
                                                    val roomDay: RoomDay =
                                                        reservationEntities.firstOrNull { reservationEntity: ReservationEntity ->
                                                            val startDate: ChronoLocalDate =
                                                                ChronoLocalDate.from(reservationEntity.startDate)
                                                            val endDate: ChronoLocalDate =
                                                                ChronoLocalDate.from(reservationEntity.endDate)
                                                            (currentDay.isAfter(startDate) && currentDay.isBefore(endDate)) || currentDay.isEqual(startDate) || currentDay.isEqual(endDate)
                                                        }?.let {
                                                            val startDate =
                                                                ChronoLocalDate.from(it.startDate)
                                                            val endDate =
                                                                ChronoLocalDate.from(it.endDate)

                                                            when {
                                                                currentDay.isEqual(startDate) -> RoomDay.StartingReservationDay(
                                                                    Day(date = OffsetDateTime.from(currentDay)),
                                                                    Reservation(
                                                                        it,
                                                                        roomEntity
                                                                    )
                                                                )
                                                                currentDay.isEqual(endDate) -> RoomDay.EndingReservationDay(
                                                                    Day(date = OffsetDateTime.from(currentDay)),
                                                                    Reservation(
                                                                        it,
                                                                        roomEntity
                                                                    )
                                                                )
                                                                else -> RoomDay.ReservedDay(
                                                                    Day(date = OffsetDateTime.from(currentDay)),
                                                                    Reservation(
                                                                        it,
                                                                        roomEntity
                                                                    )
                                                                )
                                                            }
                                                        }
                                                                ?: RoomDay.EmptyDay(Day(date = OffsetDateTime.from(currentDay)))
                                                    roomDay
                                                }
                                        emitter.onNext(Room(roomEntity) to roomDays)
                                    } ?: let {
                                        emitter.onNext(Room(roomEntity) to emptyList())
                                    }
                                    countDownLatch.countDown()
                                }
                        }
                    }

                    countDownLatch.await()
                    emitter.onComplete()
                }

        }
}