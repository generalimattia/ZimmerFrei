package com.generals.zimmerfrei.database.entities

import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.Index
import android.arch.persistence.room.PrimaryKey
import org.threeten.bp.OffsetDateTime

@Entity(
    tableName = "reservations",
    foreignKeys = [ForeignKey(
        entity = RoomEntity::class,
        parentColumns = ["id"],
        childColumns = ["roomId"]
    )],
    indices = [(Index("roomId"))]
)
data class ReservationEntity(
    @PrimaryKey(autoGenerate = true) var id: Long = 0,
    var name: String = "",
    var startDate: OffsetDateTime = OffsetDateTime.now(),
    var endDate: OffsetDateTime = OffsetDateTime.now(),
    var adults: Int = 0,
    var children: Int = 0,
    var babies: Int = 0,
    var roomId: Long = 0L,
    var color: String = "",
    var notes: String = "",
    var email: String = "",
    var mobile: String = ""
)