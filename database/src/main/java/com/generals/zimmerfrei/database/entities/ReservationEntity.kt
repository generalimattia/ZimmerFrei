package com.generals.zimmerfrei.database.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import org.threeten.bp.LocalDate

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
        var startDate: LocalDate = LocalDate.now(),
        var endDate: LocalDate = LocalDate.now(),
        var adults: Int = 0,
        var children: Int = 0,
        var babies: Int = 0,
        var roomId: Long = 0L,
        var color: String = "",
        var notes: String = "",
        var email: String = "",
        var mobile: String = ""
)