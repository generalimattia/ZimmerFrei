package com.generals.zimmerfrei.reservation.viewmodel

import android.app.Activity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import arrow.core.Option
import com.generals.zimmerfrei.common.UpdateOverviewEmitter
import com.generals.zimmerfrei.common.resources.StringResourcesProvider
import com.generals.zimmerfrei.common.utils.randomColor
import com.generals.zimmerfrei.model.ParcelableDay
import com.generals.zimmerfrei.model.ParcelableRoomDay
import com.generals.zimmerfrei.model.Reservation
import com.generals.zimmerfrei.model.Room
import com.generals.zimmerfrei.navigator.Navigator
import com.generals.zimmerfrei.reservation.R
import com.generals.zimmerfrei.reservation.usecase.ReservationUseCase
import com.generals.zimmerfrei.reservation.view.adapters.ColorItem
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDate
import javax.inject.Inject

class ReservationViewModel @Inject constructor(
        private val useCase: ReservationUseCase,
        private val stringProvider: StringResourcesProvider,
        private val updateOverviewEmitter: UpdateOverviewEmitter,
        private val navigator: Navigator
) : ViewModel() {

    private var roomPosition = 0

    private var startDay: Int = 0
    private var startMonth: Int = 0
    private var startYear: Int = 0

    private var endDay: Int = 0
    private var endMonth: Int = 0
    private var endYear: Int = 0

    private var reservation: Reservation? = null

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
    private val _name = MutableLiveData<String>()
    private val _email = MutableLiveData<String>()
    private val _mobile = MutableLiveData<String>()
    private val _adultsCount = MutableLiveData<String>()
    private val _childrenCount = MutableLiveData<String>()
    private val _babiesCount = MutableLiveData<String>()
    private val _selectedColor = MutableLiveData<ColorItem>()
    private val _notes = MutableLiveData<String>()

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

    val name: LiveData<String>
        get() = _name

    val email: LiveData<String>
        get() = _email

    val mobile: LiveData<String>
        get() = _mobile

    val adultsCount: LiveData<String>
        get() = _adultsCount

    val childrenCount: LiveData<String>
        get() = _childrenCount

    val babiesCount: LiveData<String>
        get() = _babiesCount

    val selectedColor: LiveData<ColorItem>
        get() = _selectedColor

    val notes: LiveData<String>
        get() = _notes

    fun start(
            reservation: ParcelableRoomDay?
    ) {
        reservation?.let {
            when (it) {
                is ParcelableRoomDay.Empty -> {
                    handleNewReservationFromDate(it)
                    _isEditing.value = false
                }
                is ParcelableRoomDay.Reserved -> {
                    handleExistingReservation(it)
                    this.reservation = it.reservation
                    _isEditing.value = true
                }
            }
        } ?: let {
            handleNewReservation()
            _isEditing.value = false
        }
    }

    private fun handleNewReservation() {
        fetchRooms()
        _startDate.value = null
        _endDate.value = null
    }

    private fun handleNewReservationFromDate(it: ParcelableRoomDay.Empty) {
        fetchRooms(it.room)
        onStartDateSelected(ParcelableDay(it.day.dayOfMonth, it.day.month, it.day.year))
        _endDate.value = null
    }

    private fun handleExistingReservation(it: ParcelableRoomDay.Reserved) {
        fetchRooms(it.reservation.room)

        val startDay = ParcelableDay(it.reservation.startDate)
        val endDay = ParcelableDay(it.reservation.endDate)

        _startDate.value = startDay
        _endDate.value = endDay
        onStartDateSelected(startDay)
        onEndDateSelected(endDay)

        _name.value = it.reservation.name
        _email.value = it.reservation.email
        _mobile.value = it.reservation.mobile
        _adultsCount.value = it.reservation.adults.toString()
        _childrenCount.value = it.reservation.children.toString()
        _babiesCount.value = it.reservation.babies.toString()
        _selectedColor.value = ColorItem(hex = it.reservation.color, selected = true)
        _notes.value = it.reservation.notes
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
            notes: String,
            mobile: String,
            email: String
    ) {

        val isValid = validate(roomName, name, startDay, endDay)

        if (isValid) {

            val adultsNumber: Int = try {
                adults.toInt()
            } catch (e: NumberFormatException) {
                0
            }

            val childrenNumber: Int = try {
                children.toInt()
            } catch (e: NumberFormatException) {
                0
            }

            val babiesNumber: Int = try {
                babies.toInt()
            } catch (e: NumberFormatException) {
                0
            }

            viewModelScope.launch {
                val room: Option<Room> = useCase.getRoomByListPosition(roomPosition)
                room.fold(
                        ifEmpty = {},
                        ifSome = { notNullRoom: Room ->
                            reservation?.let {
                                useCase.update(it.copy(
                                        name = name,
                                        startDate = LocalDate.of(startYear, startMonth, startDay),
                                        endDate = LocalDate.of(endYear, endMonth, endDay),
                                        adults = adultsNumber,
                                        children = childrenNumber,
                                        babies = babiesNumber,
                                        color = _selectedColor.value?.hex ?: randomColor(),
                                        notes = notes,
                                        mobile = mobile,
                                        email = email,
                                        room = notNullRoom
                                ))
                            } ?: viewModelScope.launch {
                                useCase.save(Reservation(
                                        name = name,
                                        startDate = LocalDate.of(startYear, startMonth, startDay),
                                        endDate = LocalDate.of(endYear, endMonth, endDay),
                                        adults = adultsNumber,
                                        children = childrenNumber,
                                        babies = babiesNumber,
                                        color = _selectedColor.value?.hex ?: randomColor(),
                                        notes = notes,
                                        mobile = mobile,
                                        email = email,
                                        room = notNullRoom
                                ))
                            }

                            updateOverviewEmitter.emit()
                            _pressBack.value = true
                        }
                )
            }
        }
    }

    fun delete() {
        viewModelScope.launch {
            reservation?.also {
                useCase.delete(it)
                _pressBack.value = true
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

    fun onSendEmailClick(
            to: String,
            activity: Activity
    ) {
        if (to.isNotBlank()) {
            navigator.email(to)
                    .startNewActivity(activity)
        }
    }

    fun onDialMobileClick(
            number: String,
            activity: Activity
    ) {
        if (number.isNotBlank()) {
            navigator.dial(number)
                    .startNewActivity(activity)
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