package com.generals.zimmerfrei.overview.view.adapter

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.generals.zimmerfrei.overview.model.Day
import com.generals.zimmerfrei.overview.view.custom.DayView

class DaysAdapter(
    private val days: MutableList<Day>
) : RecyclerView.Adapter<DaysAdapter.DayViewHolder>() {

    init {
        setHasStableIds(true)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): DayViewHolder = DayViewHolder(
        DayView(parent.context)
    )

    override fun onBindViewHolder(holder: DayViewHolder, position: Int) {
        holder.bind(days[position])
    }

    override fun getItemCount(): Int = days.size

    override fun getItemId(position: Int): Long = days[position].hashCode().toLong()

    fun update(day: Day) {
        days.add(day)
        notifyDataSetChanged()
    }

    class DayViewHolder(
        private val view: DayView
    ) : RecyclerView.ViewHolder(view) {
        fun bind(day: Day) {
            view.bind(day)
        }
    }
}