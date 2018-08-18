package com.generals.zimmerfrei.room.detail.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.support.v7.app.AppCompatActivity
import com.generals.zimmerfrei.model.Room
import com.generals.zimmerfrei.room.usecase.RoomUseCase
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class RoomDetailViewModel @Inject constructor(
    private val useCase: RoomUseCase
) : ViewModel() {

    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    private val _room: MutableLiveData<Room> = MutableLiveData()

    val room: LiveData<Room>
        get() = _room


    fun start(room: Room? = null) {
        room?.let {
            _room.value = it
        }
    }

    fun submit(
        activity: AppCompatActivity,
        name: String,
        personCount: String,
        isDouble: Boolean,
        isSingle: Boolean,
        isHandicap: Boolean,
        hasBalcony: Boolean
    ) {

        val persons: Int = try {
            personCount.toInt()
        } catch (e: NumberFormatException) {
            0
        }
        _room.value?.let {
            compositeDisposable.add(
                useCase.update(
                    it.copy(
                        name = name,
                        personsCount = persons,
                        isDouble = isDouble,
                        isSingle = isSingle,
                        isHandicap = isHandicap,
                        hasBalcony = hasBalcony
                    )
                ).subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread()).subscribe()
            )
        } ?: let {
            compositeDisposable.add(
                useCase.save(
                    Room(
                        name = name,
                        personsCount = persons,
                        isDouble = isDouble,
                        isSingle = isSingle,
                        isHandicap = isHandicap,
                        hasBalcony = hasBalcony
                    )
                ).subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread()).subscribe()
            )
        }

        activity.onBackPressed()
    }

    override fun onCleared() {
        compositeDisposable.dispose()
    }
}