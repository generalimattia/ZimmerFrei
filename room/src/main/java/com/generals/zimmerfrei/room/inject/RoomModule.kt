package com.generals.zimmerfrei.room.inject

import com.generals.zimmerfrei.room.detail.view.RoomDetailFragment
import com.generals.zimmerfrei.room.service.RoomService
import com.generals.zimmerfrei.room.service.RoomServiceImpl
import com.generals.zimmerfrei.room.usecase.RoomUseCase
import com.generals.zimmerfrei.room.usecase.RoomUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class RoomModule {

    @ContributesAndroidInjector
    abstract fun contributeRoomDetailFragmentInjector(): RoomDetailFragment

    @Binds
    abstract fun bindRoomUseCase(useCase: RoomUseCaseImpl): RoomUseCase

    @Binds
    abstract fun bindRoomService(service: RoomServiceImpl): RoomService
}