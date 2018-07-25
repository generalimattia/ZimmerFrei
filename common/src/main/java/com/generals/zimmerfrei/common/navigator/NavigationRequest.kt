package com.generals.zimmerfrei.common.navigator

import android.app.Activity
import android.content.Intent

data class NavigationRequest(
    private val intent: Intent
) : Navigator.Request {

    override fun start(activity: Activity) = activity.startActivity(intent)
}