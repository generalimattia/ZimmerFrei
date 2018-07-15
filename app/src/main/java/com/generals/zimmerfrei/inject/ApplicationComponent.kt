package com.generals.zimmerfrei.inject

import com.generals.zimmerfrei.overview.inject.OverviewModule
import com.generals.zimmerfrei.overview.OverviewBuilder
import com.generals.zimmerfrei.overview.view.OverviewFragment
import com.generals.zimmerfrei.repository.inject.DatabaseModule
import com.generals.zimmerfrei.common.inject.ViewModelBuilder
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [ApplicationModule::class, ViewModelBuilder::class, OverviewBuilder::class, DatabaseModule::class, OverviewModule::class]
)
interface ApplicationComponent {

    fun inject(fragment: OverviewFragment)

}