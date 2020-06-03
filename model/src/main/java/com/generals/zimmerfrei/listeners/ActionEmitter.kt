package com.generals.zimmerfrei.listeners

import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject
import javax.inject.Singleton

interface ActionEmitter<T> {
    fun emit(result: ActionResult<T>)
}

interface ActionListener<T> {
    val observable: Observable<ActionResult<T>>
}

@Singleton
class ActionEmitterImpl<T> @Inject constructor() : ActionListener<T>, ActionEmitter<T> {

    private val _observable: PublishSubject<ActionResult<T>> = PublishSubject.create()

    override val observable: Observable<ActionResult<T>>
        get() = _observable

    override fun emit(result: ActionResult<T>) {
        _observable.onNext(result)
    }
}

sealed class ActionResult<out T> {
    abstract val message: String

    data class Success<out T>(
            override val message: String,
            val data: T?
    ) : ActionResult<T>()

    data class Error(
            override val message: String
    ) : ActionResult<Nothing>()
}

