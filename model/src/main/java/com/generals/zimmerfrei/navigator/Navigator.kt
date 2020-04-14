package com.generals.zimmerfrei.navigator

import android.app.Activity
import android.content.Intent
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentTransaction
import com.generals.zimmerfrei.model.Room
import com.generals.zimmerfrei.model.RoomDay

interface Navigator {

    fun reservation(
            reservation: RoomDay? = null
    ): NavigationRequest.ActivityRequest

    fun roomList(@IdRes containerViewId: Int): NavigationRequest.FragmentRequest

    fun roomDetail(room: Room? = null): NavigationRequest.FragmentRequest

    fun customerList(): NavigationRequest.FragmentRequest

    fun customerDetail(url: String? = null): NavigationRequest.FragmentRequest

    fun email(to: String): NavigationRequest.ActivityRequest

    fun dial(number: String): NavigationRequest.ActivityRequest
}

sealed class NavigationRequest {

    data class ActivityRequest(private val intent: Intent) : NavigationRequest() {

        fun startNewActivity(activity: Activity) = activity.startActivity(intent)

    }

    sealed class FragmentRequest(private val fragment: Fragment) : NavigationRequest() {

        abstract fun start(
                activity: AppCompatActivity,
                @IdRes containerViewId: Int,
                addToBackStack: Boolean = false
        )

        abstract fun start(
                activity: FragmentActivity,
                @IdRes containerViewId: Int,
                addToBackStack: Boolean = false
        )

        data class Add(private val fragment: Fragment) : FragmentRequest(fragment) {
            override fun start(activity: AppCompatActivity, containerViewId: Int, addToBackStack: Boolean) {
                val fragmentTransaction: FragmentTransaction = activity.supportFragmentManager.beginTransaction()
                        .add(
                                containerViewId,
                                fragment
                        )
                commitTransaction(addToBackStack, fragmentTransaction)
            }

            override fun start(activity: FragmentActivity, containerViewId: Int, addToBackStack: Boolean) {
                val fragmentTransaction: FragmentTransaction = activity.supportFragmentManager.beginTransaction()
                        .add(
                                containerViewId,
                                fragment
                        )
                commitTransaction(addToBackStack, fragmentTransaction)
            }
        }

        data class Replace(private val fragment: Fragment) : FragmentRequest(fragment) {
            override fun start(activity: AppCompatActivity, containerViewId: Int, addToBackStack: Boolean) {
                val fragmentTransaction: FragmentTransaction = activity.supportFragmentManager.beginTransaction()
                        .replace(
                                containerViewId,
                                fragment
                        )
                commitTransaction(addToBackStack, fragmentTransaction)
            }

            override fun start(activity: FragmentActivity, containerViewId: Int, addToBackStack: Boolean) {
                val fragmentTransaction: FragmentTransaction = activity.supportFragmentManager.beginTransaction()
                        .replace(
                                containerViewId,
                                fragment
                        )
                commitTransaction(addToBackStack, fragmentTransaction)
            }
        }
    }
}

private fun commitTransaction(addToBackStack: Boolean, fragmentTransaction: FragmentTransaction) {
    if (addToBackStack) {
        fragmentTransaction.addToBackStack(null)
    }
    fragmentTransaction
            .commitAllowingStateLoss()
}