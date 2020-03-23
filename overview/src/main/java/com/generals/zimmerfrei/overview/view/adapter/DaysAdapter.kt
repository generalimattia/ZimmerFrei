package com.generals.zimmerfrei.overview.view.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.generals.zimmerfrei.model.Day
import com.generals.zimmerfrei.overview.view.custom.DayView
import com.generals.zimmerfrei.overview.view.custom.EmptyView

class DaysAdapter(
    days: List<Day>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val _days: MutableList<Day> = days.toMutableList()

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
        if (holder is DayViewHolder) {
            holder.bind(_days[position - 1])
        }
    }

    override fun getItemCount(): Int = _days.size + 1

    override fun getItemId(position: Int): Long =
        if (position > 0) _days[position - 1].hashCode().toLong() else EMPTY.toLong()

    override fun getItemViewType(position: Int): Int = if (position == 0) EMPTY else DAY

    fun moreDays(moreDays: List<Day>) {
        _days.addAll(moreDays)
        notifyDataSetChanged()
    }

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