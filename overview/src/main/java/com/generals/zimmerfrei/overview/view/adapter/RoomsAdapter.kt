package com.generals.zimmerfrei.overview.view.adapter

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.generals.zimmerfrei.model.Day
import com.generals.zimmerfrei.overview.view.custom.RoomView

class RoomsAdapter(
    private val days: MutableList<Day>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    init {
        setHasStableIds(true)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): RecyclerView.ViewHolder = RoomViewHolder(RoomView(parent.context))


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        /*if (holder is DayViewHolder) {
            holder.bind(days[position - 1])
        }*/
    }

    override fun getItemCount(): Int = days.size

    override fun getItemId(position: Int): Long = days[position].hashCode().toLong()


    class RoomViewHolder(
        private val view: RoomView
    ) : RecyclerView.ViewHolder(view)
}