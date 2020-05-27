package com.generals.zimmerfrei.overview.view.customer.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import arrow.core.Option
import com.generals.zimmerfrei.listeners.ActionResult
import com.generals.zimmerfrei.model.Customer
import com.generals.zimmerfrei.listeners.CustomerActionEmitter
import com.generals.zimmerfrei.overview.view.customer.usecase.CustomerUseCase
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDate
import javax.inject.Inject

class CustomerDetailViewModel @Inject constructor(
        private val useCase: CustomerUseCase,
        private val customerActionEmitter: CustomerActionEmitter
) : ViewModel() {

    val customer: LiveData<Customer>
        get() = _customer
    private val _customer: MutableLiveData<Customer> = MutableLiveData()

    val birthDate: LiveData<LocalDate>
        get() = _birthDate
    private val _birthDate: MutableLiveData<LocalDate> = MutableLiveData()

    val isEditing: LiveData<Boolean>
        get() = _isEditing
    private val _isEditing: MutableLiveData<Boolean> = MutableLiveData()

    val message: LiveData<String>
        get() = _message
    private val _message: MutableLiveData<String> = MutableLiveData()

    val pressBack: LiveData<Boolean>
        get() = _pressBack
    private val _pressBack: MutableLiveData<Boolean> = MutableLiveData()

    private var url: String? = null
    private var currentCustomer: Option<Customer> = Option.empty()
    private var currentBirthDate: LocalDate? = null

    fun start(url: String?) {
        this.url = url
        viewModelScope.launch {
            currentCustomer = url?.let { useCase.get(it) } ?: Option.empty()
            currentCustomer.fold(
                    ifSome = {
                        _customer.value = it
                        _isEditing.value = true
                    },
                    ifEmpty = {
                        _isEditing.value = false
                    })
            currentBirthDate = currentCustomer.map { it.birthDate }.orNull()
            _birthDate.value = currentBirthDate
        }
    }

    fun onBirthDateSelected(date: LocalDate) {
        currentBirthDate = date
    }

    fun submit(
            firstName: String,
            lastName: String,
            socialId: String,
            mobile: String,
            email: String,
            address: String,
            city: String,
            province: String,
            state: String,
            zip: String,
            gender: String,
            birthPlace: String
    ) {
        viewModelScope.launch {
            val birthDate: LocalDate = currentBirthDate ?: LocalDate.now()
            val result: ActionResult<Customer> = currentCustomer.fold(
                    ifSome = { customer: Customer ->
                        useCase.update(customer.copy(
                                firstName = firstName,
                                lastName = lastName,
                                socialId = socialId,
                                mobile = mobile,
                                email = email,
                                address = address,
                                city = city,
                                province = province,
                                state = state,
                                zip = zip,
                                gender = gender,
                                birthDate = birthDate,
                                birthPlace = birthPlace
                        ))

                    },
                    ifEmpty = {
                        useCase.save(Customer(
                                firstName = firstName,
                                lastName = lastName,
                                socialId = socialId,
                                mobile = mobile,
                                email = email,
                                address = address,
                                city = city,
                                province = province,
                                state = state,
                                zip = zip,
                                gender = gender,
                                birthDate = birthDate,
                                birthPlace = birthPlace
                        ))
                    }
            )
            handleResult(result)
        }
    }

    fun delete() {
        viewModelScope.launch {
            url?.let { useCase.delete(it) }?.also(this@CustomerDetailViewModel::handleResult)
        }
    }

    private fun handleResult(result: ActionResult<Customer>) {
        if (result is ActionResult.Error) {
            _message.value = result.message
        } else {
            customerActionEmitter.emit(result)
            _pressBack.value = true
        }
    }
}