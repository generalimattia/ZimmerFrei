package com.generals.zimmerfrei.features.overview.usecase

import com.generals.zimmerfrei.features.overview.service.CalendarService
import com.generals.zimmerfrei.model.Day
import javax.inject.Inject

class OverviewUseCaseImpl @Inject constructor(
        private val calendarService: CalendarService): OverviewUseCase {

    override fun loadCalendar(onComplete: (List<Day>) -> Unit) {
        onComplete(calendarService.loadCalendar())
    }
}