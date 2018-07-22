package com.generals.zimmerfrei.common.eventbus

import io.reactivex.Observable

interface EventBus<T> {

    fun emit(event: T)

    fun observe(): Observable<T>
}