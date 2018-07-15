package com.generals.zimmerfrei.overview.usecase

import android.arch.lifecycle.LiveData
import com.generals.zimmerfrei.overview.model.Day
import com.generals.zimmerfrei.overview.model.Reservation

interface OverviewUseCase {

    fun loadCalendar(): LiveData<Pair<Day, List<Reservation>>>
}