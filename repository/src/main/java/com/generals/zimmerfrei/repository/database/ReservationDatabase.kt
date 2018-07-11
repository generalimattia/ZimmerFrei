package com.generals.zimmerfrei.repository.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import android.content.Context
import com.generals.zimmerfrei.repository.dao.room.RoomReservationDAO
import com.generals.zimmerfrei.repository.dao.room.RoomRoomDAO
import com.generals.zimmerfrei.repository.entities.Reservation
import com.generals.zimmerfrei.repository.entities.ReservationsTypeConverters


@Database(
    entities = [Reservation::class, com.generals.zimmerfrei.repository.entities.Room::class],
    version = 1
)
@TypeConverters(ReservationsTypeConverters::class)
internal abstract class ReservationDatabase : RoomDatabase() {

    abstract fun reservationDAO(): RoomReservationDAO

    abstract fun roomDAO(): RoomRoomDAO

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