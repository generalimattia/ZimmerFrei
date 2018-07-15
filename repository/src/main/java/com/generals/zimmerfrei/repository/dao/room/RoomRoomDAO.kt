package com.generals.zimmerfrei.repository.dao.room

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import com.generals.zimmerfrei.repository.entities.RoomEntity
import io.reactivex.Flowable

@Dao
interface RoomRoomDAO {

    @Insert
    fun insert(rooms: List<RoomEntity>)

    @Query("SELECT * FROM rooms")
    fun getAllRooms(): Flowable<List<RoomEntity>>
}