package com.generals.zimmerfrei.navigator

import android.content.Context
import android.content.Intent
import com.generals.zimmerfrei.model.Room
import com.generals.zimmerfrei.reservation.view.ReservationActivity
import com.generals.zimmerfrei.room.detail.view.RoomDetailFragment
import javax.inject.Inject

data class NavigatorImpl @Inject constructor(
    private val context: Context
) : Navigator {

    override fun reservation(): NavigationRequest.ActivityRequest = NavigationRequest.ActivityRequest(
        Intent(
            context,
            ReservationActivity::class.java
        )
    )

    override fun room(room: Room?): NavigationRequest.FragmentRequest =
        NavigationRequest.FragmentRequest(RoomDetailFragment.newInstance(room))
}