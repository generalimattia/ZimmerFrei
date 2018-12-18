package com.generals.zimmerfrei.database.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import com.generals.zimmerfrei.database.dao.room.RoomReservationDAO
import com.generals.zimmerfrei.database.dao.room.RoomRoomDAO
import com.generals.zimmerfrei.database.entities.ReservationEntity
import com.generals.zimmerfrei.database.entities.ReservationsTypeConverters


@Database(
    entities = [ReservationEntity::class, com.generals.zimmerfrei.database.entities.RoomEntity::class],
    version = 1
)
@TypeConverters(ReservationsTypeConverters::class)
abstract class ReservationDatabase : RoomDatabase() {

    abstract fun reservationDAO(): RoomReservationDAO

    abstract fun roomDAO(): RoomRoomDAO

}