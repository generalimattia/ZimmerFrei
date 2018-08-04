package com.generals.zimmerfrei.overview.view.custom

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.FrameLayout
import com.generals.zimmerfrei.model.Day
import com.generals.zimmerfrei.overview.R
import com.generals.zimmerfrei.overview.view.adapter.TimeAdapter
import com.generals.zimmerfrei.overview.view.layout.SyncScroller

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

        layoutParams = FrameLayout.LayoutParams(WRAP_CONTENT, MATCH_PARENT)

        recyclerView = findViewById(R.id.recycler_view)
    }

    fun bind(days: List<Day>, syncScroller: SyncScroller) {
        syncScroller.bindSecond(recyclerView)
            .sync()
        recyclerView.adapter = TimeAdapter(days)
    }

    fun moreDays(days: List<Day>) {
        (recyclerView.adapter as TimeAdapter).moreDays(days)
    }
}
