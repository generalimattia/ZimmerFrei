package com.generals.zimmerfrei.navigator

import android.app.Activity
import android.content.Intent
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.generals.zimmerfrei.model.Room
import com.generals.zimmerfrei.model.RoomDay

interface Navigator {

    fun reservation(
            reservation: RoomDay? = null
    ): NavigationRequest.ActivityRequest

    fun roomList(@IdRes containerViewId: Int): NavigationRequest.FragmentRequest

    fun roomDetail(room: Room? = null): NavigationRequest.FragmentRequest

    fun email(to: String): NavigationRequest.ActivityRequest

    fun dial(number: String): NavigationRequest.ActivityRequest
}

sealed class NavigationRequest {

    data class ActivityRequest(private val intent: Intent) : NavigationRequest() {

        fun startNewActivity(activity: Activity) = activity.startActivity(intent)

    }

    data class FragmentRequest(private val fragment: Fragment) : NavigationRequest() {

        fun startNewFragment(
                activity: AppCompatActivity,
                @IdRes containerViewId: Int,
                addToBackStack: Boolean = false
        ) {
            val fragmentTransaction: FragmentTransaction = activity.supportFragmentManager.beginTransaction()
                    .replace(
                            containerViewId,
                            fragment
                    )

            if (addToBackStack) {
                fragmentTransaction.addToBackStack(null)
            }

            fragmentTransaction
                    .commitAllowingStateLoss()
        }

    }
}