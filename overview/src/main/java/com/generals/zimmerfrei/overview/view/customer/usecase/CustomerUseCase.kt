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
}