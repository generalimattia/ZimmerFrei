package com.generals.zimmerfrei.overview.view.layout

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class NotScrollableLayoutManager : LinearLayoutManager {

    constructor(context: Context) : super(context)

    constructor(
            context: Context, @RecyclerView.Orientation orientation: Int, reverseLayout: Boolean
    ) : super(context, orientation, reverseLayout)

    override fun canScrollHorizontally(): Boolean = false

    override fun canScrollVertically(): Boolean = false
}