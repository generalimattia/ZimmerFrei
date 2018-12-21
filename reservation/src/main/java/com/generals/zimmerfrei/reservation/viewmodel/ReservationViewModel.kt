package com.generals.zimmerfrei.reservation.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.generals.zimmerfrei.common.UpdateOverviewEmitter
import com.generals.zimmerfrei.common.extension.offsetDateTimeFromLocalDate
import com.generals.zimmerfrei.common.resources.StringResourcesProvider
import com.generals.zimmerfrei.model.ParcelableDay
import com.generals.zimmerfrei.model.ParcelableRoomDay
import com.generals.zimmerfrei.model.Reservation
import com.generals.zimmerfrei.model.Room
import com.generals.zimmerfrei.reservation.R
import com.generals.zimmerfrei.reservation.usecase.ReservationUseCase
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import org.threeten.bp.LocalDate
import javax.inject.Inject

class ReservationViewModel @Inject constructor(
        private val useCase: ReservationUseCase,
        private val stringProvider: StringResourcesProvider,
        private val updateOverviewEmitter: UpdateOverviewEmitter
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
    private val _color = MutableLiveData<String>()
    private val _notes = MutableLiveData<String>()

    private val availableColors: List<String> = listOf(
            "#d50000",
            "#c51162",
            "#8e24aa",
            "#6200ea",
            "#283593",
            "#2962ff",
            "#0091ea",
            "#00b8d4",
            "#00695c",
            "#4caf50",
            "#8bc34a",
            "#fbc02d",
            "#ff6f00",
            "#e65100",
            "#4e342e",
            "#546e7a"
    )

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

    val color: LiveData<String>
        get() = _color

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
        generateNewColor()
    }

    private fun handleNewReservationFromDate(it: ParcelableRoomDay.Empty) {
        fetchRooms(it.room)
        onStartDateSelected(ParcelableDay(it.day.dayOfMonth, it.day.month, it.day.year))
        _endDate.value = null
        generateNewColor()
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
        _color.value = it.reservation.color
        _notes.value = it.reservation.notes
    }

    fun generateNewColor() {
        val colorsCount = availableColors.size
        _color.value = availableColors[(0 until colorsCount).shuffled().first()]
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

            compositeDisposable.add(useCase.getRoomByListPosition(roomPosition)
                    .subscribeOn(Schedulers.newThread())
                    .subscribe { room: Room? ->
                        room?.let {

                            reservation?.let {
                                useCase.update(it.copy(
                                        name = name,
                                        startDate = offsetDateTimeFromLocalDate(LocalDate.of(startYear, startMonth, startDay)),
                                        endDate = offsetDateTimeFromLocalDate(LocalDate.of(endYear, endMonth, endDay)),
                                        adults = adultsNumber,
                                        children = childrenNumber,
                                        babies = babiesNumber,
                                        color = _color.value ?: availableColors.first(),
                                        notes = notes,
                                        mobile = mobile,
                                        email = email,
                                        room = room
                                ))
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe({
                                            updateOverviewEmitter.emit()
                                            _pressBack.value = true
                                        },
                                                {
                                                    _pressBack.value = true
                                                })
                            }
                                    ?: useCase.save(
                                            Reservation(
                                                    name = name,
                                                    startDate = offsetDateTimeFromLocalDate(LocalDate.of(startYear, startMonth, startDay)),
                                                    endDate = offsetDateTimeFromLocalDate(LocalDate.of(endYear, endMonth, endDay)),
                                                    adults = adultsNumber,
                                                    children = childrenNumber,
                                                    babies = babiesNumber,
                                                    color = _color.value ?: availableColors.first(),
                                                    notes = notes,
                                                    mobile = mobile,
                                                    email = email,
                                                    room = room
                                            )
                                    )
                                            .observeOn(AndroidSchedulers.mainThread())
                                            .subscribe({
                                                updateOverviewEmitter.emit()
                                                _pressBack.value = true
                                            },
                                                    {
                                                        _pressBack.value = true
                                                    })

                        }
                    })
        }
    }

    fun delete() {
        reservation?.let {
            useCase.delete(it)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe { _: Unit ->
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
        return isValid
    }

    private fun fetchRooms(preselected: Room? = null) {
        compositeDisposable.addAll(
                useCase.getAllRooms()
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe { entities: List<Room>? ->
                            entities?.let { rooms: List<Room> ->
                                _rooms.value = rooms.map { entity: Room -> entity.name }
                                preselected?.let {
                                    if (rooms.contains(it)) {
                                        _preselectedRoom.value = rooms.indexOf(it)
                                    }
                                }
                            }
                        })
    }

    override fun onCleared() {
        compositeDisposable.dispose()
    }

}