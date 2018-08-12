package com.generals.zimmerfrei.service

import com.generals.zimmerfrei.model.Room
import io.reactivex.Flowable

interface RoomFetcherService {

    fun fetchRooms(): Flowable<List<Room>>
}