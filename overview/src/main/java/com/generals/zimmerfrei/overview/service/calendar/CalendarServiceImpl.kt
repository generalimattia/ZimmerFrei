package com.generals.zimmerfrei.overview.service.calendar

import com.generals.zimmerfrei.model.Day
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import org.threeten.bp.*
import org.threeten.bp.format.DateTimeFormatter
import javax.inject.Inject

class CalendarServiceImpl @Inject constructor() : CalendarService {

    private var untilMonthAndYear: Pair<Int, Int> = 0 to 0

    companion object {
        private const val DAY_NAME_PATTERN = "E"
        private const val MONTH_NAME_PATTERN = "MMMM YYYY"
    }

    override fun monthDays(): Int {
        val currentDate: LocalDate = LocalDate.now()
        val currentMoth = currentDate.month
        return currentMoth.length(currentDate.isLeapYear)
    }

    override fun loadDays(date: LocalDate): Observable<Pair<List<Day>, String>> {
        untilMonthAndYear = date.monthValue to date.year

        return buildDaysObservable(date)
    }

    override fun loadMoreDays(): Observable<Pair<List<Day>, String>> {
        val previousDate: LocalDate = LocalDate.of(
            untilMonthAndYear.second,
            untilMonthAndYear.first,
            1
        )

        val newDate: LocalDate = previousDate.plusMonths(1)

        untilMonthAndYear = newDate.monthValue to newDate.year

        return buildDaysObservable(newDate)
    }

    private fun buildDaysObservable(date: LocalDate): Observable<Pair<List<Day>, String>> =
        Observable.create { emitter: ObservableEmitter<Pair<List<Day>, String>> ->

            val month: Month = date.month
            val monthLength: Int = month.length(date.isLeapYear)

            val days: List<Day> = (date.dayOfMonth..monthLength).toList()
                .map { index: Int ->

                    val accumulator: LocalDate = date.withDayOfMonth(index)

                    Day(
                        title = "$index ${DateTimeFormatter.ofPattern(
                            DAY_NAME_PATTERN
                        ).format(accumulator).toUpperCase()}",
                        date = OffsetDateTime.of(
                            accumulator,
                            LocalTime.NOON,
                            ZoneOffset.UTC
                        ),
                        monthDays = monthLength
                    )
                }
            emitter.onNext(
                days to DateTimeFormatter.ofPattern(
                    MONTH_NAME_PATTERN
                ).format(date)
            )
            emitter.onComplete()
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
                            date = OffsetDateTime.of(
                                acc,
                                LocalTime.NOON,
                                ZoneOffset.UTC
                            ),
                            monthDays = monthDays
                        )
                    )
                    acc.plusDays(1)
                }
            emitter.onComplete()
        }
    }
}