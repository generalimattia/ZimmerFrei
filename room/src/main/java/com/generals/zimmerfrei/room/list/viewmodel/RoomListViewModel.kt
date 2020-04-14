package com.generals.zimmerfrei.room.list.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.generals.zimmerfrei.model.Room
import com.generals.zimmerfrei.room.usecase.RoomUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

class RoomListViewModel @Inject constructor(
        private val useCase: RoomUseCase
) : ViewModel() {

    private val _allRooms: MutableLiveData<List<Room>> = MutableLiveData()

    val allRooms: LiveData<List<Room>>
        get() = _allRooms

    fun start() {
        fetchRooms()
    }

    private fun fetchRooms() {
        viewModelScope.launch {
            val allRooms: List<Room> = useCase.getAllRooms()
            _allRooms.value = allRooms
        }
    }

    fun onDeleteRoomClick(room: Room) {
        viewModelScope.launch { useCase.delete(room) }
        fetchRooms()
    }
}