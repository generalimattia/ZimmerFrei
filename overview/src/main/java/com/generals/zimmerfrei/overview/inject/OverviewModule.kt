package com.generals.zimmerfrei.overview.inject

import com.generals.zimmerfrei.overview.service.calendar.CalendarService
import com.generals.zimmerfrei.overview.service.calendar.CalendarServiceImpl
import com.generals.zimmerfrei.overview.service.reservation.ReservationService
import com.generals.zimmerfrei.overview.service.reservation.ReservationServiceImpl
import com.generals.zimmerfrei.overview.service.room.RoomService
import com.generals.zimmerfrei.overview.service.room.RoomServiceImpl
import com.generals.zimmerfrei.overview.usecase.OverviewUseCase
import com.generals.zimmerfrei.overview.usecase.OverviewUseCaseImpl
import com.generals.zimmerfrei.overview.view.OverviewActivity
import com.generals.zimmerfrei.overview.view.OverviewFragment
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import javax.inject.Singleton

@Module
abstract class OverviewModule {

    @ContributesAndroidInjector
    abstract fun contributeOverviewActivityInjector(): OverviewActivity

    @ContributesAndroidInjector
    abstract fun contributeOverviewFragmentInjector(): OverviewFragment

    @Binds
    @Singleton
    abstract fun bindCalendarService(service: CalendarServiceImpl): CalendarService

    @Binds
    abstract fun bindReservationService(service: ReservationServiceImpl): ReservationService

    @Binds
    abstract fun bindRoomService(service: RoomServiceImpl): RoomService

    @Binds
    abstract fun bindOverviewUseCase(useCase: OverviewUseCaseImpl): OverviewUseCase
}