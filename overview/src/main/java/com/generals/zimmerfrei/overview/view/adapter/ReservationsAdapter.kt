package com.generals.zimmerfrei.overview.view.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.generals.zimmerfrei.model.Room
import com.generals.zimmerfrei.model.RoomDay
import com.generals.zimmerfrei.overview.view.custom.TimePlan
import com.generals.zimmerfrei.overview.view.layout.SyncScroller

class ReservationsAdapter(
        private val roomDays: List<Pair<Room, List<RoomDay>>>,
        private val syncScroller: SyncScroller,
        private val onDayClick: (RoomDay) -> Unit
) : RecyclerView.Adapter<ReservationsAdapter.ReservationsViewHolder>() {

    init {
        setHasStableIds(true)
    }

    override fun onCreateViewHolder(
            parent: ViewGroup, viewType: Int
    ): ReservationsViewHolder =
            ReservationsViewHolder(TimePlan(parent.context))

    override fun onBindViewHolder(holder: ReservationsViewHolder, position: Int) {
        holder.bind(
                roomDays,
                syncScroller,
                onDayClick
        )
    }

    override fun getItemCount(): Int = 1

    override fun getItemId(position: Int): Long = position.toLong()

    class ReservationsViewHolder(
            private val view: TimePlan
    ) : RecyclerView.ViewHolder(view) {

        fun bind(roomDays: List<Pair<Room, List<RoomDay>>>,
                 syncScroller: SyncScroller,
                 onDayClick: (RoomDay) -> Unit) {
            view.bind(roomDays,
                    syncScroller,
                    onDayClick)
        }
    }
}