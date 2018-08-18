package com.generals.zimmerfrei.reservation.usecase

import com.generals.zimmerfrei.model.Reservation
import com.generals.zimmerfrei.repository.dao.ReservationDAO
import io.reactivex.Single
import io.reactivex.SingleEmitter
import javax.inject.Inject

class ReservationUseCaseImpl @Inject constructor(
    private val dao: ReservationDAO
) : ReservationUseCase {

    override fun save(reservation: Reservation): Single<Unit> =
        Single.create<Unit> { emitter: SingleEmitter<Unit> ->
            try {
                dao.insert(reservation.toEntity())
                emitter.onSuccess(Unit)
            } catch (e: Exception) {
                emitter.onError(e)
            }
        }
}