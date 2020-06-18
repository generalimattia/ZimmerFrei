package com.generals.zimmerfrei.overview.view.customer.usecase

import arrow.core.Option
import com.generals.network.api.CustomersAPI
import com.generals.network.model.CustomerInbound
import com.generals.network.model.CustomerListInbound
import com.generals.network.model.Inbound
import com.generals.zimmerfrei.listeners.ActionResult
import com.generals.zimmerfrei.model.Customer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

interface CustomerUseCase {
    suspend fun getAll(): List<Customer>
    suspend fun get(url: String): Option<Customer>
    suspend fun save(customer: Customer): ActionResult<Customer>
    suspend fun update(customer: Customer): ActionResult<Customer>
    suspend fun delete(url: String): ActionResult<Customer>
}

class CustomerUseCaseImpl @Inject constructor(
        private val api: CustomersAPI
) : CustomerUseCase {

    override suspend fun getAll(): List<Customer> = withContext(Dispatchers.IO) {
        api.fetchAll().fold(
                ifSuccess = { response: Option<Inbound<CustomerListInbound>> ->
                    response.fold(
                            ifSome = { inbound: Inbound<CustomerListInbound> -> inbound.embedded.customers.map(::Customer) },
                            ifEmpty = { emptyList() }
                    )
                },
                ifFailure = { emptyList() },
                ifError = { throwable: Throwable? ->
                    Timber.e(throwable)
                    emptyList()
                }
        )
    }

    override suspend fun get(url: String): Option<Customer> = withContext(Dispatchers.IO) {
        api.get(url).fold(
                ifSuccess = { response: Option<CustomerInbound> -> response.map(::Customer) },
                ifFailure = { Option.empty() },
                ifError = { throwable: Throwable? ->
                    Timber.e(throwable)
                    Option.empty()
                }
        )
    }

    override suspend fun save(customer: Customer): ActionResult<Customer> = withContext(Dispatchers.IO) {
        api.create(customer.toInbound()).fold(
                ifSuccess = { result: Option<CustomerInbound> ->
                    result.fold(
                            ifSome = { ActionResult.Success("Cliente creato!", Customer(it)) },
                            ifEmpty = { ActionResult.Error<Customer>("Errore") })
                },
                ifFailure = { ActionResult.Error<Customer>("Errore") },
                ifError = {
                    Timber.e(it)
                    ActionResult.Error<Customer>("Errore")
                }
        )
    }

    override suspend fun update(customer: Customer): ActionResult<Customer> = withContext(Dispatchers.IO) {
        api.update(customer.id, customer.toInbound()).fold(
                ifSuccess = { result: Option<CustomerInbound> ->
                    result.fold(
                            ifSome = { ActionResult.Success("Cliente aggiornato!", Customer(it)) },
                            ifEmpty = { ActionResult.Error<Customer>("Errore") })
                },
                ifFailure = { ActionResult.Error<Customer>("Errore") },
                ifError = {
                    Timber.e(it)
                    ActionResult.Error<Customer>("Errore")
                }
        )
    }

    override suspend fun delete(url: String): ActionResult<Customer> = withContext(Dispatchers.IO) {
        api.delete(url).fold(
                ifSuccess = { ActionResult.Success("Cliente rimosso!", null) },
                ifFailure = { ActionResult.Error<Customer>("Errore") },
                ifError = {
                    Timber.e(it)
                    ActionResult.Error<Customer>("Errore")
                }
        )
    }

}

fun Customer.toInbound(): CustomerInbound =
        CustomerInbound(
                firstName = firstName,
                lastName = lastName,
                socialId = socialId,
                mobile = mobile,
                email = email,
                address = address,
                city = city,
                province = province,
                state = state,
                gender = gender,
                zip = zip,
                birthDate = birthDate,
                birthPlace = birthPlace
        )