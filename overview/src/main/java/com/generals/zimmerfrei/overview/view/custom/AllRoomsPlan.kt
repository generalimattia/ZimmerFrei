package com.generals.zimmerfrei.overview.view.custom

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.generals.zimmerfrei.model.Day
import com.generals.zimmerfrei.model.Room
import com.generals.zimmerfrei.model.RoomDay
import com.generals.zimmerfrei.overview.R
import com.generals.zimmerfrei.overview.view.adapter.AllRoomsAdapter
import com.generals.zimmerfrei.overview.view.layout.SyncScroller

class AllRoomsPlan : FrameLayout {

    lateinit var recyclerView: RecyclerView

    constructor(context: Context) : super(context) {
        init(
                null,
                0
        )
    }

    constructor(context: Context, attrs: AttributeSet) : super(
            context,
            attrs
    ) {
        init(
                attrs,
                0
        )
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(
            context,
            attrs,
            defStyle
    ) {
        init(
                attrs,
                defStyle
        )
    }

    private fun init(attrs: AttributeSet?, defStyle: Int) {
        LayoutInflater.from(context)
                .inflate(
                        R.layout.widget_all_rooms_plan,
                        this,
                        true
                )

        recyclerView = findViewById(R.id.recycler_view)

    }

    fun bind(roomDays: List<Pair<Room, List<RoomDay>>>,
             syncScroller: SyncScroller,
             onEmptyDayClick: (day: Day) -> Unit) {
        recyclerView.adapter = AllRoomsAdapter(
                roomDays,
                syncScroller,
                onEmptyDayClick
        )
    }

    fun update(newRoomDays: List<Pair<Room, List<RoomDay>>>) {
        (recyclerView.adapter as AllRoomsAdapter).update(newRoomDays)
    }
}
