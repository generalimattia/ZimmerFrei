package com.generals.zimmerfrei.navigator

import android.content.Context
import android.content.Intent
import com.generals.zimmerfrei.common.navigator.NavigationRequest
import com.generals.zimmerfrei.common.navigator.Navigator
import com.generals.zimmerfrei.reservation.view.ReservationActivity
import javax.inject.Inject

data class NavigatorImpl @Inject constructor(
    private val context: Context
): Navigator {

    override fun reservation(): Navigator.Request {
        val intent = Intent(context, ReservationActivity::class.java)
        return NavigationRequest(intent)
    }
}