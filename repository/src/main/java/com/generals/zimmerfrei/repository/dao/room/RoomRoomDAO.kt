package com.generals.zimmerfrei.repository.dao.room

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import com.generals.zimmerfrei.repository.entities.Room

@Dao
interface RoomRoomDAO {

    @Insert
    fun insert(rooms: List<Room>)

    @Query("SELECT * FROM rooms")
    fun getAllRooms(): LiveData<List<Room>>
}