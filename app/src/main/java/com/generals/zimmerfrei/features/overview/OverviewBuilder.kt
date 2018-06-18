package com.generals.zimmerfrei.features.overview

import android.arch.lifecycle.ViewModel
import com.generals.zimmerfrei.features.overview.service.CalendarService
import com.generals.zimmerfrei.features.overview.service.CalendarServiceImpl
import com.generals.zimmerfrei.features.overview.usecase.OverviewUseCase
import com.generals.zimmerfrei.features.overview.usecase.OverviewUseCaseImpl
import com.generals.zimmerfrei.features.overview.viewmodel.OverviewViewModel
import com.generals.zimmerfrei.inject.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.Provides
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