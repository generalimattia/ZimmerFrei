package com.generals.zimmerfrei.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "rooms")
data class RoomEntity(
        @PrimaryKey(autoGenerate = true) var id: Long = 0,
        var name: String,
        var personsCount: Int,
        var isDouble: Boolean,
        var isSingle: Boolean,
        var isHandicap: Boolean,
        var hasBalcony: Boolean
)