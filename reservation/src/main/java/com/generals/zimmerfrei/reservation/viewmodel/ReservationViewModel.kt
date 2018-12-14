package com.generals.zimmerfrei.reservation.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.generals.zimmerfrei.common.extension.offsetDateTimeFromLocalDate
import com.generals.zimmerfrei.model.Reservation
import com.generals.zimmerfrei.model.Room
import com.generals.zimmerfrei.reservation.usecase.ReservationUseCase
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.threeten.bp.LocalDate
import org.threeten.bp.OffsetDateTime
import org.threeten.bp.ZoneId
import org.threeten.bp.format.DateTimeFormatter
import javax.inject.Inject

class ReservationViewModel @Inject constructor(
    private val useCase: ReservationUseCase
) : ViewModel() {

    companion object {
        private const val DATE_FORMAT = "dd/MM/yyyy"
        private val dateFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern(DATE_FORMAT).withZone(ZoneId.of("Europe/Rome"))
    }

    private val _color: MutableLiveData<String> = MutableLiveData()

    private val _pressBack: MutableLiveData<Boolean> = MutableLiveData()

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

    fun generateNewColor() {
        val colorsCount = availableColors.size
        _color.value = availableColors[(0 until colorsCount).shuffled().first()]
    }

    fun start() {
        generateNewColor()
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

        useCase.getRoomByName(roomName)
            .subscribeOn(Schedulers.newThread())
            .subscribe { room: Room? ->
                room?.let {
                    useCase.save(
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
                                       _pressBack.value = true
                                   },
                                   {
                                       _pressBack.value = true
                                   })
                }
            }
    }

}