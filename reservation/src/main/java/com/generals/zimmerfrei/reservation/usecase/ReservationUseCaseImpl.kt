package com.generals.zimmerfrei.reservation.usecase

import com.generals.roomrepository.RoomRepository
import com.generals.zimmerfrei.database.dao.ReservationDAO
import com.generals.zimmerfrei.model.Reservation
import com.generals.zimmerfrei.model.Room
import io.reactivex.Maybe
import io.reactivex.MaybeEmitter
import io.reactivex.Single
import io.reactivex.SingleEmitter
import javax.inject.Inject

class ReservationUseCaseImpl @Inject constructor(
        private val reservationDao: ReservationDAO,
        private val roomRepository: RoomRepository
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

    override fun getAllRooms(): Maybe<List<Room>> = roomRepository.getAllRooms()

    override fun getRoomByListPosition(position: Int): Maybe<Room> =
            Maybe.create { emitter: MaybeEmitter<Room> ->
                roomRepository.getAllRooms()
                        .subscribe { rooms: List<Room> ->
                            if (rooms.isEmpty() || rooms.size <= position) {
                                emitter.onError(Throwable("No room available"))
                            } else {
                                emitter.onSuccess(rooms[position])
                            }
                            emitter.onComplete()
                        }
            }

    override fun update(reservation: Reservation): Single<Unit> =
            Single.create<Unit> { emitter: SingleEmitter<Unit> ->
                try {
                    reservationDao.update(reservation.toEntity())
                    emitter.onSuccess(Unit)
                } catch (e: Exception) {
                    emitter.onError(e)
                }
            }

    override fun delete(reservation: Reservation): Single<Unit> =
            Single.create<Unit> { emitter: SingleEmitter<Unit> ->
                try {
                    reservationDao.delete(reservation.toEntity())
                    emitter.onSuccess(Unit)
                } catch (e: Exception) {
                    emitter.onError(e)
                }
            }
}