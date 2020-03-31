package com.generals.zimmerfrei.model

import android.os.Parcel
import android.os.Parcelable
import com.generals.network.model.ReservationInbound
import com.generals.network.model.RoomInbound
import com.generals.zimmerfrei.database.entities.ReservationEntity
import com.generals.zimmerfrei.database.entities.RoomEntity
import org.threeten.bp.LocalDate

data class Day(
        val title: String = "",
        val date: LocalDate,
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

    constructor(input: LocalDate) : this(
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
        val startDate: LocalDate = LocalDate.now(),
        val endDate: LocalDate = LocalDate.now(),
        val persons: Int = 0,
        val adults: Int = 0,
        val children: Int = 0,
        val babies: Int = 0,
        val color: String = "#546e7a",
        val room: Room = Room(),
        val notes: String = "",
        val mobile: String = "",
        val email: String = ""
) : Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readLong(),
            parcel.readString().orEmpty(),
            parcel.readSerializable() as LocalDate,
            parcel.readSerializable() as LocalDate,
            parcel.readInt(),
            parcel.readInt(),
            parcel.readInt(),
            parcel.readInt(),
            parcel.readString().orEmpty(),
            parcel.readParcelable(Room::class.java.classLoader) ?: Room(),
            parcel.readString().orEmpty(),
            parcel.readString().orEmpty(),
            parcel.readString().orEmpty())

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

    constructor(
            reservation: ReservationInbound, room: Room
    ) : this(
            id = reservation.id.toLong(),
            name = reservation.name,
            startDate = reservation.startDate,
            endDate = reservation.endDate,
            persons = reservation.persons,
            adults = reservation.adults,
            children = reservation.children,
            babies = reservation.babies,
            color = reservation.color,
            room = room,
            notes = reservation.notes,
            mobile = reservation.customer.mobile,
            email = reservation.customer.email
    )

    fun toEntity(): ReservationEntity = ReservationEntity(
            id = id,
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

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(id)
        parcel.writeString(name)
        parcel.writeInt(persons)
        parcel.writeInt(adults)
        parcel.writeInt(children)
        parcel.writeInt(babies)
        parcel.writeString(color)
        parcel.writeParcelable(room, flags)
        parcel.writeString(notes)
        parcel.writeString(mobile)
        parcel.writeString(email)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<Reservation> {
        override fun createFromParcel(parcel: Parcel): Reservation = Reservation(parcel)

        override fun newArray(size: Int): Array<Reservation?> = arrayOfNulls(size)
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

    constructor(inbound: RoomInbound) : this(
            id = inbound.id.toLong(),
            name = inbound.name,
            personsCount = inbound.maxPersons,
            isDouble = false,
            isSingle = false,
            isHandicap = false,
            hasBalcony = false)

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
            val day: Day,
            val room: Room
    ) : RoomDay()

    data class EmptyWeekend(
            val day: Day,
            val room: Room
    ) : RoomDay()

    data class StartingReservation(
            val day: Day,
            val reservation: Reservation = Reservation()
    ) : RoomDay()

    data class Reserved(
            val day: Day,
            val reservation: Reservation = Reservation()
    ) : RoomDay()

    data class EndingReservation(
            val day: Day,
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

        constructor(input: RoomDay.EmptyWeekend) : this(
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