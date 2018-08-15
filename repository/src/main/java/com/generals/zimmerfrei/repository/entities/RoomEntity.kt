package com.generals.zimmerfrei.repository.entities

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

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