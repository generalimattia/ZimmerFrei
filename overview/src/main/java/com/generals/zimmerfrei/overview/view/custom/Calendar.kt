package com.generals.zimmerfrei.overview.view.custom

import android.content.Context
import android.content.res.Configuration
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.generals.zimmerfrei.overview.R

class Calendar : FrameLayout {

    private lateinit var recyclerView: RecyclerView

    private val orientationSpanMap: Map<Int, Int> = mapOf(
            Configuration.ORIENTATION_UNDEFINED to 4,
            Configuration.ORIENTATION_PORTRAIT to 4,
            Configuration.ORIENTATION_LANDSCAPE to 7
    )

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
                .inflate(R.layout.widget_calendar, this, true)

        recyclerView = findViewById(R.id.recycler_view)

        recyclerView.layoutManager = GridLayoutManager(
                context,
                orientationSpanMap[resources.configuration.orientation] ?: 4
        )

    }

    /*fun bind(days: MutableList<Day>) {
        recyclerView.adapter = RoomsDaysAdapter(days)
    }*/
}
