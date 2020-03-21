package com.generals.network.model

import com.squareup.moshi.Json
import org.threeten.bp.LocalDate

data class Inbound<T>(
        @Json(name = "_embedded") val embedded: T,
        @Json(name = "_links") val link: Link
)

data class Link(val self: Self = Self())
data class Self(val href: String = "")

data class RoomListInbound(
        val rooms: List<RoomInbound>
)

data class RoomInbound(
        val id: Int = 0,
        val name: String,
        val roomCount: Int,
        @Json(name = "_links") val link: Link? = null
)

data class ReservationListInbound(
        val reservations: List<ReservationInbound>
)

data class ReservationInbound(
        val id: Int = 0,
        val name: String,
        val numberOfParticipants: Int,
        val startDate: LocalDate,
        val endDate: LocalDate,
        val customer: CustomerInbound,
        @Json(name = "_links") val link: Link? = null
)

data class CustomerInbound(
        val id: Int = 0,
        val firstName: String,
        val lastName: String,
        val socialId: String,
        val mobile: String,
        val email: String,
        val address: String,
        val birthDate: LocalDate,
        @Json(name = "_links") val link: Link? = null
)