package com.generals.zimmerfrei.room.service

import com.generals.zimmerfrei.model.Room
import com.generals.zimmerfrei.service.RoomFetcherService
import io.reactivex.Single

interface RoomService: RoomFetcherService {

    fun save(room: Room): Single<Unit>

    fun update(room: Room): Single<Unit>

    fun delete(room: Room): Single<Unit>
}