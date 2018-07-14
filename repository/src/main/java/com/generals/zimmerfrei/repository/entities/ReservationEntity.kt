package com.generals.zimmerfrei.repository.entities

import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.Index
import android.arch.persistence.room.PrimaryKey
import org.threeten.bp.OffsetDateTime

@Entity(
    tableName = "reservations", foreignKeys = [ForeignKey(
        entity = RoomEntity::class, parentColumns = ["name"], childColumns = ["roomId"]
    )], indices = [(Index("roomId"))]
)
data class ReservationEntity(
    var name: String = "",
    var startDate: OffsetDateTime = OffsetDateTime.now(),
    var endDate: OffsetDateTime = OffsetDateTime.now(),
    var personCount: Int = 3,
    var roomId: String = ""
) {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}