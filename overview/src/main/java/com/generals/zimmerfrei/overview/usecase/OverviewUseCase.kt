package com.generals.zimmerfrei.overview.usecase

import com.generals.zimmerfrei.model.Day
import com.generals.zimmerfrei.model.Room
import com.generals.zimmerfrei.model.RoomDay
import io.reactivex.Maybe
import io.reactivex.Observable
import org.threeten.bp.LocalDate

interface OverviewUseCase {

    fun loadRooms(): Maybe<List<Room>>

    fun loadDays(date: LocalDate): Observable<Pair<List<Day>, String>>

    fun loadReservations(
        startPeriod: LocalDate, endPeriod: LocalDate
    ): Observable<Pair<Room, List<RoomDay>>>
}