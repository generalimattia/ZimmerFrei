package com.generals.zimmerfrei.navigator

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.transition.Transition
import androidx.annotation.IdRes
import com.generals.zimmerfrei.model.ParcelableDay
import com.generals.zimmerfrei.model.Room
import com.generals.zimmerfrei.overview.view.customer.detail.CustomerDetailFragment
import com.generals.zimmerfrei.overview.view.customer.list.CustomerListFragment
import com.generals.zimmerfrei.reservation.view.RESERVATION_URL_KEY
import com.generals.zimmerfrei.reservation.view.ReservationActivity
import com.generals.zimmerfrei.reservation.view.SELECTED_DAY_KEY
import com.generals.zimmerfrei.reservation.view.SELECTED_ROOM_KEY
import com.generals.zimmerfrei.room.detail.view.RoomDetailFragment
import com.generals.zimmerfrei.room.list.view.RoomListFragment
import javax.inject.Inject

data class NavigatorImpl @Inject constructor(
        private val context: Context
) : Navigator {

    override fun reservation(
            selectedDay: ParcelableDay?,
            selectedRoom: Room?,
            reservationURL: String?
    ): NavigationRequest.ActivityRequest =
            NavigationRequest.ActivityRequest(
                    Intent(
                            context,
                            ReservationActivity::class.java
                    ).apply {
                        putExtra(SELECTED_DAY_KEY, selectedDay)
                        putExtra(SELECTED_ROOM_KEY, selectedRoom)
                        putExtra(RESERVATION_URL_KEY, reservationURL)
                    }
            )

    override fun roomList(@IdRes containerViewId: Int): NavigationRequest.FragmentRequest =
            NavigationRequest.FragmentRequest.Add(RoomListFragment.newInstance(containerViewId))

    override fun roomDetail(
            room: Room?): NavigationRequest.FragmentRequest =
            NavigationRequest.FragmentRequest.Add(RoomDetailFragment.newInstance(room))

    override fun customerList(
            enterTransition: Transition?,
            exitTransition: Transition?
    ): NavigationRequest.FragmentRequest =
            NavigationRequest.FragmentRequest.Add(CustomerListFragment.newInstance(), enterTransition, exitTransition)

    override fun customerDetail(
            url: String?,
            enterTransition: Transition?,
            exitTransition: Transition?
    ): NavigationRequest.FragmentRequest =
            NavigationRequest.FragmentRequest.Add(CustomerDetailFragment.newInstance(url), enterTransition, exitTransition)

    override fun email(to: String): NavigationRequest.ActivityRequest =
            NavigationRequest.ActivityRequest(Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:$to")
            })

    override fun dial(number: String): NavigationRequest.ActivityRequest =
            NavigationRequest.ActivityRequest(Intent(Intent.ACTION_DIAL).apply {
                data = Uri.parse("tel:$number")
            })
}