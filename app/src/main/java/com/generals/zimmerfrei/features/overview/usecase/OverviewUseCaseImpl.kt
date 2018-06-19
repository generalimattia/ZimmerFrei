package com.generals.zimmerfrei.features.overview.usecase

import com.generals.overview.model.Day
import com.generals.overview.service.CalendarService
import javax.inject.Inject

class OverviewUseCaseImpl @Inject constructor(private val calendarService: CalendarService) : OverviewUseCase {

    override fun loadCalendar(onComplete: (List<Day>) -> Unit) {
        onComplete(calendarService.loadCalendar())
    }
}