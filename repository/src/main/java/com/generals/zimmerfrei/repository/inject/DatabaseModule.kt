package com.generals.zimmerfrei.repository.inject

import com.generals.zimmerfrei.repository.dao.ReservationDAO
import com.generals.zimmerfrei.repository.dao.ReservationDAOImpl
import com.generals.zimmerfrei.repository.dao.RoomDAO
import com.generals.zimmerfrei.repository.dao.RoomDAOImpl
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
abstract class DatabaseModule {

    @Binds
    @Singleton
    abstract fun bindReservationDAO(dao: ReservationDAOImpl): ReservationDAO

    @Binds
    @Singleton
    abstract fun bindRoomDAO(dao: RoomDAOImpl): RoomDAO

}