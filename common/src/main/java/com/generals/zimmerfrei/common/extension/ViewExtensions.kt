package com.generals.zimmerfrei.common.extension

import android.view.View
import android.app.Activity
import android.view.inputmethod.InputMethodManager


fun View.hideKeyboard() {
    val inputMethodManager: InputMethodManager = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
}