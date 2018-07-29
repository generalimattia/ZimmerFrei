package com.generals.zimmerfrei.overview.view.adapter

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.generals.zimmerfrei.model.Day
import com.generals.zimmerfrei.overview.view.custom.TimePlan

class AllRoomsAdapter(
    private val days: MutableList<Day>
) : RecyclerView.Adapter<AllRoomsAdapter.AllRoomsViewHolder>() {

    init {
        setHasStableIds(true)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): AllRoomsViewHolder = AllRoomsViewHolder(
        TimePlan(parent.context)
    )

    override fun onBindViewHolder(holder: AllRoomsViewHolder, position: Int) {
        holder.bind(days)
    }

    override fun getItemCount(): Int = 1

    override fun getItemId(position: Int): Long = position.toLong()

    class AllRoomsViewHolder(
        private val view: TimePlan
    ) : RecyclerView.ViewHolder(view) {

        fun bind(days: MutableList<Day>) {
            view.bind(days)
        }
    }
}