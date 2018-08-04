package com.generals.zimmerfrei.overview.viewmodel

import android.app.Activity
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.generals.zimmerfrei.common.navigator.Navigator
import com.generals.zimmerfrei.model.Day
import com.generals.zimmerfrei.model.Room
import com.generals.zimmerfrei.overview.usecase.OverviewUseCase
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import org.threeten.bp.LocalDate

import javax.inject.Inject

class OverviewViewModel @Inject constructor(
    private val useCase: OverviewUseCase, private val navigator: Navigator
) : ViewModel() {

    private val _month = MutableLiveData<String>()
    private val _days = MutableLiveData<List<Day>>()
    private val _moreDays = MutableLiveData<List<Day>>()
    private val _rooms: MutableLiveData<List<Room>> = MutableLiveData()

    private val compositeDisposable = CompositeDisposable()

    val month: LiveData<String>
        get() = _month

    val days: LiveData<List<Day>>
        get() = _days

    val moreDays: LiveData<List<Day>>
        get() = _moreDays

    val rooms: LiveData<List<Room>>
        get() = _rooms

    fun start() {
        compositeDisposable.add(
            useCase.loadRooms()
                .subscribeOn(Schedulers.computation()).observeOn(
                    AndroidSchedulers.mainThread()
                ).subscribe { internalRooms: List<Room>? ->
                    internalRooms?.let {
                        _rooms.value = it
                    }
                })

        compositeDisposable.add(
            useCase.
                loadDays(LocalDate.now())
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    daysAndMonth: Pair<List<Day>, String>? ->
                    daysAndMonth?.let {
                        _days.value = it.first
                        _month.value = it.second
                    }
                }
        )

        /*compositeDisposable.add(useCase.loadCalendar().subscribeOn(Schedulers.computation()).observeOn(
            AndroidSchedulers.mainThread()
        ).subscribe { day: DayWithReservations? ->
            day?.let {
                _days.value = it
            }
        })*/
    }

    fun loadMoreDays() {
        compositeDisposable.add(
            useCase.
                loadMoreDays()
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                        daysAndMonth: Pair<List<Day>, String>? ->
                    daysAndMonth?.let {
                        _moreDays.value = daysAndMonth.first
                        _month.value = daysAndMonth.second
                    }
                }
        )
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