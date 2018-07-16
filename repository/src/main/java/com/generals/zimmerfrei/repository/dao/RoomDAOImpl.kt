package com.generals.zimmerfrei.repository.dao

import android.content.Context
import com.generals.zimmerfrei.repository.dao.room.RoomRoomDAO
import com.generals.zimmerfrei.repository.database.ReservationDatabase
import com.generals.zimmerfrei.repository.entities.RoomEntity
import io.reactivex.Flowable
import javax.inject.Inject

class RoomDAOImpl @Inject constructor(
    context: Context
) : RoomDAO {

    private val dao: RoomRoomDAO = ReservationDatabase.getDatabase(context).roomDAO()

    override fun insert(rooms: List<RoomEntity>) {
        dao.insert(rooms)
    }

    override fun getAllReservations(): Flowable<List<RoomEntity>> = dao.getAllRooms()
}