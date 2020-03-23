package com.generals.zimmerfrei.overview.view.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.generals.zimmerfrei.model.Room
import com.generals.zimmerfrei.overview.view.custom.RoomView

class RoomsAdapter(
    private val rooms: List<Room>
) : RecyclerView.Adapter<RoomsAdapter.RoomViewHolder>() {

    init {
        setHasStableIds(true)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): RoomViewHolder = RoomViewHolder(RoomView(parent.context))


    override fun onBindViewHolder(holder: RoomViewHolder, position: Int) {
        holder.bind(rooms[position])
    }

    override fun getItemCount(): Int = rooms.size

    override fun getItemId(position: Int): Long = rooms[position].hashCode().toLong()


    class RoomViewHolder(
        private val view: RoomView
    ) : RecyclerView.ViewHolder(view) {

        fun bind(room: Room) {
            view.bind(room)
        }
    }
}