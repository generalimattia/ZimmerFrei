package com.generals.zimmerfrei.overview.service.calendar

import com.generals.zimmerfrei.overview.model.Day
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import org.threeten.bp.LocalDate
import org.threeten.bp.OffsetDateTime
import org.threeten.bp.format.DateTimeFormatter
import javax.inject.Inject

class CalendarServiceImpl @Inject constructor() : CalendarService {

    override fun loadCalendar(): Observable<Day> {
        val currentDate: LocalDate = LocalDate.now()
        val currentMoth = currentDate.month
        val monthDays = currentMoth.length(currentDate.isLeapYear)

        return Observable.create<Day> { emitter: ObservableEmitter<Day> ->

            listOf(0 until monthDays).fold(currentDate) { acc: LocalDate, _: IntRange ->
                emitter.onNext(
                    Day(
                        title = "${acc.dayOfMonth} ${DateTimeFormatter.ISO_DATE.format(acc).toUpperCase()}",
                        date = OffsetDateTime.from(acc)
                    )
                )
                acc.plusDays(1)
            }
            emitter.onComplete()
        }
    }
}