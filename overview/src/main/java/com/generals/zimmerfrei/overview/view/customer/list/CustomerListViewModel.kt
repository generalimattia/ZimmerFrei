package com.generals.zimmerfrei.overview.view.customer.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.generals.zimmerfrei.listeners.ActionResult
import com.generals.zimmerfrei.listeners.ActionListener
import com.generals.zimmerfrei.model.Customer
import com.generals.zimmerfrei.overview.view.customer.usecase.CustomerUseCase
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

class CustomerListViewModel @Inject constructor(
        private val useCase: CustomerUseCase,
        private val customerActionListener: ActionListener<Customer>
) : ViewModel() {

    val customers: LiveData<List<Customer>>
        get() = _customers
    private val _customers: MutableLiveData<List<Customer>> = MutableLiveData()

    val selected: LiveData<String?>
        get() = _selected
    private val _selected: MutableLiveData<String?> = MutableLiveData()

    val message: LiveData<String>
        get() = _message
    private val _message: MutableLiveData<String> = MutableLiveData()

    private val compositeDisposable = CompositeDisposable()

    fun start() {
        fetchCustomers()

        customerActionListener.observable.subscribe(
                { result: ActionResult<Customer> ->
                    if (result is ActionResult.Success) {
                        fetchCustomers()
                        _message.value = result.message
                    }
                },
                Timber::e,
                {}
        ).also { compositeDisposable.add(it) }
    }

    private fun fetchCustomers() {
        viewModelScope.launch {
            val allCustomers: List<Customer> = useCase.getAll()
            _customers.value = allCustomers
        }
    }

    fun onCustomerClick(customer: Customer) {
        _selected.value = customer.link
    }

    override fun onCleared() {
        compositeDisposable.clear()
        super.onCleared()
    }
}