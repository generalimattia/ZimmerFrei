package com.generals.zimmerfrei.overview.service.reservation

import com.generals.zimmerfrei.model.Day
import com.generals.zimmerfrei.model.Reservation
import com.generals.zimmerfrei.model.Room
import com.generals.zimmerfrei.repository.dao.ReservationDAO
import com.generals.zimmerfrei.repository.entities.ReservationEntity
import io.reactivex.Flowable
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
                roomId = room.name, date = day.date
            )

        return reservations.map { entities: List<ReservationEntity> ->
            entities.map {
                Reservation(it)
            }
        }
    }
}