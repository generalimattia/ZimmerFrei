package com.generals.zimmerfrei.room.list.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.generals.zimmerfrei.model.Room
import com.generals.zimmerfrei.room.R

class RoomsAdapter(
    private val rooms: List<Room>,
    private val onClickListener: (Room) -> Unit,
    private val onDeleteClickListener: (Room) -> Unit
) : RecyclerView.Adapter<RoomsAdapter.RoomViewHolder>() {

    init {
        setHasStableIds(true)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoomViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(
                R.layout.item_room,
                parent,
                false
            )
        return RoomViewHolder(view)
    }

    override fun getItemCount(): Int = rooms.size

    override fun onBindViewHolder(holder: RoomViewHolder, position: Int) {
        holder.bind(
            rooms[position],
            onClickListener,
            onDeleteClickListener
        )
    }

    override fun getItemId(position: Int): Long = rooms[position].hashCode().toLong()

    class RoomViewHolder(
        view: View
    ) : RecyclerView.ViewHolder(view) {

        private val roomName: TextView = view.findViewById(R.id.room_name)
        private val delete: ImageView = view.findViewById(R.id.delete)
        private val edit: ImageView = view.findViewById(R.id.edit)

        fun bind(
            room: Room, onClickListener: (Room) -> Unit, onDeleteClickListener: (Room) -> Unit
        ) {
            roomName.text = room.name

            itemView.setOnClickListener {
                onClickListener(room)
            }

            delete.setOnClickListener {
                onDeleteClickListener(room)
            }

            edit.setOnClickListener {
                onClickListener(room)
            }
        }
    }
}