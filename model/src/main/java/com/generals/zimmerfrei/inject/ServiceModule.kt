package com.generals.zimmerfrei.inject

import com.generals.zimmerfrei.service.RoomFetcherService
import com.generals.zimmerfrei.service.RoomFetcherServiceImpl
import dagger.Binds
import dagger.Module

@Module
abstract class ServiceModule {

    @Binds
    abstract fun bindRoomService(service: RoomFetcherServiceImpl): RoomFetcherService
}