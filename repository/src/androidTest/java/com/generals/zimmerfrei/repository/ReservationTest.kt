package com.generals.zimmerfrei.repository

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Room
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import com.generals.zimmerfrei.repository.dao.room.RoomReservationDAO
import com.generals.zimmerfrei.repository.dao.room.RoomRoomDAO
import com.generals.zimmerfrei.repository.database.ReservationDatabase
import com.generals.zimmerfrei.repository.entities.ReservationEntity
import com.generals.zimmerfrei.repository.entities.RoomEntity
import junit.framework.Assert.assertEquals
import org.junit.After
import org.junit.Before
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

    @Before
    fun setUp() {
        val context = InstrumentationRegistry.getTargetContext()
        database = Room.inMemoryDatabaseBuilder(context, ReservationDatabase::class.java).build()

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

        val reservations: LiveData<List<ReservationEntity>> = reservationDAO.getAllReservations()

        assertEquals(3, reservations.getValueImmediately().size)
    }

    @Test
    fun shouldFetchAllRooms() {
        populateDatabase()

        val rooms: LiveData<List<com.generals.zimmerfrei.repository.entities.RoomEntity>> =
            roomDAO.getAllRooms()

        assertEquals(3, rooms.getValueImmediately().size)
    }

    @Test
    fun shouldFetchReservationsByRoomsAndDate() {
        populateDatabase()

        val reservationsByFirstRoom: LiveData<List<ReservationEntity>> =
            reservationDAO.findReservationsByRoom("1")
        assertEquals(2, reservationsByFirstRoom.getValueImmediately().size)

        val reservationsBySecondRoom: LiveData<List<ReservationEntity>> =
            reservationDAO.findReservationsByRoom("2")
        assertEquals(1, reservationsBySecondRoom.getValueImmediately().size)

        val reservationsByThirdRoom: LiveData<List<ReservationEntity>> =
            reservationDAO.findReservationsByRoom("3")
        assertEquals(0, reservationsByThirdRoom.getValueImmediately().size)
    }

    @Test
    fun shouldFetchReservationsByRooms() {
        populateDatabase()

        val reservationsByFirstRoom: LiveData<List<ReservationEntity>> =
            reservationDAO.findReservationsByRoom("1")
        assertEquals(2, reservationsByFirstRoom.getValueImmediately().size)

        val reservationsBySecondRoom: LiveData<List<ReservationEntity>> =
            reservationDAO.findReservationsByRoom("2")
        assertEquals(1, reservationsBySecondRoom.getValueImmediately().size)

        val reservationsByThirdRoom: LiveData<List<ReservationEntity>> =
            reservationDAO.findReservationsByRoom("3")
        assertEquals(0, reservationsByThirdRoom.getValueImmediately().size)
    }

    @Test
    fun shouldFetchReservationsByRoomAndDate() {
        populateDatabase()

        val reservationsByFirstRoom: LiveData<List<ReservationEntity>> =
            reservationDAO.findReservationsByRoomAndDateBetweenStartDateAndEndDate(
                "1", OffsetDateTime.of(2018, 7, 12, 0, 0, 0, 0, ZoneOffset.UTC)
            )

        assertEquals(1, reservationsByFirstRoom.getValueImmediately().size)

        val reservationsBySecondRoom: LiveData<List<ReservationEntity>> =
            reservationDAO.findReservationsByRoomAndDateBetweenStartDateAndEndDate(
                "2",
                OffsetDateTime.of(2018, 7, 12, 0, 0, 0, 0, ZoneOffset.UTC)
            )

        assertEquals(0, reservationsBySecondRoom.getValueImmediately().size)
    }

    @Test
    fun shouldFetchReservationsByDate() {
        populateDatabase()

        val reservationsByFirstRoom: LiveData<List<ReservationEntity>> =
            reservationDAO.findReservationsByDate(
                OffsetDateTime.of(2018, 7, 12, 0, 0, 0, 0, ZoneOffset.UTC)
            )

        assertEquals(1, reservationsByFirstRoom.getValueImmediately().size)

        val reservationsBySecondRoom: LiveData<List<ReservationEntity>> =
            reservationDAO.findReservationsByDate(
                OffsetDateTime.of(2018, 7, 21, 0, 0, 0, 0, ZoneOffset.UTC)
            )

        assertEquals(2, reservationsBySecondRoom.getValueImmediately().size)
    }

    private fun populateDatabase() {
        roomDAO.insert(
            listOf(
                RoomEntity("1"), RoomEntity("2"), RoomEntity("3")
            )
        )

        reservationDAO.insert(
            ReservationEntity(
                name = "name",
                startDate = OffsetDateTime.of(2018, 7, 10, 0, 0, 0, 0, ZoneOffset.UTC),
                endDate = OffsetDateTime.of(2018, 7, 20, 0, 0, 0, 0, ZoneOffset.UTC),
                roomId = "1"
            )
        )

        reservationDAO.insert(
            ReservationEntity(
                name = "name1",
                startDate = OffsetDateTime.of(2018, 7, 20, 0, 0, 0, 0, ZoneOffset.UTC),
                endDate = OffsetDateTime.of(2018, 7, 30, 0, 0, 0, 0, ZoneOffset.UTC),
                roomId = "1"
            )
        )

        reservationDAO.insert(
            ReservationEntity(
                name = "name2",
                startDate = OffsetDateTime.of(2018, 7, 20, 0, 0, 0, 0, ZoneOffset.UTC),
                endDate = OffsetDateTime.of(2018, 7, 30, 0, 0, 0, 0, ZoneOffset.UTC),
                roomId = "2"
            )
        )
    }
}