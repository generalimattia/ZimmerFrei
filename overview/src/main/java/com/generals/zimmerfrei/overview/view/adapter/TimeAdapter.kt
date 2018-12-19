package com.generals.zimmerfrei.overview.view.adapter

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.generals.zimmerfrei.model.Day
import com.generals.zimmerfrei.model.Room
import com.generals.zimmerfrei.model.RoomDay
import com.generals.zimmerfrei.overview.view.custom.SingleRoomPlan

class TimeAdapter(
        roomDays: List<Pair<Room, List<RoomDay>>>,
        private val onEmptyDayClick: (day: Day) -> Unit
) : RecyclerView.Adapter<TimeAdapter.SingleRoomViewHolder>() {

    private val _roomDays: MutableList<Pair<Room, List<RoomDay>>> = roomDays.toMutableList()

    init {
        setHasStableIds(true)
    }

    override fun onCreateViewHolder(
            parent: ViewGroup, viewType: Int
    ): SingleRoomViewHolder = SingleRoomViewHolder(SingleRoomPlan(parent.context))

    override fun onBindViewHolder(holder: SingleRoomViewHolder, position: Int) {
        holder.bind(_roomDays[position].second, onEmptyDayClick)
    }

    override fun getItemCount(): Int = _roomDays.size

    override fun getItemId(position: Int): Long = _roomDays[position].first.hashCode().toLong()

    fun update(newRoomDays: List<Pair<Room, List<RoomDay>>>) {
        _roomDays.clear()
        _roomDays.addAll(newRoomDays)
        notifyDataSetChanged()
    }

    class SingleRoomViewHolder(
            private val view: SingleRoomPlan
    ) : RecyclerView.ViewHolder(view) {

        fun bind(days: List<RoomDay>,
                 onEmptyDayClick: (day: Day) -> Unit) {
            view.bind(days, onEmptyDayClick)
        }
    }
}