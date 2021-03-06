package com.generals.zimmerfrei.inject

import com.generals.roomrepository.inject.RoomRepositoryModule
import com.generals.zimmerfrei.ZimmerFreiApplication
import com.generals.zimmerfrei.common.inject.ViewModelBuilder
import com.generals.zimmerfrei.overview.inject.OverviewBuilder
import com.generals.zimmerfrei.overview.inject.OverviewModule
import com.generals.zimmerfrei.reservation.inject.ReservationBuilder
import com.generals.zimmerfrei.reservation.inject.ReservationModule
import com.generals.zimmerfrei.room.inject.RoomBuilder
import com.generals.zimmerfrei.room.inject.RoomModule
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
        modules = [
            AndroidSupportInjectionModule::class,
            ApplicationModule::class,
            ViewModelBuilder::class,
            DatabaseModule::class,
            OverviewModule::class,
            OverviewBuilder::class,
            ReservationModule::class,
            ReservationBuilder::class,
            RoomModule::class,
            RoomBuilder::class,
            RoomRepositoryModule::class,
            EmittersModule::class
        ]
)
interface ApplicationComponent : AndroidInjector<ZimmerFreiApplication> {
    @Component.Builder
    abstract class Builder : AndroidInjector.Builder<ZimmerFreiApplication>()
}