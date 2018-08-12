package com.generals.zimmerfrei.room.inject

import android.arch.lifecycle.ViewModel
import com.generals.zimmerfrei.inject.ViewModelKey
import com.generals.zimmerfrei.room.detail.viewmodel.RoomDetailViewModel
import com.generals.zimmerfrei.room.list.viewmodel.RoomListViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class RoomBuilder {

    @Binds
    @IntoMap
    @ViewModelKey(RoomDetailViewModel::class)
    abstract fun bindRoomDetailViewModel(viewModel: RoomDetailViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(RoomListViewModel::class)
    abstract fun bindRoomListViewModel(viewModel: RoomListViewModel): ViewModel
}