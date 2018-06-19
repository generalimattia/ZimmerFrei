package com.generals.zimmerfrei.features.overview.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.generals.zimmerfrei.features.overview.usecase.OverviewUseCase
import com.generals.overview.model.Day

import javax.inject.Inject

class OverviewViewModel @Inject constructor(private val useCase: OverviewUseCase) : ViewModel() {

    private val _days = MutableLiveData<List<Day>>()

    val days: LiveData<List<Day>>
        get() = _days

    fun start() {
        useCase.loadCalendar { list: List<Day> ->
            _days.value = list
        }
    }

    override fun onCleared() {
        super.onCleared()
    }
}