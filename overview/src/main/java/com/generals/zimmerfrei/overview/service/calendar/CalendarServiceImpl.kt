package com.generals.zimmerfrei.overview.service.calendar

import com.generals.zimmerfrei.overview.model.Day
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalTime
import org.threeten.bp.OffsetDateTime
import org.threeten.bp.ZoneOffset
import org.threeten.bp.format.DateTimeFormatter
import javax.inject.Inject

class CalendarServiceImpl @Inject constructor() : CalendarService {

    companion object {
        private const val DAY_NAME_PATTERN = "E"
    }

    override fun monthDays(): Int {
        val currentDate: LocalDate = LocalDate.now()
        val currentMoth = currentDate.month
        return currentMoth.length(currentDate.isLeapYear)
    }

    override fun loadCalendar(): Observable<Day> {
        val currentDate: LocalDate = LocalDate.now()
        val currentMoth = currentDate.month
        val monthDays = currentMoth.length(currentDate.isLeapYear)

        val firstDayOfMonth: LocalDate = currentDate.minusDays(currentDate.dayOfMonth.toLong() - 1)

        return Observable.create { emitter: ObservableEmitter<Day> ->

            (0 until monthDays).toList()
                .fold(firstDayOfMonth) { acc: LocalDate, _: Int ->
                    emitter.onNext(
                        Day(
                            title = "${acc.dayOfMonth} ${DateTimeFormatter.ofPattern(
                                DAY_NAME_PATTERN
                            ).format(acc).toUpperCase()}",
                            date = OffsetDateTime.of(acc, LocalTime.NOON, ZoneOffset.UTC),
                            monthDays = monthDays
                        )
                    )
                    acc.plusDays(1)
                }
            emitter.onComplete()
        }
    }
}