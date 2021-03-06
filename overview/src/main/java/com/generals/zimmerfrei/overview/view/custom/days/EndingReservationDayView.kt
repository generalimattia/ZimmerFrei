package com.generals.zimmerfrei.overview.view.custom.days

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.generals.zimmerfrei.model.RoomDay
import com.generals.zimmerfrei.overview.R

class EndingReservationDayView : FrameLayout {

    constructor(context: Context) : super(context) {
        init(null, 0)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(attrs, 0)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context,
            attrs,
            defStyle) {
        init(attrs, defStyle)
    }

    private fun init(attrs: AttributeSet?, defStyle: Int) {
        LayoutInflater.from(context).inflate(R.layout.widget_reserved_day, this, true)
    }

    fun bind(
            roomDay: RoomDay.EndingReservation,
            onClick: (RoomDay) -> Unit
    ) {
        buildBackgroundDrawable(context, roomDay) ?.let {
            background = it
        }
        setOnClickListener {
            onClick(roomDay)
        }
    }
}
