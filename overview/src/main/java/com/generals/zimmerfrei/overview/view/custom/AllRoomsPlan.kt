package com.generals.zimmerfrei.overview.view.custom

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.generals.zimmerfrei.common.custom.EndlessRecyclerViewScrollListener
import com.generals.zimmerfrei.model.Day
import com.generals.zimmerfrei.overview.R
import com.generals.zimmerfrei.overview.view.adapter.AllRoomsAdapter
import com.generals.zimmerfrei.overview.view.adapter.DaysAdapter
import com.generals.zimmerfrei.overview.view.layout.SyncScroller

class AllRoomsPlan : FrameLayout {

    lateinit var recyclerView: RecyclerView

    constructor(context: Context) : super(context) {
        init(null, 0)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(attrs, 0)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(
        context, attrs, defStyle
    ) {
        init(attrs, defStyle)
    }

    private fun init(attrs: AttributeSet?, defStyle: Int) {
        LayoutInflater.from(context)
            .inflate(R.layout.widget_all_rooms_plan, this, true)

        recyclerView = findViewById(R.id.recycler_view)

    }

    fun bind(days: MutableList<Day>, syncScroller: SyncScroller) {
        recyclerView.adapter = AllRoomsAdapter(days, syncScroller)
    }

    fun bind(days: List<Day>, loadMoreDays: () -> Unit) {

        val scrollListener: EndlessRecyclerViewScrollListener = object :
            EndlessRecyclerViewScrollListener(recyclerView.layoutManager as LinearLayoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                loadMoreDays()
            }
        }
        recyclerView.addOnScrollListener(scrollListener)
        recyclerView.adapter = DaysAdapter(days)
    }

    fun moreDays(days: List<Day>) {
        (recyclerView.adapter as AllRoomsAdapter).moreDays(days)
    }
}
