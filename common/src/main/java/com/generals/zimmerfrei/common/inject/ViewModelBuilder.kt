package com.generals.zimmerfrei.common.inject

import android.arch.lifecycle.ViewModelProvider
import com.generals.zimmerfrei.inject.ZimmerFreiViewModelFactory
import dagger.Binds
import dagger.Module

@Module
abstract class ViewModelBuilder {

    @Binds
    internal abstract fun bindViewModelFactory(factory: ZimmerFreiViewModelFactory): ViewModelProvider.Factory
}