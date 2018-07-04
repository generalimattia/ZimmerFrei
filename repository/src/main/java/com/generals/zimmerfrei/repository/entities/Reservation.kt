package com.generals.zimmerfrei.repository.entities

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import org.threeten.bp.OffsetDateTime

@Entity(tableName = "reservations")
data class Reservation(
    @PrimaryKey(autoGenerate = true) val id: Long,
    val name: String,
    val startDate: OffsetDateTime,
    val endDate: OffsetDateTime,
    val personCount: Int,
    val room: Room
)

@Entity(tableName = "rooms")
data class Room(
    @PrimaryKey(autoGenerate = true) val id: Long,
    val name: String
)