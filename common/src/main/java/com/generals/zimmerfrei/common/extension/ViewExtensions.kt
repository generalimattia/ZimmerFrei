package com.generals.zimmerfrei.common.extension

import android.view.View
import android.app.Activity
import android.graphics.Color
import android.view.inputmethod.InputMethodManager

fun View.hideKeyboard() {
    val inputMethodManager: InputMethodManager = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
}

fun String.toColor(): Int = try {
    Color.parseColor(this)
} catch (e: Exception) {
    Color.CYAN
}