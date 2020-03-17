package com.generals.network.api

import com.generals.network.model.Inbound
import com.generals.network.model.RoomInbound
import com.generals.network.model.RoomListInboud
import junit.framework.Assert.*
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.junit.Test
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class RoomAPITest {

    private val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY })
            .build()

    private val retrofit = Retrofit.Builder()
            .baseUrl("http://192.168.1.18:8080/")
            .addConverterFactory(MoshiConverterFactory.create())
            .client(okHttpClient)
            .build()

    private val client: RoomAPI = retrofit.create(RoomAPI::class.java)

    @Test
    fun shouldGetAllRooms() {
        val roomsResponse: Response<Inbound<RoomListInboud>> = client.fetchAllRooms().execute()
        assertTrue(roomsResponse.isSuccessful)
        assertNotNull(roomsResponse.body())
        assertEquals(3, roomsResponse.body()!!.embedded.rooms.size)
    }

    @Test
    fun shouldGetRoomById() {
        val roomResponse: Response<RoomInbound> = client.fetchRoomById(1).execute()
        assertTrue(roomResponse.isSuccessful)
        assertNotNull(roomResponse.body())
    }
}