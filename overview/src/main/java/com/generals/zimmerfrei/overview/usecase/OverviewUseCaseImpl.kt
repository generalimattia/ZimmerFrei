package com.generals.zimmerfrei.overview.usecase

import com.generals.zimmerfrei.model.Day
import com.generals.zimmerfrei.model.DayWithReservations
import com.generals.zimmerfrei.model.Reservation
import com.generals.zimmerfrei.overview.service.calendar.CalendarService
import com.generals.zimmerfrei.overview.service.reservation.ReservationService
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.CountDownLatch
import javax.inject.Inject

class OverviewUseCaseImpl @Inject constructor(
    private val calendarService: CalendarService, private val reservationService: ReservationService
) : OverviewUseCase {

    override fun loadCalendar(): Observable<DayWithReservations> =
        Observable.create { emitter: ObservableEmitter<DayWithReservations> ->

            val countDownLatch = CountDownLatch(calendarService.monthDays())

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
                                }

                                countDownLatch.countDown()

                                if (countDownLatch.count == 0.toLong()) {
                                    emitter.onComplete()
                                }
                            }
                    }
                }

        }.sorted()
}