package com.generals.zimmerfrei.room.service

import com.generals.zimmerfrei.model.Room
import com.generals.zimmerfrei.repository.dao.RoomDAO
import com.generals.zimmerfrei.service.RoomFetcherService
import javax.inject.Inject

class RoomServiceImpl @Inject constructor(
    private val roomFetcherService: RoomFetcherService, private val dao: RoomDAO
) : RoomService, RoomFetcherService by roomFetcherService {

    override fun save(room: Room) = dao.insert(room.toEntity())
}