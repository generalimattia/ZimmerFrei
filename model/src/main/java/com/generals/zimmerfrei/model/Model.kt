package com.generals.zimmerfrei.model

import android.os.Parcel
import android.os.Parcelable
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
) : Parcelable {

    constructor(
        reservation: ReservationEntity, room: RoomEntity
    ) : this(
        id = reservation.id,
        name = reservation.name,
        startDate = reservation.startDate,
        endDate = reservation.endDate,
        adults = reservation.adults,
        children = reservation.children,
        babies = reservation.babies,
        color = reservation.color,
        room = Room(room)
    )

    fun toEntity(): ReservationEntity = ReservationEntity(
        name = name,
        startDate = startDate,
        endDate = endDate,
        adults = adults,
        children = children,
        babies = babies,
        color = color,
        roomId = room.id,
        notes = notes,
        email = email,
        mobile = mobile
    )

    constructor(source: Parcel) : this(
        source.readLong(),
        source.readString(),
        source.readSerializable() as OffsetDateTime,
        source.readSerializable() as OffsetDateTime,
        source.readInt(),
        source.readInt(),
        source.readInt(),
        source.readString(),
        source.readParcelable<Room>(Room::class.java.classLoader),
        source.readString(),
        source.readString(),
        source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeLong(id)
        writeString(name)
        writeSerializable(startDate)
        writeSerializable(endDate)
        writeInt(adults)
        writeInt(children)
        writeInt(babies)
        writeString(color)
        writeParcelable(
            room,
            0
        )
        writeString(notes)
        writeString(mobile)
        writeString(email)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Reservation> = object : Parcelable.Creator<Reservation> {
            override fun createFromParcel(source: Parcel): Reservation = Reservation(source)
            override fun newArray(size: Int): Array<Reservation?> = arrayOfNulls(size)
        }
    }
}

data class Room(
    val id: Long = 0L,
    val name: String = "",
    val personsCount: Int = 0,
    val isDouble: Boolean = false,
    val isSingle: Boolean = false,
    val isHandicap: Boolean = false,
    val hasBalcony: Boolean = false
) : Parcelable {

    constructor(entity: RoomEntity) : this(
        id = entity.id,
        name = entity.name,
        isDouble = entity.isDouble,
        isSingle = entity.isSingle,
        isHandicap = entity.isHandicap,
        hasBalcony = entity.hasBalcony
    )

    fun toEntity(): RoomEntity = RoomEntity(
        id,
        name,
        personsCount,
        isDouble,
        isSingle,
        isHandicap,
        hasBalcony
    )

    constructor(source: Parcel) : this(
        source.readLong(),
        source.readString(),
        source.readInt(),
        1 == source.readInt(),
        1 == source.readInt(),
        1 == source.readInt(),
        1 == source.readInt()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeLong(id)
        writeString(name)
        writeInt(personsCount)
        writeInt((if (isDouble) 1 else 0))
        writeInt((if (isSingle) 1 else 0))
        writeInt((if (isHandicap) 1 else 0))
        writeInt((if (hasBalcony) 1 else 0))
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Room> = object : Parcelable.Creator<Room> {
            override fun createFromParcel(source: Parcel): Room = Room(source)
            override fun newArray(size: Int): Array<Room?> = arrayOfNulls(size)
        }
    }
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