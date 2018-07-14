package com.generals.overview.inject

import com.generals.overview.service.calendar.CalendarService
import com.generals.overview.service.calendar.CalendarServiceImpl
import com.generals.overview.service.reservation.ReservationService
import com.generals.overview.service.reservation.ReservationServiceImpl
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