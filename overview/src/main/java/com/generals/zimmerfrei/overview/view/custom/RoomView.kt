package com.generals.zimmerfrei.overview.view.custom

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.TextView
import com.generals.zimmerfrei.model.Day
import com.generals.zimmerfrei.overview.R

class RoomView : FrameLayout {

    private lateinit var room: TextView

    constructor(context: Context) : super(context) {
        init(null, 0)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(attrs, 0)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(
        context, attrs, defStyle
    ) {
        init(attrs, defStyle)
    }

    private fun init(attrs: AttributeSet?, defStyle: Int) {
        LayoutInflater.from(context)
            .inflate(R.layout.widget_room, this, true)

        room = findViewById(R.id.room)
    }

    fun bind(day: Day) {
        this.room.text = day.title
    }
}
