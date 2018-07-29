package com.generals.zimmerfrei.overview.view.adapter

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.generals.zimmerfrei.model.Day
import com.generals.zimmerfrei.overview.view.custom.DayView
import com.generals.zimmerfrei.overview.view.custom.EmptyView

class DaysAdapter(
    private val days: MutableList<Day>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    init {
        setHasStableIds(true)
    }

    companion object {
        private const val EMPTY = 0
        private const val DAY = 1
    }

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): RecyclerView.ViewHolder = if (viewType == DAY) {
        DayViewHolder(DayView(parent.context))
    } else {
        EmptyViewHolder(EmptyView(parent.context))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        /*if (holder is DayViewHolder) {
            holder.bind(days[position - 1])
        }*/
    }

    override fun getItemCount(): Int = days.size + 1

    override fun getItemId(position: Int): Long =
        if (position > 0) days[position - 1].hashCode().toLong() else EMPTY.toLong()

    override fun getItemViewType(position: Int): Int = if (position == 0) EMPTY else DAY

    class EmptyViewHolder(
        view: EmptyView
    ) : RecyclerView.ViewHolder(view)

    class DayViewHolder(
        private val view: DayView
    ) : RecyclerView.ViewHolder(view) {
        fun bind(day: Day) {
            view.bind(day)
        }
    }
}