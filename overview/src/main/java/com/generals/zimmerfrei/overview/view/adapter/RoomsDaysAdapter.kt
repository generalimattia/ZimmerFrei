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

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): RecyclerView.ViewHolder =  RoomDayViewHolder(RoomDayView(parent.context))


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
    }

    override fun getItemCount(): Int = days.size

    override fun getItemId(position: Int): Long = days[position].hashCode().toLong()

    fun update(day: Day) {
        days.add(day)
        notifyDataSetChanged()
    }


    class RoomDayViewHolder(
        private val view: RoomDayView
    ) : RecyclerView.ViewHolder(view)
}