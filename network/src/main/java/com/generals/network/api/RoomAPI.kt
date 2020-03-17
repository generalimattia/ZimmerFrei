package com.generals.network.api

import com.generals.network.model.Inbound
import com.generals.network.model.RoomInbound
import com.generals.network.model.RoomListInboud
import retrofit2.Call
import retrofit2.http.*

interface RoomAPI {

    @POST("rooms")
    fun saveRoom(@Body room: RoomInbound)

    @PUT("rooms/{id}")
    fun updateRoom(@Path("id") id: Int, @Body room: RoomInbound)

    @DELETE("rooms/{id}")
    fun deleteRoom()

    @GET("rooms/{id}")
    fun fetchRoomById(@Path("id") id: Int): Call<RoomInbound>

    @GET("rooms")
    fun fetchAllRooms(): Call<Inbound<RoomListInboud>>
}