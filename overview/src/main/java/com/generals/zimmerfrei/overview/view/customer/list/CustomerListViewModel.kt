package com.generals.zimmerfrei.overview.view.customer.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.generals.zimmerfrei.model.Customer
import com.generals.zimmerfrei.overview.view.customer.usecase.CustomerUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

class CustomerListViewModel @Inject constructor(
        private val useCase: CustomerUseCase
) : ViewModel() {

    val customers: LiveData<List<Customer>>
        get() = _customers
    private val _customers: MutableLiveData<List<Customer>> = MutableLiveData()

    val selected: LiveData<String?>
        get() = _selected
    private val _selected: MutableLiveData<String?> = MutableLiveData()

    fun start() {
        viewModelScope.launch {
            val allCustomers: List<Customer> = useCase.getAll()
            _customers.value = allCustomers
        }
    }

    fun onCustomerClick(customer: Customer) {
        _selected.value = customer.link
    }
}