package com.generals.zimmerfrei.service

import com.generals.zimmerfrei.model.Room
import com.generals.zimmerfrei.repository.dao.RoomDAO
import com.generals.zimmerfrei.repository.entities.RoomEntity
import io.reactivex.Flowable
import javax.inject.Inject

class RoomFetcherServiceImpl @Inject constructor(
    private val dao: RoomDAO
) : RoomFetcherService {

    override fun fetchRooms(): Flowable<List<Room>> =
        dao.getAllRooms().map { entities: List<RoomEntity> ->
            entities.map { Room(it) }
        }
}