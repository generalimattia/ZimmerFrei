package com.generals.zimmerfrei.reservation.inject

import android.arch.lifecycle.ViewModel
import com.generals.zimmerfrei.inject.ViewModelKey
import com.generals.zimmerfrei.reservation.viewmodel.ReservationViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ReservationBuilder {

    @Binds
    @IntoMap
    @ViewModelKey(ReservationViewModel::class)
    abstract fun bindReservationViewModel(viewModel: ReservationViewModel): ViewModel
}