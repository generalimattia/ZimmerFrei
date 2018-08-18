package com.generals.zimmerfrei.room.list.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.support.annotation.IdRes
import android.support.v7.app.AppCompatActivity
import com.generals.zimmerfrei.model.Room
import com.generals.zimmerfrei.navigator.Navigator
import com.generals.zimmerfrei.room.usecase.RoomUseCase
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class RoomListViewModel @Inject constructor(
    private val useCase: RoomUseCase, private val navigator: Navigator
) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    private val _allRooms: MutableLiveData<List<Room>> = MutableLiveData()

    val allRooms: LiveData<List<Room>>
        get() = _allRooms

    fun start() {

        fetchRooms()
    }

    private fun fetchRooms() {
        compositeDisposable.add(useCase.getAllRooms().subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread()).subscribe { rooms: List<Room>? ->
            rooms?.let {
                _allRooms.value = it
            }
        })
    }

    fun onFABClick(activity: AppCompatActivity, @IdRes containerViewId: Int) {
        navigator.roomDetail()
            .startNewFragment(
                activity,
                containerViewId,
                true
            )
    }

    fun onRoomClick(activity: AppCompatActivity, @IdRes containerViewId: Int, room: Room) {
        navigator.roomDetail(room)
            .startNewFragment(
                activity,
                containerViewId,
                true
            )
    }

    fun onDeleteRoomClick(room: Room) {
        useCase.delete(room)
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe()

        fetchRooms()
    }

    override fun onCleared() {
        compositeDisposable.dispose()
    }
}