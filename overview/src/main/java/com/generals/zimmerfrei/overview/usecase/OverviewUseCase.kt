package com.generals.zimmerfrei.overview.usecase

import com.generals.zimmerfrei.model.DayWithReservations
import io.reactivex.Observable

interface OverviewUseCase {

    fun loadCalendar(): Observable<DayWithReservations>
}