package com.generals.zimmerfrei.common.navigator

import android.app.Activity

interface Navigator {

    fun reservation(): Request

    interface Request {

        fun start(activity: Activity)
    }
}