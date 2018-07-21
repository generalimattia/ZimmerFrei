package com.generals.zimmerfrei.overview.usecase

import com.generals.zimmerfrei.overview.model.Day
import com.generals.zimmerfrei.overview.model.DayWithReservations
import com.generals.zimmerfrei.overview.model.Reservation
import com.generals.zimmerfrei.overview.service.calendar.CalendarService
import com.generals.zimmerfrei.overview.service.reservation.ReservationService
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class OverviewUseCaseImpl @Inject constructor(
    private val calendarService: CalendarService, private val reservationService: ReservationService
) : OverviewUseCase {

    override fun loadCalendar(): Observable<DayWithReservations> =
        Observable.create { emitter: ObservableEmitter<DayWithReservations> ->

            var count = 0

            calendarService.loadCalendar()
                .subscribeOn(Schedulers.computation())
                .observeOn(Schedulers.computation())
                .subscribe { day: Day? ->

                    day?.let {

                        reservationService.fetchReservationsByDay(day)
                            .subscribeOn(Schedulers.computation())
                            .observeOn(Schedulers.computation())
                            .subscribe { reservations: List<Reservation>? ->

                                reservations?.let {
                                    emitter.onNext(
                                        DayWithReservations(day, it)
                                    )

                                    count += 1
                                    if (count == day.monthDays) {
                                        emitter.onComplete()
                                    }
                                }
                            }
                    }
                }
        }.sorted()
}