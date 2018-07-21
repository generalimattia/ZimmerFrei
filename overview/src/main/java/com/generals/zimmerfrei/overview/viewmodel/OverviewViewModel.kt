package com.generals.zimmerfrei.overview.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.generals.zimmerfrei.overview.model.Day
import com.generals.zimmerfrei.overview.model.DayWithReservations
import com.generals.zimmerfrei.overview.model.Reservation
import com.generals.zimmerfrei.overview.usecase.OverviewUseCase
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

import javax.inject.Inject

class OverviewViewModel @Inject constructor(private val useCase: OverviewUseCase) : ViewModel() {

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

    override fun onCleared() {
        compositeDisposable.clear()
        super.onCleared()
    }
}