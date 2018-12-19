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
        roomDays: List<RoomDay>,
        private val onEmptyDayClick: (day: RoomDay) -> Unit
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
        when (holder) {
            is RoomDayViewHolder.EmptyDayViewHolder -> holder.bind(_roomDays[position] as RoomDay.Empty, onEmptyDayClick)
            is RoomDayViewHolder.ReservedDayViewHolder -> holder.bind(_roomDays[position] as RoomDay.Reserved)
            is RoomDayViewHolder.StartingReservationViewHolder -> holder.bind(_roomDays[position] as RoomDay.StartingReservation)
            is RoomDayViewHolder.EndingReservationViewHolder -> holder.bind(_roomDays[position] as RoomDay.EndingReservation)
        }
    }

    override fun getItemCount(): Int = _roomDays.size

    override fun getItemId(position: Int): Long = _roomDays[position].hashCode().toLong()

    override fun getItemViewType(position: Int): Int = when (_roomDays[position]) {
        is RoomDay.Empty -> EMPTY_DAY
        is RoomDay.StartingReservation -> STARTING_RESERVATION
        is RoomDay.EndingReservation -> ENDING_RESERVATION
        is RoomDay.Reserved -> RESERVED
    }

    sealed class RoomDayViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        class EmptyDayViewHolder(
                private val view: EmptyDayView
        ) : RoomDayViewHolder(view) {

            fun bind(roomDay: RoomDay.Empty,
                     onEmptyDayClick: (day: RoomDay) -> Unit) {
                view.bind(roomDay, onEmptyDayClick)
            }
        }

        class StartingReservationViewHolder(
                private val view: StartingReservationDayView
        ) : RoomDayViewHolder(view) {

            fun bind(roomDay: RoomDay.StartingReservation) {
                view.bind(roomDay)
            }
        }

        class EndingReservationViewHolder(
                private val view: EndingReservationDayView
        ) : RoomDayViewHolder(view) {

            fun bind(roomDay: RoomDay.EndingReservation) {
                view.bind(roomDay)
            }
        }

        class ReservedDayViewHolder(
                private val view: ReservedDayView
        ) : RoomDayViewHolder(view) {

            fun bind(roomDay: RoomDay.Reserved) {
                view.bind(roomDay)
            }
        }
    }
}