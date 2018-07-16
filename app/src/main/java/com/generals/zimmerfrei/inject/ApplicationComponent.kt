package com.generals.zimmerfrei.inject

import com.generals.zimmerfrei.ZimmerFreiApplication
import com.generals.zimmerfrei.common.inject.ViewModelBuilder
import com.generals.zimmerfrei.overview.OverviewBuilder
import com.generals.zimmerfrei.overview.inject.OverviewModule
import com.generals.zimmerfrei.repository.inject.DatabaseModule
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [AndroidSupportInjectionModule::class, ApplicationModule::class, ViewModelBuilder::class, OverviewBuilder::class, DatabaseModule::class, OverviewModule::class]
)
interface ApplicationComponent : AndroidInjector<ZimmerFreiApplication> {
    @Component.Builder
    abstract class Builder : AndroidInjector.Builder<ZimmerFreiApplication>()
}