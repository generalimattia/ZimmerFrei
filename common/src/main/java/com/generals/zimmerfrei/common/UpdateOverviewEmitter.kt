package com.generals.zimmerfrei.common

import io.reactivex.disposables.Disposable
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

interface UpdateOverviewEmitter {

    fun subscribe(observer: Observer)

    fun unsubscribe(observer: Observer)

    fun emit()

    interface Observer {
        fun onOverviewUpdated()
    }
}

class UpdateOverviewEmitterImpl @Inject constructor() : UpdateOverviewEmitter {

    private val observers: MutableMap<UpdateOverviewEmitter.Observer, Disposable> = mutableMapOf()
    private val subject: PublishSubject<Unit> = PublishSubject.create()

    override fun subscribe(observer: UpdateOverviewEmitter.Observer) {
        observers[observer] = subject.subscribe {
            observer.onOverviewUpdated()
        }
    }

    override fun unsubscribe(observer: UpdateOverviewEmitter.Observer) {
        observers[observer]?.dispose()
        observers.remove(observer)
    }

    override fun emit() {
        subject.onNext(Unit)
    }
}