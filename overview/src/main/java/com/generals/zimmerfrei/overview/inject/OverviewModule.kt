package com.generals.zimmerfrei.overview.inject

import com.generals.zimmerfrei.overview.service.calendar.CalendarService
import com.generals.zimmerfrei.overview.service.calendar.CalendarServiceImpl
import com.generals.zimmerfrei.overview.service.reservation.ReservationService
import com.generals.zimmerfrei.overview.service.reservation.ReservationServiceImpl
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
abstract class OverviewModule {

    @Binds
    @Singleton
    abstract fun bindCalendarService(service: CalendarServiceImpl): CalendarService

    @Binds
    abstract fun bindReservationService(service: ReservationServiceImpl): ReservationService
}