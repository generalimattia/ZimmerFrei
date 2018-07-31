package com.generals.zimmerfrei.overview.usecase

import com.generals.zimmerfrei.model.DayWithReservations
import com.generals.zimmerfrei.model.Room
import io.reactivex.Observable

interface OverviewUseCase {

    fun loadRooms(): Observable<List<Room>>

    fun loadCalendar(): Observable<DayWithReservations>
}