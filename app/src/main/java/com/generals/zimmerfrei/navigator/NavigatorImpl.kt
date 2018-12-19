package com.generals.zimmerfrei.navigator

import android.content.Context
import android.content.Intent
import android.support.annotation.IdRes
import com.generals.zimmerfrei.model.ParcelableRoomDay
import com.generals.zimmerfrei.model.Room
import com.generals.zimmerfrei.model.RoomDay
import com.generals.zimmerfrei.reservation.view.RESERVATION_START_DATE
import com.generals.zimmerfrei.reservation.view.ReservationActivity
import com.generals.zimmerfrei.room.detail.view.RoomDetailFragment
import com.generals.zimmerfrei.room.list.view.RoomListFragment
import javax.inject.Inject

data class NavigatorImpl @Inject constructor(
        private val context: Context
) : Navigator {

    override fun reservation(
            reservation: RoomDay?
    ): NavigationRequest.ActivityRequest =
            NavigationRequest.ActivityRequest(
                    Intent(
                            context,
                            ReservationActivity::class.java
                    ).apply {
                        reservation?.let {
                            when (it) {
                                is RoomDay.Empty -> putExtra(RESERVATION_START_DATE, ParcelableRoomDay.Empty(it))
                                else -> {}
                            }
                        }
                    }
            )

    override fun roomList(@IdRes containerViewId: Int): NavigationRequest.FragmentRequest =
            NavigationRequest.FragmentRequest(RoomListFragment.newInstance(containerViewId))

    override fun roomDetail(room: Room?): NavigationRequest.FragmentRequest =
            NavigationRequest.FragmentRequest(RoomDetailFragment.newInstance(room))
}