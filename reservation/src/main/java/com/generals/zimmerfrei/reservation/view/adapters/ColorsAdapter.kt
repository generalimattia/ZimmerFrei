package com.generals.zimmerfrei.reservation.view.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.generals.zimmerfrei.common.extension.toColor
import com.generals.zimmerfrei.common.utils.buildDrawable
import com.generals.zimmerfrei.reservation.R

class ColorsAdapter(
        private val colors: List<ColorItem>,
        private val onColorClick: (ColorItem) -> Unit
) : RecyclerView.Adapter<ColorViewHolder>() {

    init {
        setHasStableIds(true)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ColorViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_color, parent, false)
        return ColorViewHolder(view)
    }

    override fun onBindViewHolder(holder: ColorViewHolder, position: Int) {
        holder.bind(colors[position], onColorClick)
    }

    override fun getItemCount(): Int = colors.size
    override fun getItemId(position: Int): Long = colors[position].hex.hashCode().toLong()

    fun update(item: ColorItem) {
        colors.firstOrNull { it.hex != item.hex && it.selected }?.apply { selected = false }
        colors.firstOrNull { it.hex == item.hex }?.apply { selected = item.selected }
        notifyDataSetChanged()
    }
}

class ColorViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val color: ImageView = view.findViewById(R.id.color)
    private val selection: ImageView = view.findViewById(R.id.selection)

    fun bind(
            item: ColorItem,
            onColorClick: (ColorItem) -> Unit
    ) {
        color.background = buildDrawable(itemView.context, item.hex.toColor(), R.drawable.shape_circular_solid)
        if (item.selected) {
            selection.visibility = View.VISIBLE
        } else {
            selection.visibility = View.INVISIBLE
        }
        itemView.setOnClickListener { onColorClick(item) }
    }
}

data class ColorItem(
        val hex: String,
        var selected: Boolean
)