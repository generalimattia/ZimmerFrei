package com.generals.zimmerfrei.overview.viewmodel

import android.app.Activity
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.support.annotation.IdRes
import android.support.v7.app.AppCompatActivity
import com.generals.zimmerfrei.model.Day
import com.generals.zimmerfrei.model.Room
import com.generals.zimmerfrei.model.RoomDay
import com.generals.zimmerfrei.navigator.Navigator
import com.generals.zimmerfrei.overview.usecase.OverviewUseCase
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import org.threeten.bp.LocalDate

import javax.inject.Inject
import kotlin.properties.Delegates
import kotlin.reflect.KProperty

class OverviewViewModel @Inject constructor(
    private val useCase: OverviewUseCase, private val navigator: Navigator
) : ViewModel() {

    private val _month = MutableLiveData<String>()
    private val _days = MutableLiveData<List<Day>>()
    private val _rooms: MutableLiveData<List<Room>> = MutableLiveData()
    private val _selectedDate: MutableLiveData<LocalDate> = MutableLiveData()
    private val _reservations: MutableLiveData<MutableList<Pair<Room, List<RoomDay>>>> =
        MutableLiveData()

    private var date: LocalDate by Delegates.observable(LocalDate.now()) { _: KProperty<*>, _: LocalDate?, newValue: LocalDate? ->
        newValue?.let {
            _selectedDate.value = it
            processNewDate(it)
        }
    }

    private val compositeDisposable = CompositeDisposable()

    val month: LiveData<String>
        get() = _month

    val days: LiveData<List<Day>>
        get() = _days

    val rooms: LiveData<List<Room>>
        get() = _rooms

    val selectedDate: LiveData<LocalDate>
        get() = _selectedDate

    val reservations: LiveData<MutableList<Pair<Room, List<RoomDay>>>>
        get() = _reservations

    fun start() {
        _selectedDate.value = date
        _reservations.value = mutableListOf()

        loadRooms()

        processNewDate(date)
    }

    private fun loadRooms() {
        compositeDisposable.add(useCase.loadRooms().subscribeOn(Schedulers.computation()).observeOn(
            AndroidSchedulers.mainThread()
        ).subscribe { internalRooms: List<Room>? ->
            internalRooms?.let {
                _rooms.value = it
            }
        })
    }

    fun onPreviousMonthClick() {
        date = date.minusMonths(1)
    }

    fun onNextMonthClick() {
        date = date.plusMonths(1)
    }

    fun onNewDate(month: Int, year: Int) {
        date = date.withMonth(month + 1)
            .withYear(year)
    }

    private fun processNewDate(newDate: LocalDate) {
        loadCalendar(newDate)

        loadReservations(newDate)
    }

    private fun loadReservations(currentDate: LocalDate) {

        useCase.loadReservations(
            currentDate.withDayOfMonth(1),
            currentDate.withDayOfMonth(currentDate.month.length(currentDate.isLeapYear))
        )
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { roomDay: Pair<Room, List<RoomDay>>? ->
                roomDay?.let {
                    _reservations.value?.add(it)
                }
            }
    }

    private fun loadCalendar(currentDate: LocalDate) {
        compositeDisposable.add(useCase.loadDays(currentDate).subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread()).subscribe { daysAndMonth: Pair<List<Day>, String>? ->
            daysAndMonth?.let {
                _days.value = it.first
                _month.value = it.second
            }
        })
    }

    fun onRoomsMenuItemClick(activity: AppCompatActivity, @IdRes containerViewId: Int) {
        navigator.roomList(containerViewId)
            .startNewFragment(
                activity = activity,
                containerViewId = containerViewId,
                addToBackStack = true
            )
    }

    fun onFABClick(activity: Activity) {
        navigator.reservation()
            .startNewActivity(activity)
    }

    override fun onCleared() {
        compositeDisposable.dispose()
    }
}