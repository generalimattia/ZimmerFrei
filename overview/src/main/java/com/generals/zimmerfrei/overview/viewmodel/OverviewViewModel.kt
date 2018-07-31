package com.generals.zimmerfrei.overview.viewmodel

import android.app.Activity
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.generals.zimmerfrei.common.navigator.Navigator
import com.generals.zimmerfrei.model.DayWithReservations
import com.generals.zimmerfrei.model.Room
import com.generals.zimmerfrei.overview.usecase.OverviewUseCase
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

import javax.inject.Inject

class OverviewViewModel @Inject constructor(
    private val useCase: OverviewUseCase, private val navigator: Navigator
) : ViewModel() {

    private val _days = MutableLiveData<DayWithReservations>()
    private val _rooms: MutableLiveData<List<Room>> = MutableLiveData()

    private val compositeDisposable = CompositeDisposable()

    val days: LiveData<DayWithReservations>
        get() = _days

    val rooms: LiveData<List<Room>>
        get() = _rooms

    fun start() {
        compositeDisposable.add(
            useCase.loadRooms().subscribeOn(Schedulers.computation()).observeOn(
                    AndroidSchedulers.mainThread()
                ).subscribe { internalRooms: List<Room>? ->
                    internalRooms?.let {
                        _rooms.value = it
                    }
                })

        /*compositeDisposable.add(useCase.loadCalendar().subscribeOn(Schedulers.computation()).observeOn(
            AndroidSchedulers.mainThread()
        ).subscribe { day: DayWithReservations? ->
            day?.let {
                _days.value = it
            }
        })*/
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