package com.generals.zimmerfrei.overview.view.customer.eventhandler

import com.generals.zimmerfrei.overview.view.customer.usecase.ActionResult
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject
import javax.inject.Singleton

interface CustomerActionEmitter {
    fun emit(result: ActionResult)
}

interface CustomerActionListener {
    val observable: Observable<ActionResult>
}

@Singleton
class CustomerActionEmitterImpl @Inject constructor() : CustomerActionListener, CustomerActionEmitter {

    private val _observable: PublishSubject<ActionResult> = PublishSubject.create()

    override val observable: Observable<ActionResult>
        get() = _observable

    override fun emit(result: ActionResult) {
        _observable.onNext(result)
    }
}

