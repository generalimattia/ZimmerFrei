package com.generals.zimmerfrei.reservation.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import javax.inject.Inject

class ReservationViewModel @Inject constructor() : ViewModel() {

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

    fun submit() {}

}