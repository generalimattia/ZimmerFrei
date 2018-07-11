package com.generals.zimmerfrei.repository.entities

import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.Index
import android.arch.persistence.room.PrimaryKey
import org.threeten.bp.OffsetDateTime

@Entity(
    tableName = "reservations", foreignKeys = [ForeignKey(
        entity = Room::class, parentColumns = arrayOf("name"), childColumns = arrayOf("roomId")
    )], indices = [(Index("roomId"))]
)
data class Reservation(
    var name: String = "",
    var startDate: OffsetDateTime = OffsetDateTime.now(),
    var endDate: OffsetDateTime = OffsetDateTime.now(),
    var startDay: Int = 0,
    var startMonth: Int = 0,
    var startYear: Int = 0,
    var endDay: Int = 0,
    var endMonth: Int = 0,
    var endYear: Int = 0,
    var personCount: Int = 3,
    var roomId: String = ""
) {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}