package com.generals.zimmerfrei.features.overview.usecase

import com.generals.zimmerfrei.model.Day

interface OverviewUseCase {

    fun loadCalendar(onComplete: (List<Day>) -> Unit)
}