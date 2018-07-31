package com.generals.zimmerfrei.model

import com.generals.zimmerfrei.repository.entities.ReservationEntity
import com.generals.zimmerfrei.repository.entities.RoomEntity
import org.threeten.bp.OffsetDateTime

data class Day(
    val title: String = "", val date: OffsetDateTime = OffsetDateTime.now(), val monthDays: Int = 0
)

data class Reservation(
    val id: Long = 0,
    val name: String = "",
    val startDate: OffsetDateTime = OffsetDateTime.now(),
    val endDate: OffsetDateTime = OffsetDateTime.now(),
    val personCount: Int = 3,
    val room: Room = Room()
) {

    constructor(entity: ReservationEntity) : this(
        id = entity.id,
        name = entity.name,
        startDate = entity.startDate,
        endDate = entity.endDate,
        personCount = entity.personCount,
        room = Room(entity.roomId)
    )
}

data class Room(
    val name: String = ""
) {

    constructor(entity: RoomEntity) : this(
        name = entity.name
    )
}

data class DayWithReservations(
    val day: Day = Day(), val reservations: List<Reservation> = emptyList()
) : Comparable<DayWithReservations> {

    override fun compareTo(other: DayWithReservations): Int =
        this.day.date.compareTo(other.day.date)
}