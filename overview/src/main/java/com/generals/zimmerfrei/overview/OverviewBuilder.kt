package com.generals.zimmerfrei.overview

import android.arch.lifecycle.ViewModel
import com.generals.zimmerfrei.inject.ViewModelKey
import com.generals.zimmerfrei.overview.viewmodel.OverviewViewModel
import com.generals.zimmerfrei.overview.service.calendar.CalendarService
import com.generals.zimmerfrei.overview.service.calendar.CalendarServiceImpl
import com.generals.zimmerfrei.overview.usecase.OverviewUseCase
import com.generals.zimmerfrei.overview.usecase.OverviewUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class OverviewBuilder {

    @Binds
    @IntoMap
    @ViewModelKey(OverviewViewModel::class)
    abstract fun bindOverviewViewModel(viewModel: OverviewViewModel): ViewModel

    @Binds
    abstract fun bindOverviewUseCase(useCase: OverviewUseCaseImpl): OverviewUseCase

    @Binds
    abstract fun bindCalendarService(service: CalendarServiceImpl): CalendarService
}