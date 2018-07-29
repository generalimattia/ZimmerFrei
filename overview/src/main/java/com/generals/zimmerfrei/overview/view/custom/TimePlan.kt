package com.generals.zimmerfrei.overview.view.custom

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.generals.zimmerfrei.model.Day
import com.generals.zimmerfrei.overview.R
import com.generals.zimmerfrei.overview.view.adapter.TimeAdapter

class TimePlan : FrameLayout {

    private lateinit var recyclerView: RecyclerView

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
            .inflate(R.layout.widget_time_plan, this, true)

        recyclerView = findViewById(R.id.recycler_view)

    }

    fun bind(days: MutableList<Day>) {
        recyclerView.adapter = TimeAdapter(days)
    }
}