package com.generals.zimmerfrei.overview.viewmodel

import android.app.Activity
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.generals.zimmerfrei.common.UpdateOverviewEmitter
import com.generals.zimmerfrei.model.Day
import com.generals.zimmerfrei.model.Room
import com.generals.zimmerfrei.model.RoomDay
import com.generals.zimmerfrei.navigator.Navigator
import com.generals.zimmerfrei.overview.usecase.OverviewUseCase
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDate
import javax.inject.Inject
import kotlin.properties.Delegates
import kotlin.reflect.KProperty

class OverviewViewModel @Inject constructor(
        private val useCase: OverviewUseCase,
        private val navigator: Navigator,
        private val updateOverviewEmitter: UpdateOverviewEmitter
) : ViewModel(), UpdateOverviewEmitter.Observer {

    private val _month = MutableLiveData<String>()
    private val _days = MutableLiveData<List<Day>>()
    private val _rooms = MutableLiveData<List<Room>>()
    private val _selectedDate = MutableLiveData<LocalDate>()
    private val _reservations = MutableLiveData<List<Pair<Room, List<RoomDay>>>>()

    private var date: LocalDate by Delegates.observable(LocalDate.now()) { _: KProperty<*>, _: LocalDate?, newValue: LocalDate? ->
        newValue?.let {
            _reservations.value = mutableListOf()
            _selectedDate.value = it
            processNewDate(it)
        }
    }

    val month: LiveData<String>
        get() = _month

    val days: LiveData<List<Day>>
        get() = _days

    val rooms: LiveData<List<Room>>
        get() = _rooms

    val selectedDate: LiveData<LocalDate>
        get() = _selectedDate

    val reservations: LiveData<List<Pair<Room, List<RoomDay>>>>
        get() = _reservations

    fun start() {
        updateOverviewEmitter.subscribe(this)

        _selectedDate.value = date
        _reservations.value = mutableListOf()

        loadRooms()

        processNewDate(date)
    }

    private fun loadRooms() {
        viewModelScope.launch {
            val rooms: List<Room> = useCase.loadRooms()
            _rooms.value = rooms
        }

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
        viewModelScope.launch {
            val reservationByRoom: List<Pair<Room, List<RoomDay>>> = useCase.loadReservationsByPeriod(currentDate)
            _reservations.value = reservationByRoom
        }
    }

    private fun loadCalendar(currentDate: LocalDate) {
        viewModelScope.launch {
            val daysAndMonth: Pair<List<Day>, String> = useCase.loadDays(currentDate)
            _days.value = daysAndMonth.first
            _month.value = daysAndMonth.second
        }
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

    fun onDayClick(day: RoomDay, activity: Activity) {
        navigator.reservation(day)
                .startNewActivity(activity)
    }

    override fun onOverviewUpdated() {
        loadReservations(date)
    }

    override fun onCleared() {
        updateOverviewEmitter.unsubscribe(this)
    }
}