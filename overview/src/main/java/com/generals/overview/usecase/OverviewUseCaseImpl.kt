package com.generals.overview.usecase

import com.generals.overview.model.Day
import com.generals.overview.service.calendar.CalendarService
import com.generals.overview.service.reservation.ReservationService
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class OverviewUseCaseImpl @Inject constructor(
    private val calendarService: CalendarService, private val reservationService: ReservationService
) : OverviewUseCase {

    override fun loadCalendar(onComplete: (List<Day>) -> Unit) {
        calendarService.loadCalendar().subscribeOn(Schedulers.computation())
            .observeOn(Schedulers.computation()).subscribe { day: Day? ->
                day?.let { reservationService.fetchReservationsByDay(day) }
            }
    }
}