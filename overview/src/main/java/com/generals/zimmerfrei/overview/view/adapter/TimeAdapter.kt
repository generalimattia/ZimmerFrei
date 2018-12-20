package com.generals.zimmerfrei.overview.view.adapter

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.generals.zimmerfrei.model.Room
import com.generals.zimmerfrei.model.RoomDay
import com.generals.zimmerfrei.overview.view.custom.SingleRoomPlan

class TimeAdapter(
        roomDays: List<Pair<Room, List<RoomDay>>>,
        private val onDayClick: (RoomDay) -> Unit
) : RecyclerView.Adapter<TimeAdapter.SingleRoomViewHolder>() {

    private val _roomDays: MutableList<Pair<Room, List<RoomDay>>> = roomDays.toMutableList()

    init {
        setHasStableIds(true)
    }

    override fun onCreateViewHolder(
            parent: ViewGroup, viewType: Int
    ): SingleRoomViewHolder = SingleRoomViewHolder(SingleRoomPlan(parent.context))

    override fun onBindViewHolder(holder: SingleRoomViewHolder, position: Int) {
        holder.bind(_roomDays[position].second, onDayClick)
    }

    override fun getItemCount(): Int = _roomDays.size

    override fun getItemId(position: Int): Long = _roomDays[position].first.hashCode().toLong()

    class SingleRoomViewHolder(
            private val view: SingleRoomPlan
    ) : RecyclerView.ViewHolder(view) {

        fun bind(days: List<RoomDay>,
                 onDayClick: (RoomDay) -> Unit) {
            view.bind(days, onDayClick)
        }
    }
}