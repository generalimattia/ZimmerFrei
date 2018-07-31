package com.generals.zimmerfrei.overview.service.room

import com.generals.zimmerfrei.model.Room
import io.reactivex.Flowable

interface RoomService {

    fun fetchRooms(): Flowable<List<Room>>
}