package com.generals.zimmerfrei.overview.service.reservation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.generals.roomrepository.RoomRepository
import com.generals.zimmerfrei.database.dao.ReservationDAO
import com.generals.zimmerfrei.database.entities.ReservationEntity
import com.generals.zimmerfrei.model.Room
import com.generals.zimmerfrei.model.RoomDay
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.eq
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Observable
import org.junit.Rule
import org.junit.Test
import org.threeten.bp.LocalDate

class ReservationServiceImplTest {

    private val reservationDao: ReservationDAO = mock()
    private val roomRepository: RoomRepository = mock()
    private val sut: ReservationService = ReservationServiceImpl(reservationDao, roomRepository)

    @get:Rule
    val executor = InstantTaskExecutorRule()

    @Test
    fun shouldFetchReservationsByRoom() {

        whenever(roomRepository.getAllRooms()).thenReturn(
                Maybe.just(
                        listOf(
                                Room(
                                        id = 0,
                                        name = "A",
                                        personsCount = 4,
                                        isDouble = true,
                                        isSingle = false,
                                        isHandicap = false,
                                        hasBalcony = true
                                ), Room(
                                id = 1,
                                name = "B",
                                personsCount = 4,
                                isDouble = true,
                                isSingle = false,
                                isHandicap = false,
                                hasBalcony = true
                        ), Room(
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
                                        startDate = now.minusDays(5),
                                        endDate = now.plusDays(1)
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
                                        startDate = now.minusDays(7),
                                        endDate = now.plusDays(5)
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
                            result.second[0] is RoomDay.Empty &&
                            result.second[startReservation - 1] is RoomDay.StartingReservation &&
                            result.second[endReservation - 1] is RoomDay.EndingReservation
                }.assertValueAt(1) { result: Pair<Room, List<RoomDay>> ->
                    var startReservation = now.dayOfMonth - 7
                    startReservation = Math.max(1, startReservation)

                    var endReservation = now.dayOfMonth + 5
                    endReservation = Math.min(endReservation, 30)

                    result.first.id == 1L &&
                            result.second.isNotEmpty() &&
                            result.second[0] is RoomDay.Empty &&
                            result.second[startReservation - 1] is RoomDay.StartingReservation &&
                            result.second[endReservation - 1] is RoomDay.EndingReservation
                }

                .assertValueAt(2) { result: Pair<Room, List<RoomDay>> ->
                    result.first.id == 2L && result.second.isNotEmpty()
                }
    }
}