package com.generals.zimmerfrei.repository

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.persistence.room.Room
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import com.generals.zimmerfrei.repository.dao.room.RoomReservationDAO
import com.generals.zimmerfrei.repository.dao.room.RoomRoomDAO
import com.generals.zimmerfrei.repository.database.ReservationDatabase
import com.generals.zimmerfrei.repository.entities.ReservationEntity
import com.generals.zimmerfrei.repository.entities.RoomEntity
import io.reactivex.Flowable
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.threeten.bp.OffsetDateTime
import org.threeten.bp.ZoneOffset
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class ReservationTest {

    private lateinit var database: ReservationDatabase
    private lateinit var reservationDAO: RoomReservationDAO
    private lateinit var roomDAO: RoomRoomDAO

    @get:Rule
    val executor = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        val context = InstrumentationRegistry.getTargetContext()
        database = Room.inMemoryDatabaseBuilder(
            context,
            ReservationDatabase::class.java
        )
            .allowMainThreadQueries()
            .build()

        reservationDAO = database.reservationDAO()
        roomDAO = database.roomDAO()
    }

    @After
    @Throws(IOException::class)
    fun closeDatabase() {
        database.close()
    }

    @Test
    fun shouldFetchAllReservations() {
        populateDatabase()

        val reservations: Flowable<List<ReservationEntity>> = reservationDAO.getAllReservations()

        reservations.test()
            .assertValue { entities: List<ReservationEntity> ->
                entities.size == 3
            }
    }

    @Test
    fun shouldFetchAllRooms() {
        populateDatabase()

        val rooms: Flowable<List<RoomEntity>> = roomDAO.getAllRooms()

        rooms.test()
            .assertValue { entities: List<RoomEntity> ->
                entities.size == 3
            }
    }

    @Test
    fun shouldFetchReservationsByRoomsAndDate() {
        populateDatabase()

        val reservationsByFirstRoom: Flowable<List<ReservationEntity>> =
            reservationDAO.findReservationsByRoom("1")

        reservationsByFirstRoom.test()
            .assertValue { entities: List<ReservationEntity> ->
                entities.size == 2
            }

        val reservationsBySecondRoom: Flowable<List<ReservationEntity>> =
            reservationDAO.findReservationsByRoom("2")

        reservationsBySecondRoom.test()
            .assertValue { entities: List<ReservationEntity> ->
                entities.size == 1
            }

        val reservationsByThirdRoom: Flowable<List<ReservationEntity>> =
            reservationDAO.findReservationsByRoom("3")

        reservationsByThirdRoom.test()
            .assertValue { entities: List<ReservationEntity> ->
                entities.isEmpty()
            }
    }

    @Test
    fun shouldFetchReservationsByRooms() {
        populateDatabase()

        val reservationsByFirstRoom: Flowable<List<ReservationEntity>> =
            reservationDAO.findReservationsByRoom("1")

        reservationsByFirstRoom.test()
            .assertValue { entities: List<ReservationEntity> ->
                entities.size == 2
            }

        val reservationsBySecondRoom: Flowable<List<ReservationEntity>> =
            reservationDAO.findReservationsByRoom("2")

        reservationsBySecondRoom.test()
            .assertValue { entities: List<ReservationEntity> ->
                entities.size == 1
            }

        val reservationsByThirdRoom: Flowable<List<ReservationEntity>> =
            reservationDAO.findReservationsByRoom("3")

        reservationsByThirdRoom.test()
            .assertValue { entities: List<ReservationEntity> ->
                entities.isEmpty()
            }
    }

    @Test
    fun shouldFetchReservationsByRoomAndDate() {
        populateDatabase()

        val reservationsByFirstRoom: Flowable<List<ReservationEntity>> =
            reservationDAO.findReservationsByRoomAndDateBetweenStartDateAndEndDate(
                "1",
                OffsetDateTime.of(
                    2018,
                    7,
                    12,
                    0,
                    0,
                    0,
                    0,
                    ZoneOffset.UTC
                )
            )

        reservationsByFirstRoom.test()
            .assertValue { entities: List<ReservationEntity> ->
                entities.size == 1
            }

        val reservationsBySecondRoom: Flowable<List<ReservationEntity>> =
            reservationDAO.findReservationsByRoomAndDateBetweenStartDateAndEndDate(
                "2",
                OffsetDateTime.of(
                    2018,
                    7,
                    12,
                    0,
                    0,
                    0,
                    0,
                    ZoneOffset.UTC
                )
            )

        reservationsBySecondRoom.test()
            .assertValue { entities: List<ReservationEntity> ->
                entities.isEmpty()
            }

    }

    @Test
    fun shouldFetchReservationsByDate() {
        populateDatabase()

        val reservationsByFirstRoom: Flowable<List<ReservationEntity>> =
            reservationDAO.findReservationsByDate(
                OffsetDateTime.of(
                    2018,
                    7,
                    12,
                    0,
                    0,
                    0,
                    0,
                    ZoneOffset.UTC
                )
            )

        reservationsByFirstRoom.test()
            .assertValue { entities: List<ReservationEntity> ->
                entities.size == 1
            }

        val reservationsBySecondRoom: Flowable<List<ReservationEntity>> =
            reservationDAO.findReservationsByDate(
                OffsetDateTime.of(
                    2018,
                    7,
                    21,
                    0,
                    0,
                    0,
                    0,
                    ZoneOffset.UTC
                )
            )

        reservationsBySecondRoom.test()
            .assertValue { entities: List<ReservationEntity> ->
                entities.size == 2
            }
    }

    @Test
    fun shouldFetchReservationsFromDateToDate() {
        populateDatabase()

        val first: Flowable<List<ReservationEntity>> =
            reservationDAO.findReservationsFromDateToDate(
                OffsetDateTime.of(
                    2018,
                    7,
                    1,
                    0,
                    0,
                    0,
                    0,
                    ZoneOffset.UTC
                ),
                OffsetDateTime.of(
                    2018,
                    7,
                    31,
                    0,
                    0,
                    0,
                    0,
                    ZoneOffset.UTC
                )
            )

        first.test()
            .assertValue { entities: List<ReservationEntity> ->
                entities.size == 3
            }

        val second: Flowable<List<ReservationEntity>> =
            reservationDAO.findReservationsFromDateToDate(
                OffsetDateTime.of(
                    2018,
                    8,
                    1,
                    0,
                    0,
                    0,
                    0,
                    ZoneOffset.UTC
                ),
                OffsetDateTime.of(
                    2018,
                    8,
                    31,
                    0,
                    0,
                    0,
                    0,
                    ZoneOffset.UTC
                )
            )

        second.test()
            .assertValue { entities: List<ReservationEntity> ->
                entities.isEmpty()
            }

        val third: Flowable<List<ReservationEntity>> =
            reservationDAO.findReservationsFromDateToDate(
                OffsetDateTime.of(
                    2018,
                    7,
                    10,
                    0,
                    0,
                    0,
                    0,
                    ZoneOffset.UTC
                ),
                OffsetDateTime.of(
                    2018,
                    7,
                    19,
                    0,
                    0,
                    0,
                    0,
                    ZoneOffset.UTC
                )
            )

        third.test()
            .assertValue { entities: List<ReservationEntity> ->
                entities.size == 1
            }
    }

    private fun populateDatabase() {
        roomDAO.insert(
            listOf(
                RoomEntity(
                    name = "1",
                    personsCount = 3,
                    isDouble = true,
                    isSingle = false,
                    isHandicap = true,
                    hasBalcony = true
                ),
                RoomEntity(
                    name = "2",
                    personsCount = 6,
                    isDouble = true,
                    isSingle = false,
                    isHandicap = true,
                    hasBalcony = true
                ),
                RoomEntity(
                    name = "3",
                    personsCount = 5,
                    isDouble = true,
                    isSingle = false,
                    isHandicap = true,
                    hasBalcony = true
                )
            )
        )

        val rooms: Flowable<List<RoomEntity>> = roomDAO.getAllRooms()

        val roomEntities: List<RoomEntity> = rooms.test().assertValue { entities: List<RoomEntity> -> entities.size == 3 }.values()[0]

        reservationDAO.insert(
            ReservationEntity(
                name = "name",
                startDate = OffsetDateTime.of(
                    2018,
                    7,
                    10,
                    0,
                    0,
                    0,
                    0,
                    ZoneOffset.UTC
                ),
                endDate = OffsetDateTime.of(
                    2018,
                    7,
                    20,
                    0,
                    0,
                    0,
                    0,
                    ZoneOffset.UTC
                ),
                roomId = roomEntities[0].id
            )
        )

        reservationDAO.insert(
            ReservationEntity(
                name = "name1",
                startDate = OffsetDateTime.of(
                    2018,
                    7,
                    20,
                    0,
                    0,
                    0,
                    0,
                    ZoneOffset.UTC
                ),
                endDate = OffsetDateTime.of(
                    2018,
                    7,
                    30,
                    0,
                    0,
                    0,
                    0,
                    ZoneOffset.UTC
                ),
                roomId = roomEntities[0].id
            )
        )

        reservationDAO.insert(
            ReservationEntity(
                name = "name2",
                startDate = OffsetDateTime.of(
                    2018,
                    7,
                    20,
                    0,
                    0,
                    0,
                    0,
                    ZoneOffset.UTC
                ),
                endDate = OffsetDateTime.of(
                    2018,
                    7,
                    30,
                    0,
                    0,
                    0,
                    0,
                    ZoneOffset.UTC
                ),
                roomId = roomEntities[1].id
            )
        )
    }
}