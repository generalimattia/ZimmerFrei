package com.generals.zimmerfrei.common.utils

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import androidx.annotation.DrawableRes

fun buildDrawable(
        context: Context,
        color: Int,
        @DrawableRes resource: Int
): GradientDrawable {
    val drawable: Drawable? = context.getDrawable(resource)
    val gradientDrawable: GradientDrawable = drawable as GradientDrawable
    gradientDrawable.color = ColorStateList.valueOf(color)
    return gradientDrawable
}

val availableColors: List<String> = listOf(
        "#d50000",
        "#c51162",
        "#8e24aa",
        "#6200ea",
        "#283593",
        "#2962ff",
        "#0091ea",
        "#00b8d4",
        "#00695c",
        "#4caf50",
        "#8bc34a",
        "#fbc02d",
        "#ff6f00",
        "#e65100",
        "#4e342e",
        "#546e7a"
)

fun randomColor(): String = availableColors[(availableColors.indices).shuffled().first()]

fun formatDateForTextView(day: Int, month: Int, year: Int): String =
        DATE_FORMAT.format(
                day.toString(),
                month.toString(),
                year.toString()
        )

private const val DATE_FORMAT = "%s/%s/%s"