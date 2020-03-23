package com.generals.zimmerfrei.reservation.view.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.generals.zimmerfrei.reservation.R
import de.hdodenhof.circleimageview.CircleImageView

class ColorsAdapter(
        private val colors: List<ReservationColor>
): RecyclerView.Adapter<ColorViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ColorViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_color, parent, false)
        return ColorViewHolder(view)
    }

    override fun onBindViewHolder(holder: ColorViewHolder, position: Int) {
        holder.bind(colors[position].color)
    }

    override fun getItemCount(): Int = colors.size

    override fun getItemId(position: Int): Long = colors[position].id
}

class ColorViewHolder(view: View): RecyclerView.ViewHolder(view) {

    private val circleImageView: CircleImageView = view.findViewById(R.id.circle)

    fun bind(color: String) {
        circleImageView.circleBackgroundColor = Color.parseColor(color)
    }
}

data class ReservationColor(
        val id: Long,
        val color: String,
        var selected: Boolean
)