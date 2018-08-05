package com.generals.zimmerfrei.overview.view.adapter

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.generals.zimmerfrei.model.Day
import com.generals.zimmerfrei.model.RoomDay
import com.generals.zimmerfrei.overview.view.custom.RoomDayView

class RoomsDaysAdapter(
    roomDays: List<RoomDay>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val _days: MutableList<RoomDay> = roomDays.toMutableList()

    init {
        setHasStableIds(true)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): RecyclerView.ViewHolder = RoomDayViewHolder(RoomDayView(parent.context))


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
    }

    override fun getItemCount(): Int = _days.size

    override fun getItemId(position: Int): Long = _days[position].hashCode().toLong()


    class RoomDayViewHolder(
        private val view: RoomDayView
    ) : RecyclerView.ViewHolder(view)
}