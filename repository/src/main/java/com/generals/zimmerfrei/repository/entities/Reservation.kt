package com.generals.zimmerfrei.repository.entities

import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.PrimaryKey
import org.threeten.bp.OffsetDateTime

@Entity(
    tableName = "reservations", foreignKeys = [ForeignKey(
        entity = Room::class, parentColumns = arrayOf("id"), childColumns = arrayOf("roomId")
    )]
)
data class Reservation(
    @PrimaryKey(autoGenerate = true) val id: Long, val name: String,
    val startDate: OffsetDateTime,
    val endDate: OffsetDateTime,
    val startDay: Int,
    val startMonth: Int,
    val startYear: Int,
    val endDay: Int,
    val endMonth: Int,
    val endYear: Int,
    val personCount: Int,
    val roomId: Long
)