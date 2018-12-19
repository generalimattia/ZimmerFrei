package com.generals.zimmerfrei.reservation.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.generals.zimmerfrei.common.extension.offsetDateTimeFromLocalDate
import com.generals.zimmerfrei.common.resources.StringResourcesProvider
import com.generals.zimmerfrei.model.ParcelableDay
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
        private val stringProvider: StringResourcesProvider
) : ViewModel() {

    private var roomPosition = 0

    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    private val _color = MutableLiveData<String>()
    private val _pressBack = MutableLiveData<Boolean>()
    private val _roomError = MutableLiveData<String>()
    private val _nameError = MutableLiveData<String>()
    private val _startDateError = MutableLiveData<String>()
    private val _endDateError = MutableLiveData<String>()
    private val _rooms = MutableLiveData<List<String>>()
    private val _selectedRoom = MutableLiveData<String>()
    private val _startDate = MutableLiveData<ParcelableDay>()

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

    val color: LiveData<String>
        get() = _color

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

    val startDay: LiveData<ParcelableDay>
        get() = _startDate

    fun start(
            startDay: ParcelableDay?
    ) {
        _startDate.value = startDay

        generateNewColor()

        fetchRooms()
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

    fun submit(
            name: String,
            startDay: Int,
            startMonth: Int,
            startYear: Int,
            endDay: Int,
            endMonth: Int,
            endYear: Int,
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
                            useCase.save(
                                    Reservation(
                                            name = name,
                                            startDate = offsetDateTimeFromLocalDate(LocalDate.of(startYear, startMonth + 1, startDay)),
                                            endDate = offsetDateTimeFromLocalDate(LocalDate.of(endYear, endMonth + 1, endDay)),
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
                                        _pressBack.value = true
                                    },
                                            {
                                                _pressBack.value = true
                                            })
                        }
                    })
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

    private fun fetchRooms() {
        compositeDisposable.addAll(
                useCase.getAllRooms()
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe { entities: List<Room>? ->
                            entities?.let {
                                _rooms.value = it.map { entity: Room -> entity.name }
                            }
                        })
    }

    override fun onCleared() {
        compositeDisposable.dispose()
    }

}