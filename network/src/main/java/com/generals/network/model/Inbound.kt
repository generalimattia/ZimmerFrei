package com.generals.network.model

import com.squareup.moshi.Json
import org.threeten.bp.LocalDate

data class Inbound<T>(
        @field:Json(name = "_embedded") val embedded: T,
        @field:Json(name = "_links") val link: Link
)

data class Link(val self: Self)
data class Self(val href: String)

data class RoomListInboud(
        val rooms: List<RoomInbound>
)

data class RoomInbound(
        val id: Int,
        val name: String,
        val roomCount: Int,
        @field:Json(name = "_links") val link: Link
)

data class ReservationInbound(
        val id: Int,
        val name: String,
        val numberOfParticipants: Int,
        val startDate: LocalDate,
        val endDate: LocalDate,
        val customer: CustomerInbound,
        @field:Json(name = "_links") val link: Link
)

data class CustomerInbound(
        val id: Int,
        val firstName: String,
        val lastName: String,
        val socialId: String,
        val mobile: String,
        val email: String,
        val address: String,
        val birthDate: LocalDate,
        @field:Json(name = "_links") val link: Link
)