package com.generals.zimmerfrei.overview.view.custom

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.generals.zimmerfrei.common.extension.isWeekend
import com.generals.zimmerfrei.model.Day
import com.generals.zimmerfrei.overview.R

class DayView : FrameLayout {

    private lateinit var day: TextView

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
        LayoutInflater.from(context).inflate(R.layout.widget_day, this, true)

        day = findViewById(R.id.day)
    }

    fun bind(day: Day) {
        this.day.text = day.title

        val textColor: Int = if (day.date.isWeekend()) {
            ContextCompat.getColor(context, R.color.amber)
        } else {
            Color.WHITE
        }
        this.day.setTextColor(textColor)
    }
}
