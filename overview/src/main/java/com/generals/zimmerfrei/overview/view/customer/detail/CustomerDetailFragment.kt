package com.generals.zimmerfrei.overview.view.customer.detail

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.generals.zimmerfrei.common.utils.formatDateForTextView
import com.generals.zimmerfrei.model.Customer
import com.generals.zimmerfrei.overview.R
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_customer_detail.*
import org.threeten.bp.LocalDate
import java.util.*
import javax.inject.Inject

class CustomerDetailFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: CustomerDetailViewModel

    private lateinit var datePicker: DatePickerDialog

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(
                this,
                viewModelFactory
        ).get(CustomerDetailViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_customer_detail, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (savedInstanceState == null) {
            viewModel.customer.observe(viewLifecycleOwner, Observer { value: Customer ->
                updateView(value)
            })

            viewModel.birthDate.observe(viewLifecycleOwner, Observer { value: LocalDate? ->
                setUpBirthDate(value)
            })

            viewModel.isEditing.observe(viewLifecycleOwner, Observer { value: Boolean ->
                toolbar.menu.findItem(R.id.delete).isVisible = value
            })
        }

        val url: String? = arguments?.getString(CUSTOMER_URL_ARG_KEY)
        val titleId: Int = url?.let { R.string.customer_detail } ?: R.string.create_customer

        setUpToolbar(titleId)
        setUpListeners()

        viewModel.start(url)
    }

    private fun setUpBirthDate(value: LocalDate?) {
        datePicker = buildDatePicker(value) {
            viewModel.onBirthDateSelected(it)
            birth_date.setText(
                    formatDateForTextView(it.dayOfMonth, it.monthValue, it.year),
                    TextView.BufferType.NORMAL
            )
        }
        value?.also { date: LocalDate ->
            birth_date.setText(
                    formatDateForTextView(
                            date.dayOfMonth,
                            date.monthValue,
                            date.year
                    ),
                    TextView.BufferType.NORMAL
            )
        }
    }

    private fun updateView(value: Customer) {
        first_name.setText(value.firstName)
        last_name.setText(value.lastName)
        email.setText(value.email)
        mobile.setText(value.mobile)
        social_id.setText(value.socialId)
        address.setText(value.address)
        city.setText(value.city)
        province.setText(value.province)
        zip.setText(value.zip)
        state.setText(value.state)
        gender.setText(value.gender)
        birth_place.setText(value.birthPlace)
    }

    private fun setUpToolbar(@StringRes titleId: Int) {
        toolbar.setTitle(titleId)
        toolbar.inflateMenu(R.menu.menu_save_delete)
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp)
        toolbar.setNavigationOnClickListener {
            activity?.onBackPressed()
        }
        toolbar.menu.findItem(R.id.save)
                .setOnMenuItemClickListener { _: MenuItem? ->
                    submit()
                    true
                }
    }

    private fun setUpListeners() {
        birth_date.setOnClickListener { datePicker.show() }
        submit.setOnClickListener { submit() }
    }

    private fun buildDatePicker(
            date: LocalDate?,
            onDateSelected: (LocalDate) -> Unit
    ): DatePickerDialog {
        val calendar: Calendar = Calendar.getInstance()
        return DatePickerDialog(
                context,
                { _: DatePicker, year: Int, month: Int, day: Int ->
                    onDateSelected(LocalDate.of(year, month + 1, day))
                },
                date?.year ?: calendar.get(Calendar.YEAR),
                date?.let { it.monthValue - 1 } ?: calendar.get(Calendar.MONTH),
                date?.dayOfMonth ?: calendar.get(Calendar.DAY_OF_MONTH)
        )
    }

    private fun submit() {
        viewModel.submit(
                firstName = first_name.text.toString(),
                lastName = last_name.text.toString(),
                socialId = social_id.text.toString(),
                mobile = mobile.text.toString(),
                email = email.text.toString(),
                address = address.text.toString(),
                city = city.text.toString(),
                province = province.text.toString(),
                state = state.text.toString(),
                zip = zip.text.toString(),
                gender = gender.text.toString(),
                birthPlace = birth_place.text.toString()
        )
    }

    companion object {
        @JvmStatic
        fun newInstance(url: String?) =
                CustomerDetailFragment().apply {
                    arguments = Bundle().apply {
                        putString(CUSTOMER_URL_ARG_KEY, url)
                    }
                }
    }
}

const val CUSTOMER_URL_ARG_KEY = "CustomerDetailFragment.CUSTOMER_URL_ARG_KEY"
