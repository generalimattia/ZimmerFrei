package com.generals.zimmerfrei.overview.view.adapter

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.generals.zimmerfrei.model.Day
import com.generals.zimmerfrei.overview.view.custom.SingleRoomPlan
import java.lang.ref.WeakReference

class TimeAdapter(
    private val days: List<Day>
) : RecyclerView.Adapter<TimeAdapter.SingleRoomViewHolder>() {

    private val holders: MutableList<WeakReference<SingleRoomViewHolder>> = mutableListOf()

    init {
        setHasStableIds(true)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): SingleRoomViewHolder {
        val holder = SingleRoomViewHolder(SingleRoomPlan(parent.context))
        holders.add(WeakReference(holder))
        return holder
    }

    override fun onBindViewHolder(holder: SingleRoomViewHolder, position: Int) {
        holder.bind(days)
    }

    override fun getItemCount(): Int = days.size

    override fun getItemId(position: Int): Long = days[position].hashCode().toLong()

    fun moreDays(days: List<Day>) {
        holders.forEach {
            it.get()
                ?.moreDays(days)

        }
    }

    class SingleRoomViewHolder(
        private val view: SingleRoomPlan
    ) : RecyclerView.ViewHolder(view) {

        fun bind(days: List<Day>) {
            view.bind(days)
        }

        fun moreDays(days: List<Day>) {
            view.moreDays(days)
        }
    }
}