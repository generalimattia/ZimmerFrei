package com.generals.zimmerfrei.navigator

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.annotation.IdRes
import com.generals.zimmerfrei.model.ParcelableRoomDay
import com.generals.zimmerfrei.model.Room
import com.generals.zimmerfrei.model.RoomDay
import com.generals.zimmerfrei.reservation.view.RESERVATION
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
                        val parcelableRoomDay: ParcelableRoomDay? = reservation?.let {
                            when (it) {
                                is RoomDay.Empty -> ParcelableRoomDay.Empty(it)
                                is RoomDay.EmptyWeekend -> ParcelableRoomDay.Empty(it)
                                is RoomDay.Reserved -> ParcelableRoomDay.Reserved(it)
                                is RoomDay.StartingReservation -> ParcelableRoomDay.Reserved(it)
                                is RoomDay.EndingReservation -> ParcelableRoomDay.Reserved(it)
                            }
                        }
                        putExtra(RESERVATION, parcelableRoomDay)
                    }
            )

    override fun roomList(@IdRes containerViewId: Int): NavigationRequest.FragmentRequest =
            NavigationRequest.FragmentRequest(RoomListFragment.newInstance(containerViewId))

    override fun roomDetail(room: Room?): NavigationRequest.FragmentRequest =
            NavigationRequest.FragmentRequest(RoomDetailFragment.newInstance(room))

    override fun email(to: String): NavigationRequest.ActivityRequest =
            NavigationRequest.ActivityRequest(Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:$to")
            })

    override fun dial(number: String): NavigationRequest.ActivityRequest =
            NavigationRequest.ActivityRequest(Intent(Intent.ACTION_DIAL).apply {
                data = Uri.parse("tel:$number")
            })
}