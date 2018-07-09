package com.generals.zimmerfrei.inject

import com.generals.zimmerfrei.features.overview.OverviewBuilder
import com.generals.zimmerfrei.features.overview.view.OverviewFragment
import com.generals.zimmerfrei.repository.inject.DatabaseModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [ApplicationModule::class, ViewModelBuilder::class, OverviewBuilder::class, DatabaseModule::class]
)
interface ApplicationComponent {

    fun inject(fragment: OverviewFragment)

}