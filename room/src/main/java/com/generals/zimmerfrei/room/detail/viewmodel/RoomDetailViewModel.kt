package com.generals.zimmerfrei.room.detail.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.generals.zimmerfrei.common.resources.StringResourcesProvider
import com.generals.zimmerfrei.model.Room
import com.generals.zimmerfrei.room.R
import com.generals.zimmerfrei.room.usecase.RoomUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

class RoomDetailViewModel @Inject constructor(
        private val useCase: RoomUseCase,
        private val stringProvider: StringResourcesProvider
) : ViewModel() {

    private val _room: MutableLiveData<Room> = MutableLiveData()
    private val _pressBack: MutableLiveData<Boolean> = MutableLiveData()
    private val _errorOnName: MutableLiveData<String> = MutableLiveData()
    private val _toolbarTitle: MutableLiveData<String> = MutableLiveData()

    val room: LiveData<Room>
        get() = _room

    val pressBack: LiveData<Boolean>
        get() = _pressBack

    val errorOnName: LiveData<String>
        get() = _errorOnName

    val toolbarTitle: LiveData<String>
        get() = _toolbarTitle


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

            val persons: Int = try {
                personCount.toInt()
            } catch (e: NumberFormatException) {
                0
            }
            _room.value?.let {
                updateExistingRoom(it, name, persons, isDouble, isSingle, isHandicap, hasBalcony)
            } ?: saveNewRoom(name, persons, isDouble, isSingle, isHandicap, hasBalcony)

            _pressBack.value = true
        }
    }

    private fun saveNewRoom(
            name: String,
            persons: Int,
            isDouble: Boolean,
            isSingle: Boolean,
            isHandicap: Boolean,
            hasBalcony: Boolean
    ) {
        viewModelScope.launch {
            useCase.save(
                    Room(
                            name = name,
                            personsCount = persons,
                            isDouble = isDouble,
                            isSingle = isSingle,
                            isHandicap = isHandicap,
                            hasBalcony = hasBalcony
                    )
            )
        }
    }

    private fun updateExistingRoom(
            updated: Room,
            name: String,
            persons: Int,
            isDouble: Boolean,
            isSingle: Boolean,
            isHandicap: Boolean,
            hasBalcony: Boolean
    ) {
        viewModelScope.launch {
            useCase.update(
                    updated.copy(
                            name = name,
                            personsCount = persons,
                            isDouble = isDouble,
                            isSingle = isSingle,
                            isHandicap = isHandicap,
                            hasBalcony = hasBalcony
                    )
            )
        }
    }
}