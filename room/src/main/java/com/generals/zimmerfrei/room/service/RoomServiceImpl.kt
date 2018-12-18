package com.generals.zimmerfrei.room.service

import com.generals.zimmerfrei.model.Room
import com.generals.zimmerfrei.repository.dao.RoomDAO
import com.generals.zimmerfrei.service.RoomFetcherService
import io.reactivex.Single
import io.reactivex.SingleEmitter
import javax.inject.Inject

class RoomServiceImpl @Inject constructor(
        private val roomFetcherService: RoomFetcherService,
        private val dao: RoomDAO
) : RoomService, RoomFetcherService by roomFetcherService {

    override fun save(room: Room): Single<Unit> =
            createAsyncOperation { dao.insert(room.toEntity()) }

    override fun update(room: Room): Single<Unit> =
            createAsyncOperation { dao.update(room.toEntity()) }

    override fun delete(room: Room): Single<Unit> =
            createAsyncOperation { dao.delete(room.toEntity()) }

    private fun createAsyncOperation(dbOperation: () -> Unit): Single<Unit> =
            Single.create<Unit> { emitter: SingleEmitter<Unit> ->
                try {
                    dbOperation()
                    emitter.onSuccess(Unit)
                } catch (e: Exception) {
                    emitter.onError(e)
                }
            }
}