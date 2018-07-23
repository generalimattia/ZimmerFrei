package com.generals.zimmerfrei.reservation.inject

import com.generals.zimmerfrei.reservation.view.ReservationActivity
import com.generals.zimmerfrei.reservation.view.ReservationFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ReservationModule {

    @ContributesAndroidInjector
    abstract fun contributeOverviewActivityInjector(): ReservationActivity

    @ContributesAndroidInjector
    abstract fun contributeOverviewFragmentInjector(): ReservationFragment
}