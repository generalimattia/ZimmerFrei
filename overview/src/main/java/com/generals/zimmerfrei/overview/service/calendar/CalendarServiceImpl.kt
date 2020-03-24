package com.generals.zimmerfrei.overview.service.calendar

import com.generals.zimmerfrei.model.Day
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import org.threeten.bp.LocalDate
import org.threeten.bp.Month
import org.threeten.bp.format.DateTimeFormatter
import javax.inject.Inject

class CalendarServiceImpl @Inject constructor() : CalendarService {

    private var untilMonthAndYear: Pair<Int, Int> = 0 to 0

    companion object {
        private const val DAY_NAME_PATTERN = "E"
        private const val MONTH_NAME_PATTERN = "MMMM YYYY"
    }

    override fun loadDays(date: LocalDate): Observable<Pair<List<Day>, String>> {
        untilMonthAndYear = date.monthValue to date.year

        return buildDaysObservable(date)
    }

    private fun buildDaysObservable(date: LocalDate): Observable<Pair<List<Day>, String>> =
            Observable.create { emitter: ObservableEmitter<Pair<List<Day>, String>> ->

                val month: Month = date.month
                val monthLength: Int = month.length(date.isLeapYear)

                val days: List<Day> = (1..monthLength).toList()
                        .map { index: Int ->

                            val accumulator: LocalDate = date.withDayOfMonth(index)

                            Day(
                                    title = "$index ${DateTimeFormatter.ofPattern(
                                            DAY_NAME_PATTERN
                                    ).format(accumulator).toUpperCase()}",
                                    date = accumulator,
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
}