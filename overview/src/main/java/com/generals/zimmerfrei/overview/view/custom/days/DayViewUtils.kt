package com.generals.zimmerfrei.overview.view.custom.days

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import androidx.annotation.DrawableRes
import com.generals.zimmerfrei.model.Reservation
import com.generals.zimmerfrei.model.RoomDay
import com.generals.zimmerfrei.overview.R

fun buildBackgroundDrawable(context: Context, roomDay: RoomDay): GradientDrawable? =
        when (roomDay) {
            is RoomDay.Reserved -> buildDrawable(context, roomDay.reservation, R.drawable.background_reserved_shape)
            is RoomDay.StartingReservation -> buildDrawable(context, roomDay.reservation, R.drawable.left_half_circle)
            is RoomDay.EndingReservation -> buildDrawable(context, roomDay.reservation, R.drawable.right_half_circle)
            else -> null
        }

private fun buildDrawable(
        context: Context,
        reservation: Reservation,
        @DrawableRes resource: Int
): GradientDrawable = buildDrawable(context, Color.parseColor(reservation.color), resource)

fun buildDrawable(
        context: Context,
        color: Int,
        @DrawableRes resource: Int
): GradientDrawable {
    val drawable = context.getDrawable(resource)
    val gradientDrawable = drawable as GradientDrawable
    gradientDrawable.color = ColorStateList.valueOf(color)
    return gradientDrawable
}