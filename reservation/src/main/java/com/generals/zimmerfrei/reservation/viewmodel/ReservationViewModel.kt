package com.generals.zimmerfrei.reservation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import arrow.core.Option
import com.generals.zimmerfrei.common.resources.StringResourcesProvider
import com.generals.zimmerfrei.common.utils.randomColor
import com.generals.zimmerfrei.common.utils.safeToInt
import com.generals.zimmerfrei.listeners.ActionEmitter
import com.generals.zimmerfrei.listeners.ActionListener
import com.generals.zimmerfrei.listeners.ActionResult
import com.generals.zimmerfrei.model.Customer
import com.generals.zimmerfrei.model.ParcelableDay
import com.generals.zimmerfrei.model.Reservation
import com.generals.zimmerfrei.model.Room
import com.generals.zimmerfrei.reservation.R
import com.generals.zimmerfrei.reservation.usecase.ReservationUseCase
import com.generals.zimmerfrei.reservation.view.adapters.ColorItem
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDate
import timber.log.Timber
import javax.inject.Inject

class ReservationViewModel @Inject constructor(
        private val useCase: ReservationUseCase,
        private val stringProvider: StringResourcesProvider,
        private val customerActionListener: ActionListener<Customer>,
        private val reservationActionEmitter: ActionEmitter<Reservation>
) : ViewModel() {

    private var roomPosition = 0

    private var startDay: Int = 0
    private var startMonth: Int = 0
    private var startYear: Int = 0

    private var endDay: Int = 0
    private var endMonth: Int = 0
    private var endYear: Int = 0

    private var currentReservation: Reservation? = null
    private var currentCustomer: Customer? = null

    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    private val _pressBack = MutableLiveData<Boolean>()
    private val _roomError = MutableLiveData<String>()
    private val _nameError = MutableLiveData<String>()
    private val _startDateError = MutableLiveData<String>()
    private val _endDateError = MutableLiveData<String>()
    private val _rooms = MutableLiveData<List<String>>()
    private val _preselectedRoom = MutableLiveData<Int>()
    private val _isEditing = MutableLiveData<Boolean>()

    private val _selectedRoom = MutableLiveData<String>()
    private val _startDate = MutableLiveData<ParcelableDay>()
    private val _endDate = MutableLiveData<ParcelableDay>()
    private val _selectedColor = MutableLiveData<ColorItem>()
    private val _result = MutableLiveData<String>()

    private val _reservation = MutableLiveData<Reservation>()
    private val _customer = MutableLiveData<Customer?>()

    val pressBack: LiveData<Boolean>
        get() = _pressBack

    val roomError: LiveData<String>
        get() = _roomError

    val nameError: LiveData<String>
        get() = _nameError

    val startDateError: LiveData<String>
        get() = _startDateError

    val endDateError: LiveData<String>
        get() = _endDateError

    val rooms: LiveData<List<String>>
        get() = _rooms

    val selectedRoom: LiveData<String>
        get() = _selectedRoom

    val isEditing: LiveData<Boolean>
        get() = _isEditing

    val startDate: LiveData<ParcelableDay>
        get() = _startDate

    val endDate: LiveData<ParcelableDay>
        get() = _endDate

    val preselectedRoom: LiveData<Int>
        get() = _preselectedRoom

    val selectedColor: LiveData<ColorItem>
        get() = _selectedColor

    val result: LiveData<String>
        get() = _result

    val reservation: LiveData<Reservation>
        get() = _reservation

    val customer: LiveData<Customer?>
        get() = _customer

    fun start(
            selectedDay: ParcelableDay?,
            selectedRoom: Room?,
            reservationURL: String?
    ) {
        if (selectedDay != null && selectedRoom != null) {
            if (reservationURL.isNullOrBlank()) {
                handleNewReservationFromDate(selectedDay, selectedRoom)
                _isEditing.value = false
            } else {
                handleExistingReservation(selectedRoom, reservationURL)
                _isEditing.value = true
            }
        } else {
            handleNewReservation()
            _isEditing.value = false
        }

        customerActionListener.observable.subscribe(
                { result: ActionResult<Customer> ->
                    if (result is ActionResult.Success || result is ActionResult.Selected) {
                        _customer.value = result.data
                        currentCustomer = result.data
                    }
                },
                Timber::e,
                {}
        ).also { compositeDisposable.add(it) }
    }

    private fun handleNewReservation() {
        fetchRooms()
        _startDate.value = null
        _endDate.value = null
    }

    private fun handleNewReservationFromDate(
            day: ParcelableDay,
            room: Room
    ) {
        fetchRooms(room)
        onStartDateSelected(ParcelableDay(day.dayOfMonth, day.month, day.year))
        _endDate.value = null
        _customer.value = null
    }

    private fun handleExistingReservation(
            room: Room,
            url: String
    ) {
        fetchRooms(room)
        fetchReservation(url)
    }

    fun onRoomSelected(position: Int) {
        val selectedRoom: String? = _rooms.value?.get(position)
        _selectedRoom.value = selectedRoom
        roomPosition = position
    }

    fun onStartDateSelected(day: ParcelableDay) {
        startDay = day.dayOfMonth
        startMonth = day.month
        startYear = day.year

        _startDate.value = day
    }

    fun onEndDateSelected(day: ParcelableDay) {
        endDay = day.dayOfMonth
        endMonth = day.month
        endYear = day.year

        _endDate.value = day
    }

    fun submit(
            name: String,
            adults: String,
            children: String,
            babies: String,
            roomName: String,
            notes: String
    ) {

        val isValid: Boolean = validate(roomName, name, startDay, endDay)

        if (isValid) {

            viewModelScope.launch {
                val room: Option<Room> = useCase.getRoomByListPosition(roomPosition)
                room.fold(
                        ifEmpty = {},
                        ifSome = { notNullRoom: Room ->
                            val result: ActionResult<Reservation> = currentReservation?.let { validReservation: Reservation ->
                                useCase.update(
                                        validReservation.copy(
                                                name = name,
                                                startDate = LocalDate.of(startYear, startMonth, startDay),
                                                endDate = LocalDate.of(endYear, endMonth, endDay),
                                                adults = adults.safeToInt(),
                                                children = children.safeToInt(),
                                                babies = babies.safeToInt(),
                                                color = _selectedColor.value?.hex ?: randomColor(),
                                                notes = notes,
                                                room = notNullRoom,
                                                customer = currentCustomer
                                        ))
                            } ?: useCase.save(
                                    Reservation(
                                            name = name,
                                            startDate = LocalDate.of(startYear, startMonth, startDay),
                                            endDate = LocalDate.of(endYear, endMonth, endDay),
                                            adults = adults.safeToInt(),
                                            children = children.safeToInt(),
                                            babies = babies.safeToInt(),
                                            color = _selectedColor.value?.hex ?: randomColor(),
                                            notes = notes,
                                            room = notNullRoom,
                                            customer = currentCustomer
                                    ))

                            handleResult(result)
                        }
                )
            }
        }
    }

    private fun handleResult(result: ActionResult<Reservation>) {
        if (result is ActionResult.Error) {
            _result.value = result.message
        } else {
            reservationActionEmitter.emit(result)
            _pressBack.value = true
        }
    }

    fun delete() {
        viewModelScope.launch {
            currentReservation?.also {
                handleResult(useCase.delete(it))
            }
        }
    }

    private fun validate(roomName: String, name: String, startDay: Int, endDay: Int): Boolean {
        var isValid = true

        if (roomName.isBlank()) {
            _roomError.value = stringProvider.provide(R.string.mandatory_field)
            isValid = false
        }

        if (name.isBlank()) {
            _nameError.value = stringProvider.provide(R.string.mandatory_field)
            isValid = false
        }

        if (startDay == 0) {
            _startDateError.value = stringProvider.provide(R.string.mandatory_field)
            isValid = false
        }

        if (endDay == 0) {
            _endDateError.value = stringProvider.provide(R.string.mandatory_field)
            isValid = false
        }

        if (LocalDate.of(startYear, startMonth, startDay).isAfter(LocalDate.of(endYear, endMonth, endDay))) {
            _startDateError.value = stringProvider.provide(R.string.start_date_after_end_date_error)
            isValid = false
        }

        return isValid
    }

    private fun fetchRooms(preselected: Room? = null) {
        viewModelScope.launch {
            val allRooms: List<Room> = useCase.getAllRooms()
            _rooms.value = allRooms.map { entity: Room -> entity.name }
            preselected?.let {
                if (allRooms.contains(it)) {
                    _preselectedRoom.value = allRooms.indexOf(it)
                }
            }
        }
    }

    private fun fetchReservation(url: String) {
        viewModelScope.launch {
            val result: Reservation? = useCase.get(url).orNull()

            result?.also { value: Reservation ->
                val startDay = ParcelableDay(value.startDate)
                val endDay = ParcelableDay(value.endDate)

                _startDate.value = startDay
                _endDate.value = endDay
                onStartDateSelected(startDay)
                onEndDateSelected(endDay)
            }

            currentReservation = result
            _customer.value = result?.customer
        }
    }

    fun onColorClick(color: ColorItem) {
        color.selected = !color.selected
        _selectedColor.value = color
    }

    override fun onCleared() {
        compositeDisposable.dispose()
    }

}