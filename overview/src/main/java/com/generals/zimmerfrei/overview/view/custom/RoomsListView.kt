package com.generals.zimmerfrei.overview.view.custom

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.recyclerview.widget.RecyclerView
import com.generals.zimmerfrei.model.Room
import com.generals.zimmerfrei.overview.R
import com.generals.zimmerfrei.overview.view.adapter.RoomsAdapter

class RoomsListView : FrameLayout {

    lateinit var recyclerView: RecyclerView

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
            .inflate(R.layout.widget_rooms_list, this, true)

        recyclerView = findViewById(R.id.recycler_view)

    }

    fun bind(rooms: List<Room>) {
        recyclerView.adapter = RoomsAdapter(rooms)
    }
}
