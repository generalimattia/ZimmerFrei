package com.generals.zimmerfrei.overview.inject

import com.generals.zimmerfrei.overview.service.reservation.ReservationService
import com.generals.zimmerfrei.overview.service.reservation.ReservationServiceImpl
import com.generals.zimmerfrei.overview.usecase.OverviewUseCase
import com.generals.zimmerfrei.overview.usecase.OverviewUseCaseImpl
import com.generals.zimmerfrei.overview.view.OverviewActivity
import com.generals.zimmerfrei.overview.view.OverviewFragment
import com.generals.zimmerfrei.overview.view.customer.detail.CustomerDetailFragment
import com.generals.zimmerfrei.overview.view.customer.list.CustomerListFragment
import com.generals.zimmerfrei.overview.view.customer.usecase.CustomerUseCase
import com.generals.zimmerfrei.overview.view.customer.usecase.CustomerUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class OverviewModule {

    @ContributesAndroidInjector
    abstract fun contributeOverviewActivityInjector(): OverviewActivity

    @ContributesAndroidInjector
    abstract fun contributeOverviewFragmentInjector(): OverviewFragment

    @ContributesAndroidInjector
    abstract fun contributeCustomerListFragmentInjector(): CustomerListFragment

    @ContributesAndroidInjector
    abstract fun contributeCustomerDetailFragmentInjector(): CustomerDetailFragment

    @Binds
    abstract fun bindReservationService(service: ReservationServiceImpl): ReservationService

    @Binds
    abstract fun bindOverviewUseCase(useCase: OverviewUseCaseImpl): OverviewUseCase

    @Binds
    abstract fun bindCustomerUseCase(useCase: CustomerUseCaseImpl): CustomerUseCase
}