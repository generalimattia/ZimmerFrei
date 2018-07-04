package com.generals.zimmerfrei.repository.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import android.content.Context
import com.generals.zimmerfrei.repository.dao.RoomReservationDAO
import com.generals.zimmerfrei.repository.entities.Reservation
import com.generals.zimmerfrei.repository.entities.ReservationsTypeConverters


@Database(entities = [Reservation::class, Room::class], version = 1)
@TypeConverters(ReservationsTypeConverters::class)
abstract class ReservationDatabase : RoomDatabase() {

    abstract fun reservationDAO(): RoomReservationDAO

    companion object {

        private var INSTANCE: ReservationDatabase? = null


        internal fun getDatabase(context: Context): ReservationDatabase {
            if (INSTANCE == null) {
                synchronized(ReservationDatabase::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder(
                            context.applicationContext,
                            ReservationDatabase::class.java,
                            "reservation_database"
                        ).build()

                    }
                }
            }
            return INSTANCE!!
        }
    }

}