package com.generals.zimmerfrei.features.overview.view.adapter

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.generals.zimmerfrei.features.overview.view.custom.DayView
import com.generals.zimmerfrei.model.Day

class DaysAdapter(private val days: List<Day>) : RecyclerView.Adapter<DaysAdapter.DayViewHolder>() {

    init {
        setHasStableIds(true)
    }

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): DayViewHolder = DayViewHolder(DayView(parent.context))

    override fun onBindViewHolder(holder: DayViewHolder, position: Int) {
        holder.bind(days[position])
    }

    override fun getItemCount(): Int = days.size

    override fun getItemId(position: Int): Long = days[position].hashCode().toLong()

    class DayViewHolder(private val view: com.generals.zimmerfrei.features.overview.view.custom.DayView) : RecyclerView.ViewHolder(
            view) {


        fun bind(day: Day) {
            view.bind(day)
        }
    }
}