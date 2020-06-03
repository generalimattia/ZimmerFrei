package com.generals.zimmerfrei.room.list.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.generals.zimmerfrei.listeners.ActionListener
import com.generals.zimmerfrei.listeners.ActionResult
import com.generals.zimmerfrei.model.Room
import com.generals.zimmerfrei.room.usecase.RoomUseCase
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

class RoomListViewModel @Inject constructor(
        private val useCase: RoomUseCase,
        private val roomActionListener: ActionListener<Room>
) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    private val _allRooms: MutableLiveData<List<Room>> = MutableLiveData()
    private val _result: MutableLiveData<String> = MutableLiveData()

    val allRooms: LiveData<List<Room>>
        get() = _allRooms

    val result: LiveData<String>
        get() = _result

    fun start() {
        roomActionListener.observable.subscribe(
                { result: ActionResult<Room> ->
                    if (result is ActionResult.Success) {
                        fetchRooms()
                    }
                },
                Timber::e,
                {}
        ).also { compositeDisposable.add(it) }

        fetchRooms()
    }

    private fun fetchRooms() {
        viewModelScope.launch {
            val allRooms: List<Room> = useCase.getAllRooms()
            _allRooms.value = allRooms
        }
    }

    fun onDeleteRoomClick(room: Room) {
        viewModelScope.launch {
            val result: ActionResult<Unit> = useCase.delete(room)
            if (result is ActionResult.Success) {
                fetchRooms()
                _result.value = result.message
            }
        }
    }

    override fun onCleared() {
        compositeDisposable.clear()
    }
}