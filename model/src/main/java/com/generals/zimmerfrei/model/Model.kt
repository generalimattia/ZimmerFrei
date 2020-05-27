package com.generals.zimmerfrei.model

import android.os.Parcel
import android.os.Parcelable
import com.generals.network.model.CustomerInbound
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
        val url: String? = null,
        val customer: Customer? = null
) {

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
            url = null,
            customer = null
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
            url = null,
            customer = null
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
            url = reservation.link?.self?.href,
            customer = reservation.customer?.let { Customer(it) }
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
            notes = notes
    )
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

    abstract val day: Day
    abstract val room: Room
    abstract val reservationURL: String?

    data class Empty(
            override val day: Day,
            override val room: Room
    ) : RoomDay() {
        override val reservationURL: String? = null
    }

    data class EmptyWeekend(
            override val day: Day,
            override val room: Room
    ) : RoomDay() {
        override val reservationURL: String? = null
    }

    data class StartingReservation(
            override val day: Day,
            val reservation: Reservation = Reservation()
    ) : RoomDay() {
        override val room: Room
            get() = reservation.room
        override val reservationURL: String?
            get() = reservation.url
    }

    data class Reserved(
            override val day: Day,
            val reservation: Reservation = Reservation()
    ) : RoomDay() {
        override val room: Room
            get() = reservation.room
        override val reservationURL: String?
            get() = reservation.url
    }

    data class EndingReservation(
            override val day: Day,
            val reservation: Reservation = Reservation()
    ) : RoomDay() {
        override val room: Room
            get() = reservation.room
        override val reservationURL: String?
            get() = reservation.url
    }
}

data class Customer(
        val id: Int = 0,
        val firstName: String,
        val lastName: String,
        val socialId: String,
        val mobile: String,
        val email: String,
        val address: String,
        val city: String,
        val province: String,
        val state: String,
        val zip: String,
        val gender: String,
        val birthDate: LocalDate,
        val birthPlace: String,
        val link: String? = null
) : Parcelable {

    constructor(inbound: CustomerInbound) : this(
            id = inbound.id,
            firstName = inbound.firstName,
            lastName = inbound.lastName,
            socialId = inbound.socialId,
            mobile = inbound.mobile,
            email = inbound.email,
            address = inbound.address,
            city = inbound.city,
            province = inbound.province,
            state = inbound.state,
            zip = inbound.zip,
            gender = inbound.gender,
            birthDate = inbound.birthDate,
            birthPlace = inbound.birthPlace,
            link = inbound.link?.self?.href
    )

    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readString().orEmpty(),
            parcel.readString().orEmpty(),
            parcel.readString().orEmpty(),
            parcel.readString().orEmpty(),
            parcel.readString().orEmpty(),
            parcel.readString().orEmpty(),
            parcel.readString().orEmpty(),
            parcel.readString().orEmpty(),
            parcel.readString().orEmpty(),
            parcel.readString().orEmpty(),
            parcel.readString().orEmpty(),
            parcel.readSerializable() as LocalDate,
            parcel.readString().orEmpty(),
            parcel.readString())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(firstName)
        parcel.writeString(lastName)
        parcel.writeString(socialId)
        parcel.writeString(mobile)
        parcel.writeString(email)
        parcel.writeString(address)
        parcel.writeString(city)
        parcel.writeString(province)
        parcel.writeString(state)
        parcel.writeString(zip)
        parcel.writeString(gender)
        parcel.writeString(birthPlace)
        parcel.writeString(link)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<Customer> {
        override fun createFromParcel(parcel: Parcel): Customer = Customer(parcel)

        override fun newArray(size: Int): Array<Customer?> = arrayOfNulls(size)
    }
}