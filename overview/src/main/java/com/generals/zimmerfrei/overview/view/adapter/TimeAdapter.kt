package com.generals.zimmerfrei.overview.view.adapter

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.generals.zimmerfrei.model.Day
import com.generals.zimmerfrei.overview.view.custom.DaysListView
import com.generals.zimmerfrei.overview.view.custom.SingleRoomPlan

class TimeAdapter(
    private val days: MutableList<Day>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    init {
        setHasStableIds(true)
    }

    companion object {
        private const val DAY = 0
        private const val ROOM = 1
    }

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): RecyclerView.ViewHolder = if (viewType == DAY) {
        DaysViewHolder(DaysListView(parent.context))
    } else {
        SingleRoomViewHolder(SingleRoomPlan(parent.context))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is SingleRoomViewHolder) {
            holder.bind(days)
        } else if (holder is DaysViewHolder) {
            holder.bind(days)
        }
    }

    override fun getItemCount(): Int = days.size + 1

    override fun getItemId(position: Int): Long =
        if (position > 0) days[position - 1].hashCode().toLong() else DAY.toLong()

    override fun getItemViewType(position: Int): Int = if (position > 0) ROOM else DAY

    class DaysViewHolder(
        private val view: DaysListView
    ) : RecyclerView.ViewHolder(view) {

        fun bind(days: MutableList<Day>) {
            view.bind(days)
        }
    }

    class SingleRoomViewHolder(
        private val view: SingleRoomPlan
    ) : RecyclerView.ViewHolder(view) {

        fun bind(days: MutableList<Day>) {
            view.bind(days)
        }
    }
}