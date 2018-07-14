package com.generals.overview.service.reservation

import android.arch.lifecycle.LiveData
import com.generals.overview.model.Day
import com.generals.overview.model.Reservation
import com.generals.overview.model.Room

interface ReservationService {

    fun fetchReservationsByDay(day: Day): LiveData<List<Reservation>>

    fun fetchReservationsByRoomAndDay(room: Room, day: Day): LiveData<List<Reservation>>
}