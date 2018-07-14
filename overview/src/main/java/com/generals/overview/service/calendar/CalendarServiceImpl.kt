package com.generals.overview.service.calendar

import com.generals.overview.model.Day
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class CalendarServiceImpl @Inject constructor() : CalendarService {

    private val calendar = Calendar.getInstance()

    companion object {
        private val dayFormat = SimpleDateFormat("EEE", Locale.getDefault())
    }

    override fun loadCalendar(): Observable<Day> {
        val numOfDays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)

        return Observable.create<Day> { emitter: ObservableEmitter<Day> ->

            (0..numOfDays).forEach { index: Int ->
                calendar.set(Calendar.DAY_OF_MONTH, index)
                val num: Int = index + 1
                emitter.onNext(
                    Day(
                        "$num ${dayFormat.format(calendar.time).toUpperCase()}",
                        index,
                        calendar.get(Calendar.YEAR)
                    )
                )
            }
            emitter.onComplete()
        }
    }
}