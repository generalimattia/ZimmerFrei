package com.generals.zimmerfrei.inject

import android.content.Context
import androidx.room.Room
import com.generals.zimmerfrei.database.dao.ReservationDAO
import com.generals.zimmerfrei.database.dao.ReservationDAOImpl
import com.generals.zimmerfrei.database.dao.RoomDAO
import com.generals.zimmerfrei.database.dao.RoomDAOImpl
import com.generals.zimmerfrei.database.dao.room.RoomReservationDAO
import com.generals.zimmerfrei.database.dao.room.RoomRoomDAO
import com.generals.zimmerfrei.database.database.ReservationDatabase
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