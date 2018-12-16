package com.generals.zimmerfrei.overview.service.reservation

import android.arch.core.executor.testing.InstantTaskExecutorRule
import com.generals.zimmerfrei.common.extension.offsetDateTimeFromLocalDate
import com.generals.zimmerfrei.model.Room
import com.generals.zimmerfrei.model.RoomDay
import com.generals.zimmerfrei.repository.dao.ReservationDAO
import com.generals.zimmerfrei.repository.dao.RoomDAO
import com.generals.zimmerfrei.repository.entities.ReservationEntity
import com.generals.zimmerfrei.repository.entities.RoomEntity
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.eq
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Flowable
import io.reactivex.Observable
import org.junit.Rule
import org.junit.Test
import org.threeten.bp.LocalDate

class ReservationServiceImplTest {

    private val reservationDao: ReservationDAO = mock()
    private val roomDAO: RoomDAO = mock()
    private val sut: ReservationService = ReservationServiceImpl(reservationDao, roomDAO)

    @get:Rule
    val executor = InstantTaskExecutorRule()

    @Test
    fun shouldFetchReservationsByRoom() {

        whenever(roomDAO.getAllRooms()).thenReturn(
                Flowable.just(
                        listOf(
                                RoomEntity(
                                        id = 0,
                                        name = "A",
                                        personsCount = 4,
                                        isDouble = true,
                                        isSingle = false,
                                        isHandicap = false,
                                        hasBalcony = true
                                ), RoomEntity(
                                id = 1,
                                name = "B",
                                personsCount = 4,
                                isDouble = true,
                                isSingle = false,
                                isHandicap = false,
                                hasBalcony = true
                        ), RoomEntity(
                                id = 2,
                                name = "C",
                                personsCount = 4,
                                isDouble = true,
                                isSingle = false,
                                isHandicap = false,
                                hasBalcony = true
                        )
                        )
                )
        )

        val now = LocalDate.now()
        whenever(
                reservationDao.findReservationsByRoomAndFromDateToDate(
                        eq(0), any(), any()
                )
        ).thenReturn(
                Flowable.just(
                        listOf(
                                ReservationEntity(
                                        id = 0,
                                        name = "White",
                                        startDate = offsetDateTimeFromLocalDate(now.minusDays(5)),
                                        endDate = offsetDateTimeFromLocalDate(now.plusDays(1))
                                )
                        )
                )
        )

        whenever(
                reservationDao.findReservationsByRoomAndFromDateToDate(
                        eq(1), any(), any()
                )
        ).thenReturn(
                Flowable.just(
                        listOf(
                                ReservationEntity(
                                        id = 0,
                                        name = "Black",
                                        startDate = offsetDateTimeFromLocalDate(now.minusDays(7)),
                                        endDate = offsetDateTimeFromLocalDate(now.plusDays(5))
                                )
                        )
                )
        )

        val observable: Observable<Pair<Room, List<RoomDay>>> =
                sut.fetchReservationsFromDayToDayGroupedByRoom(
                        startPeriod = now.withDayOfMonth(1), endPeriod = now.withDayOfMonth(30)
                )

        observable.test().assertValueCount(3)
                .assertValueAt(0) { result: Pair<Room, List<RoomDay>> ->
                    var startReservation = now.dayOfMonth - 5
                    startReservation = Math.max(1, startReservation)

                    var endReservation = now.dayOfMonth + 1
                    endReservation = Math.min(endReservation, 30)

                    result.first.id == 0L &&
                            result.second.isNotEmpty() &&
                            result.second[0] is RoomDay.EmptyDay &&
                            result.second[startReservation - 1] is RoomDay.StartingReservationDay &&
                            result.second[endReservation - 1] is RoomDay.EndingReservationDay
                }.assertValueAt(1) { result: Pair<Room, List<RoomDay>> ->
                    var startReservation = now.dayOfMonth - 7
                    startReservation = Math.max(1, startReservation)

                    var endReservation = now.dayOfMonth + 5
                    endReservation = Math.min(endReservation, 30)

                    result.first.id == 1L &&
                            result.second.isNotEmpty() &&
                            result.second[0] is RoomDay.EmptyDay &&
                            result.second[startReservation - 1] is RoomDay.StartingReservationDay &&
                            result.second[endReservation - 1] is RoomDay.EndingReservationDay
                }

                .assertValueAt(2) { result: Pair<Room, List<RoomDay>> ->
                    result.first.id == 2L && result.second.isNotEmpty()
                }
    }
}