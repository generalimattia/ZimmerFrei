package com.generals.roomrepository

import com.generals.zimmerfrei.database.dao.RoomDAO
import com.generals.zimmerfrei.database.entities.RoomEntity
import com.generals.zimmerfrei.model.Room
import io.reactivex.Maybe
import io.reactivex.MaybeEmitter
import java.util.concurrent.atomic.AtomicBoolean
import javax.inject.Inject

class RoomRepositoryImpl @Inject constructor(
        private val dao: RoomDAO
) : RoomRepository {

    private val cache: MutableList<Room> = mutableListOf()
    private var isCacheValid: AtomicBoolean = AtomicBoolean(false)

    override fun getAllRooms(): Maybe<List<Room>> =
            if (isCacheValid.get()) {
                Maybe.just(cache)
            } else {
                dao.getAllRooms().map { entities: List<RoomEntity> ->
                    entities.map { Room(it) }
                }.doAfterSuccess {
                    cache.addAll(it)
                }.doFinally {
                    isCacheValid.set(true)
                }
            }

    override fun save(room: Room): Maybe<Unit> = createAsyncOperation(true) { dao.insert(room.toEntity()) }

    override fun update(room: Room): Maybe<Unit> = createAsyncOperation(true) { dao.update(room.toEntity()) }

    override fun delete(room: Room): Maybe<Unit> = createAsyncOperation(true) { dao.delete(room.toEntity()) }

    private fun createAsyncOperation(
            invalidateCache: Boolean,
            dbOperation: () -> Unit): Maybe<Unit> =
            Maybe.create<Unit> { emitter: MaybeEmitter<Unit> ->
                try {
                    if (invalidateCache) {
                        isCacheValid.set(false)
                        cache.clear()
                    }
                    dbOperation()
                    emitter.onSuccess(Unit)
                } catch (e: Exception) {
                    emitter.onError(e)
                }
            }
}