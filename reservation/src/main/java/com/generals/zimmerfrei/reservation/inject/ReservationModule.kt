package com.generals.zimmerfrei.reservation.inject

import com.generals.zimmerfrei.reservation.usecase.ReservationUseCase
import com.generals.zimmerfrei.reservation.usecase.ReservationUseCaseImpl
import com.generals.zimmerfrei.reservation.view.ReservationActivity
import com.generals.zimmerfrei.reservation.view.ReservationFragment
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ReservationModule {

    @ContributesAndroidInjector
    abstract fun contributeOverviewActivityInjector(): ReservationActivity

    @ContributesAndroidInjector
    abstract fun contributeOverviewFragmentInjector(): ReservationFragment

    @Binds
    abstract fun bindReservationUseCase(useCase: ReservationUseCaseImpl): ReservationUseCase
}