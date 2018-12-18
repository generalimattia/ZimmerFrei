package com.generals.zimmerfrei.reservation.usecase

import com.generals.zimmerfrei.model.Reservation
import com.generals.zimmerfrei.model.Room
import com.generals.zimmerfrei.database.dao.ReservationDAO
import com.generals.zimmerfrei.database.dao.RoomDAO
import com.generals.zimmerfrei.database.entities.RoomEntity
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.SingleEmitter
import javax.inject.Inject

class ReservationUseCaseImpl @Inject constructor(
        private val reservationDao: ReservationDAO,
        private val roomDao: RoomDAO
) : ReservationUseCase {

    override fun save(reservation: Reservation): Single<Unit> =
            Single.create<Unit> { emitter: SingleEmitter<Unit> ->
                try {
                    reservationDao.insert(reservation.toEntity())
                    emitter.onSuccess(Unit)
                } catch (e: Exception) {
                    emitter.onError(e)
                }
            }

    override fun getRoomByName(name: String): Observable<Room> =
            roomDao.findByName(name).map { entity: RoomEntity ->
                Room(entity)
            }.toObservable()

    override fun getAllRooms(): Observable<List<Room>> =
            roomDao.getAllRooms().map { entities: List<RoomEntity> ->
                entities.map { Room(it) }
            }.toObservable()
}