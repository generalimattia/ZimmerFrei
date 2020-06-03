package com.generals.zimmerfrei.room.detail.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.generals.zimmerfrei.common.resources.StringResourcesProvider
import com.generals.zimmerfrei.common.utils.safeToInt
import com.generals.zimmerfrei.listeners.ActionEmitter
import com.generals.zimmerfrei.listeners.ActionResult
import com.generals.zimmerfrei.model.Room
import com.generals.zimmerfrei.room.R
import com.generals.zimmerfrei.room.usecase.RoomUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

class RoomDetailViewModel @Inject constructor(
        private val useCase: RoomUseCase,
        private val stringProvider: StringResourcesProvider,
        private val roomActionEmitter: ActionEmitter<Room>
) : ViewModel() {

    private val _room: MutableLiveData<Room> = MutableLiveData()
    private val _pressBack: MutableLiveData<Boolean> = MutableLiveData()
    private val _errorOnName: MutableLiveData<String> = MutableLiveData()
    private val _toolbarTitle: MutableLiveData<String> = MutableLiveData()
    private val _message: MutableLiveData<String> = MutableLiveData()

    val room: LiveData<Room>
        get() = _room

    val pressBack: LiveData<Boolean>
        get() = _pressBack

    val errorOnName: LiveData<String>
        get() = _errorOnName

    val toolbarTitle: LiveData<String>
        get() = _toolbarTitle

    val message: LiveData<String>
        get() = _message


    fun start(room: Room? = null) {
        room?.let {
            _room.value = it
            _toolbarTitle.value = "${stringProvider.provide(R.string.room)} ${it.name}"
        } ?: let {
            _toolbarTitle.value = stringProvider.provide(R.string.new_room)
        }
    }

    fun submit(
            name: String,
            personCount: String,
            isDouble: Boolean,
            isSingle: Boolean,
            isHandicap: Boolean,
            hasBalcony: Boolean
    ) {

        if (name.isBlank()) {
            _errorOnName.value = stringProvider.provide(R.string.mandatory_field)
        } else {
            viewModelScope.launch {
                val room: Room? = _room.value
                val result: ActionResult<Room> = if (room != null) {
                    useCase.update(
                            room.copy(
                                    name = name,
                                    personsCount = personCount.safeToInt(),
                                    isDouble = isDouble,
                                    isSingle = isSingle,
                                    isHandicap = isHandicap,
                                    hasBalcony = hasBalcony
                            )
                    )
                } else {
                    useCase.save(
                            Room(
                                    name = name,
                                    personsCount = personCount.safeToInt(),
                                    isDouble = isDouble,
                                    isSingle = isSingle,
                                    isHandicap = isHandicap,
                                    hasBalcony = hasBalcony
                            )
                    )
                }

                handleResult(result)
            }
        }
    }

    private fun handleResult(result: ActionResult<Room>) {
        if (result is ActionResult.Error) {
            _message.value = result.message
        } else {
            roomActionEmitter.emit(result)
            _pressBack.value = true
        }
    }
}