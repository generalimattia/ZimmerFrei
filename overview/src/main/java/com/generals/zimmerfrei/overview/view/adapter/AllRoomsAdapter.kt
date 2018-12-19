package com.generals.zimmerfrei.overview.view.adapter

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.generals.zimmerfrei.model.Day
import com.generals.zimmerfrei.model.Room
import com.generals.zimmerfrei.model.RoomDay
import com.generals.zimmerfrei.overview.view.custom.TimePlan
import com.generals.zimmerfrei.overview.view.layout.SyncScroller
import java.lang.ref.WeakReference

class AllRoomsAdapter(
        private val roomDays: List<Pair<Room, List<RoomDay>>>,
        private val syncScroller: SyncScroller,
        private val onEmptyDayClick: (day: Day) -> Unit
) : RecyclerView.Adapter<AllRoomsAdapter.AllRoomsViewHolder>() {

    private var weakViewHolder: WeakReference<AllRoomsViewHolder>? = null

    init {
        setHasStableIds(true)
    }

    override fun onCreateViewHolder(
            parent: ViewGroup, viewType: Int
    ): AllRoomsViewHolder {
        val viewHolder = AllRoomsViewHolder(
                TimePlan(parent.context)
        )
        weakViewHolder = WeakReference(viewHolder)
        return viewHolder
    }

    override fun onBindViewHolder(holder: AllRoomsViewHolder, position: Int) {
        holder.bind(
                roomDays,
                syncScroller,
                onEmptyDayClick
        )
    }

    override fun getItemCount(): Int = 1

    override fun getItemId(position: Int): Long = position.toLong()

    fun update(newRoomDays: List<Pair<Room, List<RoomDay>>>) {
        weakViewHolder?.get()
                ?.update(newRoomDays)
    }

    class AllRoomsViewHolder(
            private val view: TimePlan
    ) : RecyclerView.ViewHolder(view) {

        fun bind(roomDays: List<Pair<Room, List<RoomDay>>>,
                 syncScroller: SyncScroller,
                 onEmptyDayClick: (day: Day) -> Unit) {
            view.bind(roomDays,
                    syncScroller,
                    onEmptyDayClick)
        }

        fun update(newRoomDays: List<Pair<Room, List<RoomDay>>>) {
            view.update(newRoomDays)
        }
    }
}