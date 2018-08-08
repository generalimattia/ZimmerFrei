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
    val adults: Int = 0,
    val children: Int = 0,
    val babies: Int = 0,
    val color: String = "#546e7a",
    val room: Room = Room(),
    val notes: String = "",
    val mobile: String = "",
    val email: String = ""
) {

    constructor(entity: ReservationEntity) : this(
        id = entity.id,
        name = entity.name,
        startDate = entity.startDate,
        endDate = entity.endDate,
        adults = entity.adults,
        children = entity.children,
        babies = entity.babies,
        color = entity.color,
        room = Room(entity.roomId)
    )

    fun toEntity() : ReservationEntity = ReservationEntity(
        name = name,
        startDate = startDate,
        endDate = endDate,
        adults = adults,
        children = children,
        babies = babies,
        color = color,
        roomId = room.name,
        notes = notes,
        email = email,
        mobile = mobile
    )
}

data class Room(
    val name: String = ""
) {

    constructor(entity: RoomEntity) : this(
        name = entity.name
    )

    fun toEntity(): RoomEntity = RoomEntity(name)
}

data class DayWithReservations(
    val day: Day = Day(), val reservations: List<Reservation> = emptyList()
) : Comparable<DayWithReservations> {

    override fun compareTo(other: DayWithReservations): Int =
        this.day.date.compareTo(other.day.date)
}

sealed class RoomDay {

    data class EmptyDay(val day: Day = Day()) : RoomDay()

    data class StartingReservationDay(
        val day: Day = Day(), val reservation: Reservation = Reservation()
    ) : RoomDay()

    data class ReservedDay(
        val day: Day = Day(), val reservation: Reservation = Reservation()
    ) : RoomDay()

    data class EndingReservationDay(
        val day: Day = Day(), val reservation: Reservation = Reservation()
    ) : RoomDay()
}