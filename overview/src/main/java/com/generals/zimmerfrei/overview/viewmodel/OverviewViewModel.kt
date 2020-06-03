package com.generals.zimmerfrei.overview.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.generals.zimmerfrei.listeners.ActionListener
import com.generals.zimmerfrei.listeners.ActionResult
import com.generals.zimmerfrei.model.Day
import com.generals.zimmerfrei.model.Reservation
import com.generals.zimmerfrei.model.Room
import com.generals.zimmerfrei.model.RoomDay
import com.generals.zimmerfrei.overview.usecase.OverviewUseCase
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDate
import timber.log.Timber
import javax.inject.Inject
import kotlin.properties.Delegates
import kotlin.reflect.KProperty

class OverviewViewModel @Inject constructor(
        private val useCase: OverviewUseCase,
        private val reservationActionListener: ActionListener<Reservation>
) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    private val _month = MutableLiveData<String>()
    private val _days = MutableLiveData<List<Day>>()
    private val _rooms = MutableLiveData<List<Room>>()
    private val _selectedDate = MutableLiveData<LocalDate>()
    private val _reservations = MutableLiveData<List<Pair<Room, List<RoomDay>>>>()
    private val _result: MutableLiveData<String> = MutableLiveData()

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

    val result: LiveData<String>
        get() = _result

    fun start() {
        reservationActionListener.observable.subscribe(
                { actionResult: ActionResult<Reservation> ->
                    if (actionResult is ActionResult.Success) {
                        loadReservations(date)
                        _result.value = actionResult.message
                    }
                },
                Timber::e,
                {}
        ).also { compositeDisposable.add(it) }

        _selectedDate.value = date

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

    override fun onCleared() {
        compositeDisposable.clear()
    }
}