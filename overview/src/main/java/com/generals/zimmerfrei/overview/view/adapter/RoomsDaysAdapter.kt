package com.generals.zimmerfrei.overview.view.adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.generals.zimmerfrei.model.RoomDay
import com.generals.zimmerfrei.overview.view.custom.days.EmptyDayView
import com.generals.zimmerfrei.overview.view.custom.days.EndingReservationDayView
import com.generals.zimmerfrei.overview.view.custom.days.ReservedDayView
import com.generals.zimmerfrei.overview.view.custom.days.StartingReservationDayView

class RoomsDaysAdapter(
    roomDays: List<RoomDay>
) : RecyclerView.Adapter<RoomsDaysAdapter.RoomDayViewHolder>() {

    private val _roomDays: MutableList<RoomDay> = roomDays.toMutableList()

    companion object {
        private const val EMPTY_DAY = 0
        private const val STARTING_RESERVATION = 1
        private const val ENDING_RESERVATION = 2
        private const val RESERVED = 3
    }

    init {
        setHasStableIds(true)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): RoomDayViewHolder = when (viewType) {
        EMPTY_DAY -> RoomDayViewHolder.EmptyDayViewHolder(EmptyDayView(parent.context))
        RESERVED -> RoomDayViewHolder.ReservedDayViewHolder(ReservedDayView(parent.context))
        STARTING_RESERVATION -> RoomDayViewHolder.StartingReservationViewHolder(StartingReservationDayView(parent.context))
        else -> RoomDayViewHolder.EndingReservationViewHolder(EndingReservationDayView(parent.context))
    }


    override fun onBindViewHolder(holder: RoomDayViewHolder, position: Int) {
        when(holder) {
            is RoomDayViewHolder.EmptyDayViewHolder -> holder.bind(_roomDays[position] as RoomDay.EmptyDay)
            is RoomDayViewHolder.ReservedDayViewHolder -> holder.bind(_roomDays[position] as RoomDay.ReservedDay)
            is RoomDayViewHolder.StartingReservationViewHolder -> holder.bind(_roomDays[position] as RoomDay.StartingReservationDay)
            is RoomDayViewHolder.EndingReservationViewHolder -> holder.bind(_roomDays[position] as RoomDay.EndingReservationDay)
        }
    }

    override fun getItemCount(): Int = _roomDays.size

    override fun getItemId(position: Int): Long = _roomDays[position].hashCode().toLong()

    override fun getItemViewType(position: Int): Int = when (_roomDays[position]) {
        is RoomDay.EmptyDay -> EMPTY_DAY
        is RoomDay.StartingReservationDay -> STARTING_RESERVATION
        is RoomDay.EndingReservationDay -> ENDING_RESERVATION
        is RoomDay.ReservedDay -> RESERVED
    }

    sealed class RoomDayViewHolder(view: View): RecyclerView.ViewHolder(view) {

        class EmptyDayViewHolder(
            private val view: EmptyDayView
        ) : RoomDayViewHolder(view) {

            fun bind(roomDay: RoomDay.EmptyDay) {
                view.bind(roomDay)
            }
        }

        class StartingReservationViewHolder(
            private val view: StartingReservationDayView
        ) : RoomDayViewHolder(view) {

            fun bind(roomDay: RoomDay.StartingReservationDay) {
                view.bind(roomDay)
            }
        }

        class EndingReservationViewHolder(
            private val view: EndingReservationDayView
        ) : RoomDayViewHolder(view) {

            fun bind(roomDay: RoomDay.EndingReservationDay) {
                view.bind(roomDay)
            }
        }

        class ReservedDayViewHolder(
            private val view: ReservedDayView
        ) : RoomDayViewHolder(view) {

            fun bind(roomDay: RoomDay.ReservedDay) {
                view.bind(roomDay)
            }
        }
    }
}