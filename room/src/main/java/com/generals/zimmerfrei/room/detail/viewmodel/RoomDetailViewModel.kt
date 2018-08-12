package com.generals.zimmerfrei.room.detail.viewmodel

import android.arch.lifecycle.ViewModel
import com.generals.zimmerfrei.room.usecase.RoomUseCase
import javax.inject.Inject

class RoomDetailViewModel @Inject constructor(
    private val useCase: RoomUseCase
) : ViewModel() {

    fun submit() {
        //useCase.save()
    }
}