package com.generals.network.api

import com.generals.network.model.*
import org.threeten.bp.LocalDate
import retrofit2.http.*

interface RoomsAPI {

    @POST("rooms")
    suspend fun create(@Body room: RoomInbound): APIResult<Unit>

    @PUT("rooms/{id}")
    suspend fun update(@Path("id") id: Int, @Body room: RoomInbound): APIResult<Unit>

    @DELETE("rooms/{id}")
    suspend fun delete(@Path("id") id: Int): APIResult<Unit>

    @GET("rooms/{id}")
    suspend fun fetchById(@Path("id") id: Int): APIResult<RoomInbound>

    @GET("rooms")
    suspend fun fetchAll(): APIResult<Inbound<RoomListInbound>>
}

interface ReservationsAPI {

    @POST("reservations")
    suspend fun create(@Body reservation: ReservationInbound): APIResult<Unit>

    @PUT("reservations/{id}")
    suspend fun update(@Path("id") id: Int, @Body reservation: ReservationInbound): APIResult<Unit>

    @DELETE("reservations/{id}")
    suspend fun delete(@Path("id") id: Int): APIResult<Unit>

    @GET("reservations/{id}")
    suspend fun fetchById(@Path("id") id: Int): APIResult<ReservationInbound>

    @GET("reservations")
    suspend fun fetchAll(): APIResult<Inbound<ReservationListInbound>>

    @GET("reservations")
    suspend fun fetchByRoomAndFromDateToDate(
            @Query("roomId") roomId: Int,
            @Query("from") from: LocalDate,
            @Query("to") to: LocalDate
    ): APIResult<Inbound<ReservationListInbound>>
}