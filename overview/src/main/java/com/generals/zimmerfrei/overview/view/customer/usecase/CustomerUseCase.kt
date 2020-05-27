package com.generals.zimmerfrei.overview.view.customer.usecase

import arrow.core.Option
import com.generals.network.api.CustomersAPI
import com.generals.network.model.CustomerInbound
import com.generals.network.model.CustomerListInbound
import com.generals.network.model.Inbound
import com.generals.zimmerfrei.model.Customer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

interface CustomerUseCase {
    suspend fun getAll(): List<Customer>
    suspend fun get(url: String): Option<Customer>
    suspend fun save(customer: Customer): ActionResult
    suspend fun update(customer: Customer): ActionResult
    suspend fun delete(url: String): ActionResult
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

    override suspend fun save(customer: Customer): ActionResult = withContext(Dispatchers.IO) {
        api.create(customer.toInbound()).fold(
                ifSuccess = { ActionResult.Success("Cliente creato!") },
                ifFailure = { ActionResult.Error("Errore") },
                ifError = {
                    Timber.e(it)
                    ActionResult.Error("Errore")
                }
        )
    }

    override suspend fun update(customer: Customer): ActionResult = withContext(Dispatchers.IO) {
        api.update(customer.id, customer.toInbound()).fold(
                ifSuccess = { ActionResult.Success("Cliente aggiornato!") },
                ifFailure = { ActionResult.Error("Errore") },
                ifError = {
                    Timber.e(it)
                    ActionResult.Error("Errore")
                }
        )
    }

    override suspend fun delete(url: String): ActionResult = withContext(Dispatchers.IO) {
        api.delete(url).fold(
                ifSuccess = { ActionResult.Success("Cliente rimosso!") },
                ifFailure = { ActionResult.Error("Errore") },
                ifError = {
                    Timber.e(it)
                    ActionResult.Error("Errore")
                }
        )
    }

}

sealed class ActionResult {
    abstract val message: String

    data class Success(
            override val message: String
    ) : ActionResult()

    data class Error(
            override val message: String
    ) : ActionResult()
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