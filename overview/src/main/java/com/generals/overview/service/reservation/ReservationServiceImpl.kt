package com.generals.overview.service.reservation

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Transformations
import com.generals.overview.model.Day
import com.generals.overview.model.Reservation
import com.generals.overview.model.Room
import com.generals.zimmerfrei.repository.dao.ReservationDAO
import com.generals.zimmerfrei.repository.entities.ReservationEntity
import javax.inject.Inject

class ReservationServiceImpl @Inject constructor(
    private val dao: ReservationDAO
) : ReservationService {

    override fun fetchReservationsByDay(day: Day): LiveData<List<Reservation>> {
        val reservations: LiveData<List<ReservationEntity>> = dao.findReservationsByDate(day.date)

        return Transformations.map(reservations) { input: List<ReservationEntity>? ->
            input?.let { it.map { Reservation(it) } }
        }
    }

    override fun fetchReservationsByRoomAndDay(room: Room, day: Day): LiveData<List<Reservation>> {

        val reservations: LiveData<List<ReservationEntity>> =
            dao.findReservationsByRoomAndDateBetweenStartDateAndEndDate(
                roomId = room.name, date = day.date
            )

        return Transformations.map(reservations) { input: List<ReservationEntity>? ->
            input?.let { it.map { Reservation(it) } }
        }
    }
}