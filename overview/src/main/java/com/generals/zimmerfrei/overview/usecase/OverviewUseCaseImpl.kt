package com.generals.zimmerfrei.overview.usecase

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.LiveDataReactiveStreams
import com.generals.zimmerfrei.overview.model.Day
import com.generals.zimmerfrei.overview.model.Reservation
import com.generals.zimmerfrei.overview.service.calendar.CalendarService
import com.generals.zimmerfrei.overview.service.reservation.ReservationService
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class OverviewUseCaseImpl @Inject constructor(
    private val calendarService: CalendarService, private val reservationService: ReservationService
) : OverviewUseCase {

    override fun loadCalendar(): LiveData<Pair<Day, List<Reservation>>> {

        val flowable: Flowable<Pair<Day, List<Reservation>>> =
            Observable.create<Pair<Day, List<Reservation>>> { emitter: ObservableEmitter<Pair<Day, List<Reservation>>> ->

                calendarService.loadCalendar()
                    .subscribeOn(Schedulers.computation())
                    .observeOn(Schedulers.computation())
                    .subscribe { day: Day? ->

                        day?.let {

                            reservationService.fetchReservationsByDay(day)
                                .subscribeOn(Schedulers.computation())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe { reservations: List<Reservation>? ->

                                    reservations?.let {
                                        emitter.onNext(day to it)
                                    }
                                }
                        }
                    }
            }
                .toFlowable(BackpressureStrategy.BUFFER)


        return LiveDataReactiveStreams.fromPublisher(flowable)
    }
}