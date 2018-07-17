package com.generals.zimmerfrei.inject

import android.arch.persistence.room.Room
import android.content.Context
import com.generals.zimmerfrei.repository.dao.ReservationDAO
import com.generals.zimmerfrei.repository.dao.ReservationDAOImpl
import com.generals.zimmerfrei.repository.dao.RoomDAO
import com.generals.zimmerfrei.repository.dao.RoomDAOImpl
import com.generals.zimmerfrei.repository.dao.room.RoomReservationDAO
import com.generals.zimmerfrei.repository.dao.room.RoomRoomDAO
import com.generals.zimmerfrei.repository.database.ReservationDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(context: Context): ReservationDatabase = Room.databaseBuilder(
        context.applicationContext, ReservationDatabase::class.java, "reservation_database"
    ).build()

    @Provides
    fun provideRoomRoomDAO(database: ReservationDatabase): RoomRoomDAO = database.roomDAO()

    @Provides
    fun provideRoomReservationDAO(database: ReservationDatabase): RoomReservationDAO = database.reservationDAO()

    @Provides
    @Singleton
    fun provideRoomDAO(dao: RoomRoomDAO) : RoomDAO = RoomDAOImpl(dao)

    @Provides
    @Singleton
    fun provideReservationDAO(dao: RoomReservationDAO) : ReservationDAO = ReservationDAOImpl(dao)

}