package com.generals.zimmerfrei.repository.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import com.generals.zimmerfrei.repository.dao.room.RoomReservationDAO
import com.generals.zimmerfrei.repository.dao.room.RoomRoomDAO
import com.generals.zimmerfrei.repository.entities.ReservationEntity
import com.generals.zimmerfrei.repository.entities.ReservationsTypeConverters


@Database(
    entities = [ReservationEntity::class, com.generals.zimmerfrei.repository.entities.RoomEntity::class],
    version = 1
)
@TypeConverters(ReservationsTypeConverters::class)
abstract class ReservationDatabase : RoomDatabase() {

    abstract fun reservationDAO(): RoomReservationDAO

    abstract fun roomDAO(): RoomRoomDAO

}