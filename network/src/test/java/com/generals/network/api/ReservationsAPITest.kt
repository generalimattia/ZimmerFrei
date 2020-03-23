package com.generals.network.api

import com.generals.network.model.*
import junit.framework.Assert.*
import kotlinx.coroutines.runBlocking
import org.junit.Ignore
import org.junit.Test
import org.threeten.bp.LocalDate

@Ignore
class ReservationsAPITest {

    private val client: ReservationsAPI = retrofit.create(ReservationsAPI::class.java)

    @Test
    fun shouldGetAllReservations() = runBlocking {
        val reservationsResponse: APIResult<Inbound<ReservationListInbound>> = client.fetchAll()
        assertTrue(reservationsResponse is APIResult.Success<*>)
        reservationsResponse as APIResult.Success
        assertNotNull(reservationsResponse.body)
        assertEquals(3, reservationsResponse.body.orNull()!!.embedded.reservations.size)
    }

    @Test
    fun shouldGetReservationById() = runBlocking {
        val reservationResponse: APIResult<ReservationInbound> = client.fetchById(4)
        assertTrue(reservationResponse is APIResult.Success<*>)
        reservationResponse as APIResult.Success
        assertNotNull(reservationResponse.body)
    }

    @Test
    fun shouldDeleteReservationById() = runBlocking {
        val response: APIResult<Unit> = client.delete(8)
        assertTrue(response is APIResult.Success)
    }

    @Test
    fun shouldCreateNewReservation() = runBlocking {
        val newReservation = ReservationInbound(
                name = "Reserved",
                numberOfParticipants = 10,
                startDate = LocalDate.now(),
                endDate = LocalDate.now().plusWeeks(2),
                customer = CustomerInbound(
                        firstName = "Jhones",
                        lastName = "Mc Kane",
                        socialId = "423409890223423",
                        mobile = "1230912",
                        email = "jhones@mckane.com",
                        address = "Jhones street 2nd",
                        birthDate = LocalDate.now().minusYears(45)
                )
        )
        val response: APIResult<Unit> = client.create(newReservation)
        assertTrue(response is APIResult.Success<*>)
        response as APIResult.Success

        val reservationsResponse: APIResult<Inbound<ReservationListInbound>> = client.fetchAll()
        reservationsResponse as APIResult.Success
        assertNotNull(reservationsResponse.body)
        assertEquals(4, reservationsResponse.body.orNull()!!.embedded.reservations.size)
    }

    @Test
    fun shouldGetReservationsByRoomIdAndFromDateToDate() = runBlocking {
        val reservationsResponse: APIResult<Inbound<ReservationListInbound>> = client.fetchByRoomAndFromDateToDate(
                roomId = 1,
                from = LocalDate.now().minusDays(10),
                to = LocalDate.now().plusDays(10)
        )
        assertTrue(reservationsResponse is APIResult.Success<*>)
        reservationsResponse as APIResult.Success
        assertNotNull(reservationsResponse.body)
        assertEquals(2, reservationsResponse.body.orNull()!!.embedded.reservations.size)
    }

    @Test
    fun shouldUpdateExistingReservation() = runBlocking {
        val reservationResponse: APIResult<ReservationInbound> = client.fetchById(4)
        reservationResponse as APIResult.Success
        assertNotNull(reservationResponse.body)

        val updatedReservation: ReservationInbound = reservationResponse.body.orNull()!!.copy(numberOfParticipants = 35)
        val response: APIResult<Unit> = client.update(4, updatedReservation)
        assertTrue(response is APIResult.Success<*>)

        val reservationsResponse: APIResult<Inbound<ReservationListInbound>> = client.fetchAll()
        reservationsResponse as APIResult.Success
        assertNotNull(reservationsResponse.body)
        val reservations: List<ReservationInbound> = reservationsResponse.body.fold(ifEmpty = { emptyList() }, ifSome = { it.embedded.reservations })
        assertEquals(4, reservations.size)
        assertEquals(35, reservations.first { it.numberOfParticipants == 1 }.name)
    }
}