package com.generals.roomrepository.inject

import com.generals.roomrepository.RoomRepository
import com.generals.roomrepository.RoomRepositoryImpl
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
abstract class RoomRepositoryModule {

    @Binds
    @Singleton
    abstract fun bindRoomRepository(impl: RoomRepositoryImpl): RoomRepository
}