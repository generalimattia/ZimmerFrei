package com.generals.zimmerfrei.model

import android.os.Parcel
import android.os.Parcelable
import com.generals.zimmerfrei.database.entities.ReservationEntity
import com.generals.zimmerfrei.database.entities.RoomEntity
import org.threeten.bp.OffsetDateTime

data class Day(
        val title: String = "",
        val date: OffsetDateTime = OffsetDateTime.now(),
        val monthDays: Int = 0
)

data class ParcelableDay(
        val dayOfMonth: Int,
        val month: Int,
        val year: Int
) : Parcelable {

    constructor(input: Day) : this(
            dayOfMonth = input.date.dayOfMonth,
            month = input.date.monthValue,
            year = input.date.year
    )

    constructor(input: OffsetDateTime) : this(
            dayOfMonth = input.dayOfMonth,
            month = input.monthValue,
            year = input.year
    )

    constructor(source: Parcel) : this(
            source.readInt(),
            source.readInt(),
            source.readInt()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeInt(dayOfMonth)
        writeInt(month)
        writeInt(year)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<ParcelableDay> = object : Parcelable.Creator<ParcelableDay> {
            override fun createFromParcel(source: Parcel): ParcelableDay = ParcelableDay(source)
            override fun newArray(size: Int): Array<ParcelableDay?> = arrayOfNulls(size)
        }
    }
}

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
            room = Room(room),
            notes = reservation.notes,
            mobile = reservation.mobile,
            email = reservation.email
    )

    constructor(
            reservation: ReservationEntity, room: Room
    ) : this(
            id = reservation.id,
            name = reservation.name,
            startDate = reservation.startDate,
            endDate = reservation.endDate,
            adults = reservation.adults,
            children = reservation.children,
            babies = reservation.babies,
            color = reservation.color,
            room = room,
            notes = reservation.notes,
            mobile = reservation.mobile,
            email = reservation.email
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
            personsCount = entity.personsCount,
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

sealed class RoomDay {

    data class Empty(
            val day: Day = Day(),
            val room: Room
    ) : RoomDay()

    data class StartingReservation(
            val day: Day = Day(),
            val reservation: Reservation = Reservation()
    ) : RoomDay()

    data class Reserved(
            val day: Day = Day(),
            val reservation: Reservation = Reservation()
    ) : RoomDay()

    data class EndingReservation(
            val day: Day = Day(),
            val reservation: Reservation = Reservation()
    ) : RoomDay()
}

sealed class ParcelableRoomDay : Parcelable {

    abstract val day: ParcelableDay

    data class Empty(
            override val day: ParcelableDay,
            val room: Room
    ) : ParcelableRoomDay(), Parcelable {

        constructor(input: RoomDay.Empty) : this(
                ParcelableDay(input.day),
                input.room
        )

        constructor(source: Parcel) : this(
                source.readParcelable<ParcelableDay>(ParcelableDay::class.java.classLoader),
                source.readParcelable<Room>(Room::class.java.classLoader)
        )

        override fun describeContents() = 0

        override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
            writeParcelable(day, 0)
            writeParcelable(room, 0)
        }

        companion object {
            @JvmField
            val CREATOR: Parcelable.Creator<Empty> = object : Parcelable.Creator<Empty> {
                override fun createFromParcel(source: Parcel): Empty = Empty(source)
                override fun newArray(size: Int): Array<Empty?> = arrayOfNulls(size)
            }
        }
    }

    data class Reserved(
            override val day: ParcelableDay,
            val reservation: Reservation
    ) : ParcelableRoomDay(), Parcelable {

        constructor(input: RoomDay.Reserved) : this(
                ParcelableDay(input.day),
                input.reservation
        )

        constructor(input: RoomDay.StartingReservation) : this(
                ParcelableDay(input.day),
                input.reservation
        )

        constructor(input: RoomDay.EndingReservation) : this(
                ParcelableDay(input.day),
                input.reservation
        )

        constructor(source: Parcel) : this(
                source.readParcelable<ParcelableDay>(ParcelableDay::class.java.classLoader),
                source.readParcelable<Reservation>(Reservation::class.java.classLoader)
        )

        override fun describeContents() = 0

        override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
            writeParcelable(day, 0)
            writeParcelable(reservation, 0)
        }

        companion object {
            @JvmField
            val CREATOR: Parcelable.Creator<Reserved> = object : Parcelable.Creator<Reserved> {
                override fun createFromParcel(source: Parcel): Reserved = Reserved(source)
                override fun newArray(size: Int): Array<Reserved?> = arrayOfNulls(size)
            }
        }
    }
}