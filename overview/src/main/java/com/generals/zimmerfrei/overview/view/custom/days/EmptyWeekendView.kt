package com.generals.zimmerfrei.overview.view.custom.days

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import com.generals.zimmerfrei.common.extension.isWeekend
import com.generals.zimmerfrei.model.RoomDay
import com.generals.zimmerfrei.overview.R

class EmptyWeekendView : FrameLayout {

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
        LayoutInflater.from(context).inflate(R.layout.widget_empty_day_weekend, this, true)
    }

    fun bind(roomDay: RoomDay.EmptyWeekend,
             onClick: (day: RoomDay) -> Unit) {
        setOnClickListener {
            onClick(roomDay)
        }
    }
}
