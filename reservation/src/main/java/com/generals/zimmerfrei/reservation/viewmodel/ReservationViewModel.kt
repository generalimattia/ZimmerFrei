package com.generals.zimmerfrei.reservation.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.generals.zimmerfrei.model.Reservation
import com.generals.zimmerfrei.model.Room
import com.generals.zimmerfrei.reservation.usecase.ReservationUseCase
import org.threeten.bp.OffsetDateTime
import org.threeten.bp.format.DateTimeFormatter
import javax.inject.Inject

class ReservationViewModel @Inject constructor(
    private val useCase: ReservationUseCase
) : ViewModel() {

    companion object {
        private const val DATE_FORMAT = "dd/MM/yyyy"
    }

    private val _color: MutableLiveData<String> = MutableLiveData()

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

    fun generateNewColor() {
        val colorsCount = availableColors.size
        _color.value = availableColors[(0 until colorsCount).shuffled().first()]
    }

    fun start() {
        generateNewColor()
    }

    fun setStartDate(year: Int, month: Int, day: Int) {}

    fun setEndDate(year: Int, month: Int, day: Int) {}

    fun submit(
        name: String,
        startDate: String,
        endDate: String,
        adults: String,
        children: String,
        babies: String,
        room: String,
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

        useCase.save(
            Reservation(
                name = name,
                startDate = OffsetDateTime.parse(
                    startDate,
                    DateTimeFormatter.ofPattern(DATE_FORMAT)
                ),
                endDate = OffsetDateTime.parse(
                    endDate,
                    DateTimeFormatter.ofPattern(DATE_FORMAT)
                ),
                adults = adultsNumber,
                children = childrenNumber,
                babies = babiesNumber,
                color = _color.value ?: availableColors.first(),
                room = Room(room),
                notes = notes,
                mobile = mobile,
                email = email
            )
        )
    }

}