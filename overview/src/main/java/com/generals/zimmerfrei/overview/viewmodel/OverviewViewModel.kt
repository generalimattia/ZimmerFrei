package com.generals.zimmerfrei.overview.viewmodel

import android.app.Activity
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.generals.zimmerfrei.common.navigator.Navigator
import com.generals.zimmerfrei.model.DayWithReservations
import com.generals.zimmerfrei.overview.usecase.OverviewUseCase
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

import javax.inject.Inject

class OverviewViewModel @Inject constructor(
    private val useCase: OverviewUseCase, private val navigator: Navigator
) : ViewModel() {

    private val _days = MutableLiveData<DayWithReservations>()
    private val compositeDisposable = CompositeDisposable()

    val days: LiveData<DayWithReservations>
        get() = _days

    fun start() {
        compositeDisposable.add(useCase.loadCalendar().subscribeOn(Schedulers.computation()).observeOn(
            AndroidSchedulers.mainThread()
        ).subscribe { day: DayWithReservations? ->
            day?.let {
                _days.value = it
            }
        })
    }

    fun onFABClick(activity: Activity) {
        navigator.reservation()
            .start(activity)
    }

    override fun onCleared() {
        compositeDisposable.clear()
        super.onCleared()
    }
}