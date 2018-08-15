package com.generals.zimmerfrei.room.detail.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.generals.zimmerfrei.model.Room
import com.generals.zimmerfrei.room.usecase.RoomUseCase
import javax.inject.Inject

class RoomDetailViewModel @Inject constructor(
    private val useCase: RoomUseCase
) : ViewModel() {

    private val _room: MutableLiveData<Room> = MutableLiveData()

    val room: LiveData<Room>
        get() = _room


    fun start(room: Room?) {
        room?.let {
            _room.value = it
        }
    }

    fun submit() {
        //useCase.save()
    }
}