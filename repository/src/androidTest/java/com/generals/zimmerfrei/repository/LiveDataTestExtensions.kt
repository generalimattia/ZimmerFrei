package com.generals.zimmerfrei.repository

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Observer
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit


fun <T> LiveData<T>.getValueImmediately(): T {
    var result: T? = null
    val latch = CountDownLatch(1)
    val observer = object : Observer<T> {

        override fun onChanged(t: T?) {
            result = t
            latch.countDown()
            removeObserver(this)
        }
    }
    observeForever(observer)
    latch.await(2, TimeUnit.SECONDS)
    return result!!
}