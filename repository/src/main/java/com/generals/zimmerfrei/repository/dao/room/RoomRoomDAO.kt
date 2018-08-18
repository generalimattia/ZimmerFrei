package com.generals.zimmerfrei.repository.dao.room

import android.arch.persistence.room.*
import com.generals.zimmerfrei.repository.entities.RoomEntity
import io.reactivex.Flowable

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
    fun findById(id: Long): Flowable<RoomEntity>

    @Query("SELECT * FROM rooms")
    fun getAllRooms(): Flowable<List<RoomEntity>>
}