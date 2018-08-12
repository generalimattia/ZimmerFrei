package com.generals.zimmerfrei.overview.usecase

import com.generals.zimmerfrei.model.*
import com.generals.zimmerfrei.overview.service.calendar.CalendarService
import com.generals.zimmerfrei.overview.service.reservation.ReservationService
import com.generals.zimmerfrei.service.RoomFetcherService
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.schedulers.Schedulers
import org.threeten.bp.LocalDate
import java.util.concurrent.CountDownLatch
import javax.inject.Inject

class OverviewUseCaseImpl @Inject constructor(
    private val calendarService: CalendarService,
    private val reservationService: ReservationService,
    private val roomService: RoomFetcherService
) : OverviewUseCase {

    override fun loadDays(date: LocalDate): Observable<Pair<List<Day>, String>> =
        calendarService.loadDays(date)

    override fun loadRooms(): Observable<List<Room>> = roomService.fetchRooms().toObservable()

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
                                        DayWithReservations(
                                            day,
                                            it
                                        )
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

    override fun loadReservationsByRoom(
        startDate: LocalDate,
        endDate: LocalDate
    ): Observable<Map<Room, List<RoomDay>>> =
        reservationService.fetchReservationsFromDayToDayGroupedByRoom(
            startDate,
            endDate
        ).toObservable()
}