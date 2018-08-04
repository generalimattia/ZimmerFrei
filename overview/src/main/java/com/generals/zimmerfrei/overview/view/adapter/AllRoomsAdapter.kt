package com.generals.zimmerfrei.overview.view.adapter

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.generals.zimmerfrei.model.Day
import com.generals.zimmerfrei.overview.view.custom.TimePlan
import com.generals.zimmerfrei.overview.view.layout.SyncScroller
import java.lang.ref.WeakReference

class AllRoomsAdapter(
    private val days: MutableList<Day>, private val syncScroller: SyncScroller
) : RecyclerView.Adapter<AllRoomsAdapter.AllRoomsViewHolder>() {

    private val holders: MutableList<WeakReference<AllRoomsViewHolder>> = mutableListOf()

    init {
        setHasStableIds(true)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): AllRoomsViewHolder {
        val holder = AllRoomsViewHolder(
            TimePlan(parent.context)
        )
        holders.add(WeakReference(holder))
        return holder
    }

    override fun onBindViewHolder(holder: AllRoomsViewHolder, position: Int) {
        holder.bind(
            days,
            syncScroller
        )
    }

    override fun getItemCount(): Int = 1

    override fun getItemId(position: Int): Long = position.toLong()

    fun moreDays(days: List<Day>) {
        holders.forEach {
            it.get()
                ?.moreDays(days)
        }
    }

    class AllRoomsViewHolder(
        private val view: TimePlan
    ) : RecyclerView.ViewHolder(view) {

        fun bind(days: List<Day>, syncScroller: SyncScroller) {
            view.bind(
                days,
                syncScroller
            )
        }

        fun moreDays(days: List<Day>) {
            view.moreDays(days)
        }
    }
}