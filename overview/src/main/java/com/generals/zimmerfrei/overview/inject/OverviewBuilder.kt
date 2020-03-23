package com.generals.zimmerfrei.overview.inject

import androidx.lifecycle.ViewModel
import com.generals.zimmerfrei.inject.ViewModelKey
import com.generals.zimmerfrei.overview.viewmodel.OverviewViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class OverviewBuilder {

    @Binds
    @IntoMap
    @ViewModelKey(OverviewViewModel::class)
    abstract fun bindOverviewViewModel(viewModel: OverviewViewModel): ViewModel

}