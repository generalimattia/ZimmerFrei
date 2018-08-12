package com.generals.zimmerfrei.room.service

import com.generals.zimmerfrei.model.Room
import com.generals.zimmerfrei.service.RoomFetcherService

interface RoomService: RoomFetcherService {

    fun save(room: Room)
}