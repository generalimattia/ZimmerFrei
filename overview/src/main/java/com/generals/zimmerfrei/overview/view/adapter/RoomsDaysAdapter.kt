package com.generals.zimmerfrei.overview.view.adapter

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.generals.zimmerfrei.model.Day
import com.generals.zimmerfrei.overview.view.custom.RoomDayView
import com.generals.zimmerfrei.overview.view.custom.RoomView

class RoomsDaysAdapter(
    private val days: MutableList<Day>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    init {
        setHasStableIds(true)
    }

    companion object {
        private const val ROOM = 0
        private const val ROOM_DAY = 1
    }

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): RecyclerView.ViewHolder = if (viewType == ROOM_DAY) {
        RoomDayViewHolder(RoomDayView(parent.context))
    } else {
        RoomViewHolder(RoomView(parent.context))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
    }

    override fun getItemCount(): Int = days.size + 1

    override fun getItemId(position: Int): Long =
        if (position > 0) days[position - 1].hashCode().toLong() else ROOM.toLong()

    override fun getItemViewType(position: Int): Int = if (position == 0) ROOM else ROOM_DAY

    fun update(day: Day) {
        days.add(day)
        notifyDataSetChanged()
    }

    class RoomViewHolder(
        private val view: RoomView
    ) : RecyclerView.ViewHolder(view)

    class RoomDayViewHolder(
        private val view: RoomDayView
    ) : RecyclerView.ViewHolder(view)
}