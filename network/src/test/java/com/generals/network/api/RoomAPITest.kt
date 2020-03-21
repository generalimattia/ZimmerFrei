package com.generals.network.api

import com.generals.network.model.APIResult
import com.generals.network.model.Inbound
import com.generals.network.model.RoomInbound
import com.generals.network.model.RoomListInbound
import junit.framework.Assert.*
import kotlinx.coroutines.runBlocking
import org.junit.Test

class RoomAPITest {

    private val client: RoomAPI = retrofit.create(RoomAPI::class.java)

    @Test
    fun shouldGetAllRooms() = runBlocking {
        val roomsResponse: APIResult<Inbound<RoomListInbound>> = client.fetchAll()
        assertTrue(roomsResponse is APIResult.Success<*>)
        roomsResponse as APIResult.Success
        assertNotNull(roomsResponse.body)
        assertEquals(3, roomsResponse.body!!.embedded.rooms.size)
    }

    @Test
    fun shouldGetRoomById() = runBlocking {
        val roomResponse: APIResult<RoomInbound> = client.fetchById(1)
        assertTrue(roomResponse is APIResult.Success<*>)
        roomResponse as APIResult.Success
        assertNotNull(roomResponse.body)
    }

    @Test
    fun shouldDeleteRoomById() = runBlocking {
        val response: APIResult<Unit> = client.delete(1)
        assertTrue(response is APIResult.Failure)
        response as APIResult.Failure
        assertEquals(403, response.code)
    }

    @Test
    fun shouldCreateNewRoom() = runBlocking {
        val newRoom = RoomInbound(name = "TEST", roomCount = 3)
        val response: APIResult<Unit> = client.create(newRoom)
        assertTrue(response is APIResult.Success<*>)
        response as APIResult.Success

        val roomsResponse: APIResult<Inbound<RoomListInbound>> = client.fetchAll()
        roomsResponse as APIResult.Success
        assertNotNull(roomsResponse.body)
        assertEquals(4, roomsResponse.body!!.embedded.rooms.size)
    }

    @Test
    fun shouldUpdateExistingRoom() = runBlocking {
        val updatedRoom = RoomInbound(id = 1, name = "TEST", roomCount = 3)
        val response: APIResult<Unit> = client.update(1, updatedRoom)
        assertTrue(response is APIResult.Success<*>)

        val roomsResponse: APIResult<Inbound<RoomListInbound>> = client.fetchAll()
        roomsResponse as APIResult.Success
        assertNotNull(roomsResponse.body)
        val rooms: List<RoomInbound> = roomsResponse.body!!.embedded.rooms
        assertEquals(4, rooms.size)
        assertEquals("TEST", rooms.first { it.id == 1 }.name)

    }
}