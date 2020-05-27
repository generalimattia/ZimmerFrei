package com.generals.zimmerfrei.listeners

import com.generals.zimmerfrei.model.Customer
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject
import javax.inject.Singleton

interface CustomerActionEmitter {
    fun emit(result: ActionResult<Customer>)
}

interface CustomerActionListener {
    val observable: Observable<ActionResult<Customer>>
}

@Singleton
class CustomerActionEmitterImpl @Inject constructor() : CustomerActionListener, CustomerActionEmitter {

    private val _observable: PublishSubject<ActionResult<Customer>> = PublishSubject.create()

    override val observable: Observable<ActionResult<Customer>>
        get() = _observable

    override fun emit(result: ActionResult<Customer>) {
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

