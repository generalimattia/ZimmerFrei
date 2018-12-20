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
        private val onDayClick: (RoomDay) -> Unit
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
            is RoomDayViewHolder.EmptyDayViewHolder -> holder.bind(_roomDays[position] as RoomDay.Empty, onDayClick)
            is RoomDayViewHolder.ReservedDayViewHolder -> holder.bind(_roomDays[position] as RoomDay.Reserved, onDayClick)
            is RoomDayViewHolder.StartingReservationViewHolder -> holder.bind(_roomDays[position] as RoomDay.StartingReservation, onDayClick)
            is RoomDayViewHolder.EndingReservationViewHolder -> holder.bind(_roomDays[position] as RoomDay.EndingReservation, onDayClick)
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

            fun bind(
                    roomDay: RoomDay.Empty,
                    onDayClick: (RoomDay) -> Unit
            ) {
                view.bind(roomDay, onDayClick)
            }
        }

        class StartingReservationViewHolder(
                private val view: StartingReservationDayView
        ) : RoomDayViewHolder(view) {

            fun bind(
                    roomDay: RoomDay.StartingReservation,
                    onDayClick: (RoomDay) -> Unit
            ) {
                view.bind(roomDay, onDayClick)
            }
        }

        class EndingReservationViewHolder(
                private val view: EndingReservationDayView
        ) : RoomDayViewHolder(view) {

            fun bind(
                    roomDay: RoomDay.EndingReservation,
                    onDayClick: (RoomDay) -> Unit
            ) {
                view.bind(roomDay, onDayClick)
            }
        }

        class ReservedDayViewHolder(
                private val view: ReservedDayView
        ) : RoomDayViewHolder(view) {

            fun bind(
                    roomDay: RoomDay.Reserved,
                    onDayClick: (RoomDay) -> Unit
            ) {
                view.bind(roomDay, onDayClick)
            }
        }
    }
}