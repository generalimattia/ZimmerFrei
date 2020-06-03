package com.generals.zimmerfrei.room.inject

import com.generals.zimmerfrei.listeners.ActionEmitter
import com.generals.zimmerfrei.listeners.ActionEmitterImpl
import com.generals.zimmerfrei.listeners.ActionListener
import com.generals.zimmerfrei.model.Room
import com.generals.zimmerfrei.room.detail.view.RoomDetailFragment
import com.generals.zimmerfrei.room.list.view.RoomListFragment
import com.generals.zimmerfrei.room.usecase.RoomUseCase
import com.generals.zimmerfrei.room.usecase.RoomUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class RoomModule {

    @ContributesAndroidInjector
    abstract fun contributeRoomDetailFragmentInjector(): RoomDetailFragment

    @ContributesAndroidInjector
    abstract fun contributeRoomListFragmentInjector(): RoomListFragment

    @Binds
    abstract fun bindRoomUseCase(useCase: RoomUseCaseImpl): RoomUseCase

    @Binds
    abstract fun bindRoomActionEmitter(impl: ActionEmitterImpl<Room>): ActionEmitter<Room>

    @Binds
    abstract fun bindRoomActionListener(impl: ActionEmitterImpl<Room>): ActionListener<Room>
}