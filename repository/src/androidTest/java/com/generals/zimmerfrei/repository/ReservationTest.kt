package com.generals.zimmerfrei.repository

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Room
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import com.generals.zimmerfrei.repository.dao.room.RoomReservationDAO
import com.generals.zimmerfrei.repository.dao.room.RoomRoomDAO
import com.generals.zimmerfrei.repository.database.ReservationDatabase
import com.generals.zimmerfrei.repository.entities.Reservation
import junit.framework.Assert.assertEquals
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
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

        val reservations: LiveData<List<Reservation>> = reservationDAO.getAllReservations()

        assertEquals(3, reservations.getValueImmediately().size)
    }

    @Test
    fun shouldFetchAllRooms() {
        populateDatabase()

        val rooms: LiveData<List<com.generals.zimmerfrei.repository.entities.Room>> = roomDAO.getAllRooms()

        assertEquals(3, rooms.getValueImmediately().size)
    }

    @Test
    fun shouldFetchReservationsByRooms() {
        populateDatabase()

        val reservationsByFirstRoom: LiveData<List<Reservation>> = reservationDAO.findReservationsByRoom("1")
        assertEquals(2, reservationsByFirstRoom.getValueImmediately().size)

        val reservationsBySecondRoom: LiveData<List<Reservation>> = reservationDAO.findReservationsByRoom("2")
        assertEquals(1, reservationsBySecondRoom.getValueImmediately().size)

        val reservationsByThirdRoom: LiveData<List<Reservation>> = reservationDAO.findReservationsByRoom("3")
        assertEquals(0, reservationsByThirdRoom.getValueImmediately().size)
    }

    private fun populateDatabase() {
        roomDAO.insert(
            listOf(
                com.generals.zimmerfrei.repository.entities.Room("1"),
                com.generals.zimmerfrei.repository.entities.Room("2"),
                com.generals.zimmerfrei.repository.entities.Room("3")
            )
        )

        reservationDAO.insert(
            Reservation(
                name = "name",
                startDay = 10,
                startMonth = 7,
                startYear = 2018,
                endDay = 20,
                endMonth = 7,
                endYear = 2018,
                roomId = "1"
            )
        )

        reservationDAO.insert(
            Reservation(
                name = "name1",
                startDay = 20,
                startMonth = 7,
                startYear = 2018,
                endDay = 30,
                endMonth = 7,
                endYear = 2018,
                roomId = "1"
            )
        )

        reservationDAO.insert(
            Reservation(
                name = "name2",
                startDay = 20,
                startMonth = 7,
                startYear = 2018,
                endDay = 30,
                endMonth = 7,
                endYear = 2018,
                roomId = "2"
            )
        )
    }
}