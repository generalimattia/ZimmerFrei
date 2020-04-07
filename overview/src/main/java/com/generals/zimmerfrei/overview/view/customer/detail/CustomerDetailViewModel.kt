package com.generals.zimmerfrei.overview.view.customer.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import arrow.core.Option
import com.generals.zimmerfrei.model.Customer
import com.generals.zimmerfrei.overview.view.customer.usecase.CustomerUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

class CustomerDetailViewModel @Inject constructor(
        private val useCase: CustomerUseCase
) : ViewModel() {

    fun start(url: String) {
        viewModelScope.launch {
            val customer: Option<Customer> = useCase.get(url)
        }
    }
}