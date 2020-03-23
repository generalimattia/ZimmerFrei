package com.generals.zimmerfrei.database.dao.room

import androidx.room.*
import com.generals.zimmerfrei.database.entities.RoomEntity
import io.reactivex.Maybe

@Dao
interface RoomRoomDAO {

    @Insert
    fun insert(room: RoomEntity)

    @Insert
    fun insert(rooms: List<RoomEntity>)

    @Update
    fun update(room: RoomEntity)

    @Delete
    fun delete(room: RoomEntity)

    @Query("SELECT * FROM rooms WHERE id = :id")
    fun findById(id: Long): Maybe<RoomEntity>

    @Query("SELECT * FROM rooms")
    fun getAllRooms(): Maybe<List<RoomEntity>>
}