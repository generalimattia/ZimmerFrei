package com.generals.overview.usecase

import com.generals.overview.model.Day

interface OverviewUseCase {

    fun loadCalendar(onComplete: (List<Day>) -> Unit)
}