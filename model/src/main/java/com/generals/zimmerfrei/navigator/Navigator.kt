package com.generals.zimmerfrei.navigator

import android.app.Activity
import android.content.Intent
import android.support.annotation.IdRes
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.AppCompatActivity
import com.generals.zimmerfrei.model.Day
import com.generals.zimmerfrei.model.Room

interface Navigator {

    fun reservation(
            startDate: Day? = null
    ): NavigationRequest.ActivityRequest

    fun roomList(@IdRes containerViewId: Int): NavigationRequest.FragmentRequest

    fun roomDetail(room: Room? = null): NavigationRequest.FragmentRequest
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

            if(addToBackStack) {
                fragmentTransaction.addToBackStack(null)
            }

            fragmentTransaction
                .commitAllowingStateLoss()
        }

    }
}