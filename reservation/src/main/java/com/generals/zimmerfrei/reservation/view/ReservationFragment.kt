package com.generals.zimmerfrei.reservation.view

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.transition.Slide
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.generals.zimmerfrei.common.utils.availableColors
import com.generals.zimmerfrei.common.utils.formatDateForTextView
import com.generals.zimmerfrei.model.Customer
import com.generals.zimmerfrei.model.ParcelableDay
import com.generals.zimmerfrei.model.Reservation
import com.generals.zimmerfrei.model.Room
import com.generals.zimmerfrei.navigator.Navigator
import com.generals.zimmerfrei.reservation.R
import com.generals.zimmerfrei.reservation.view.adapters.ColorItem
import com.generals.zimmerfrei.reservation.view.adapters.ColorsAdapter
import com.generals.zimmerfrei.reservation.viewmodel.ReservationViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_reservation.*
import java.util.*
import javax.inject.Inject

class ReservationFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var navigator: Navigator

    private lateinit var viewModel: ReservationViewModel

    private lateinit var startDatePickerDialog: DatePickerDialog
    private lateinit var endDatePickerDialog: DatePickerDialog

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(
                this,
                viewModelFactory
        ).get(ReservationViewModel::class.java)
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? = inflater.inflate(
            R.layout.fragment_reservation,
            container,
            false
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(
                view,
                savedInstanceState
        )

        setUpToolbar()

        if (savedInstanceState == null) {
            viewModel.startDate.observe(viewLifecycleOwner,
                    Observer { nullableDate: ParcelableDay? ->
                        startDatePickerDialog = buildDatePicker(nullableDate) {
                            viewModel.onStartDateSelected(it)
                            start_date.setText(
                                    formatDateForTextView(it.dayOfMonth, it.month, it.year),
                                    TextView.BufferType.NORMAL
                            )
                        }
                        nullableDate?.let {
                            start_date.setText(
                                    formatDateForTextView(
                                            it.dayOfMonth,
                                            it.month,
                                            it.year
                                    ),
                                    TextView.BufferType.NORMAL
                            )
                        }
                    })

            viewModel.endDate.observe(viewLifecycleOwner,
                    Observer { nullableDate: ParcelableDay? ->
                        endDatePickerDialog = buildDatePicker(nullableDate) {
                            viewModel.onEndDateSelected(it)
                            end_date.setText(
                                    formatDateForTextView(it.dayOfMonth, it.month, it.year),
                                    TextView.BufferType.NORMAL
                            )
                        }
                        nullableDate?.let {
                            end_date.setText(
                                    formatDateForTextView(
                                            it.dayOfMonth,
                                            it.month,
                                            it.year
                                    ),
                                    TextView.BufferType.NORMAL
                            )
                        }
                    })

            viewModel.reservation.observe(viewLifecycleOwner,
                    Observer { value: Reservation ->
                        name.setText(value.name, TextView.BufferType.NORMAL)
                        adult_count.setText(value.adults, TextView.BufferType.NORMAL)
                        children_count.setText(value.children, TextView.BufferType.NORMAL)
                        babies_count.setText(value.babies, TextView.BufferType.NORMAL)
                        notes.setText(value.notes, TextView.BufferType.NORMAL)
                    })

            viewModel.selectedColor.observe(viewLifecycleOwner,
                    Observer { colorItem: ColorItem? ->
                        colorItem?.let { selected: ColorItem ->
                            (colors.adapter as ColorsAdapter).update(selected)
                        }
                    })

            viewModel.pressBack.observe(
                    viewLifecycleOwner,
                    Observer { shouldPressBack: Boolean? ->
                        shouldPressBack?.let {
                            if (it) {
                                activity?.onBackPressed()
                            }
                        }
                    })

            viewModel.roomError.observe(viewLifecycleOwner,
                    Observer { error: String? ->
                        error?.let {
                            room_input_layout.error = it
                        }
                    })

            viewModel.startDateError.observe(viewLifecycleOwner,
                    Observer { error: String? ->
                        error?.let {
                            start_date_input_layout.error = it
                        }
                    })

            viewModel.endDateError.observe(viewLifecycleOwner,
                    Observer { error: String? ->
                        error?.let {
                            end_date_input_layout.error = it
                        }
                    })

            viewModel.nameError.observe(viewLifecycleOwner,
                    Observer { error: String? ->
                        error?.let {
                            name_input_layout.error = it
                        }
                    })

            viewModel.rooms.observe(viewLifecycleOwner,
                    Observer { rooms: List<String>? ->
                        rooms?.let {
                            room_spinner.adapter = ArrayAdapter(context,
                                    android.R.layout.simple_spinner_item,
                                    it).apply {
                                setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                            }

                            room_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                                override fun onNothingSelected(parent: AdapterView<*>?) {}

                                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                                    viewModel.onRoomSelected(position)
                                }
                            }
                        }
                    })

            viewModel.selectedRoom.observe(viewLifecycleOwner,
                    Observer { nullableRoom: String? ->
                        nullableRoom?.let {
                            room.setText(it, TextView.BufferType.EDITABLE)
                        }
                    }
            )

            viewModel.preselectedRoom.observe(viewLifecycleOwner,
                    Observer { selection: Int? ->
                        selection?.let {
                            room_spinner.setSelection(it)
                        }
                    })

            viewModel.isEditing.observe(viewLifecycleOwner,
                    Observer { isEditing: Boolean? ->
                        isEditing?.let {
                            toolbar.menu.findItem(R.id.delete).isVisible = it
                        }
                    })

            viewModel.result.observe(viewLifecycleOwner,
                    Observer { value: String? ->
                        value?.also {
                            Snackbar.make(root, it, Snackbar.LENGTH_LONG)
                                    .show()
                        }
                    })

            viewModel.customer.observe(viewLifecycleOwner,
                    Observer { value: Customer? ->
                        if (value != null) {
                            showCustomerDetails(value)
                        } else {
                            hideCustomerDetails()
                        }
                    })

            viewModel.pressBack.observe(viewLifecycleOwner, Observer { value: Boolean ->
                if (value) {
                    pressBack()
                }
            })
        }

        setupListeners()

        val selectedDay: ParcelableDay? = arguments?.getParcelable(SELECTED_DAY_KEY)
        val selectedRoom: Room? = arguments?.getParcelable(SELECTED_ROOM_KEY)
        val url: String? = arguments?.getString(RESERVATION_URL_KEY)

        viewModel.start(selectedDay, selectedRoom, url)
    }

    private fun showCustomerDetails(value: Customer) {
        add_customer.visibility = View.GONE
        customer_details.visibility = View.VISIBLE
        customer_details_header.visibility = View.VISIBLE
        customer_name_input_layout.visibility = View.VISIBLE
        email_input_layout.visibility = View.VISIBLE
        action_email.visibility = View.VISIBLE
        mobile_input_layout.visibility = View.VISIBLE
        action_dial.visibility = View.VISIBLE
        edit_customer.visibility = View.VISIBLE

        customer_name.setText("${value.firstName} ${value.lastName}")
        email.setText(value.email)
        mobile.setText(value.mobile)
    }

    private fun hideCustomerDetails() {
        add_customer.visibility = View.VISIBLE
        customer_details.visibility = View.GONE
        customer_details_header.visibility = View.GONE
        customer_name_input_layout.visibility = View.GONE
        email_input_layout.visibility = View.GONE
        action_email.visibility = View.GONE
        mobile_input_layout.visibility = View.GONE
        action_dial.visibility = View.GONE
        edit_customer.visibility = View.GONE
    }

    private fun setUpToolbar() {
        toolbar.setTitle(R.string.create_reservation)
        toolbar.inflateMenu(R.menu.menu_save_delete)
        toolbar.menu.findItem(R.id.save)
                .setOnMenuItemClickListener { _: MenuItem? ->
                    submit()
                    true
                }
        toolbar.menu.findItem(R.id.delete)
                .setOnMenuItemClickListener { _: MenuItem? ->
                    val dialog: AlertDialog = buildDeleteReservationAlertDialog()
                    dialog.show()
                    true
                }
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp)
        toolbar.setNavigationOnClickListener {
            activity?.onBackPressed()
        }
    }

    private fun buildDeleteReservationAlertDialog(): AlertDialog =
            AlertDialog.Builder(context)
                    .apply {
                        setMessage(R.string.delete_reservation_dialog_message)
                        setPositiveButton(R.string.yes) { dialog: DialogInterface?, _: Int ->
                            viewModel.delete()
                            dialog?.dismiss()
                        }
                        setNegativeButton(R.string.no) { dialog: DialogInterface?, _: Int ->
                            dialog?.dismiss()
                        }
                    }.create()

    private fun buildDatePicker(
            date: ParcelableDay?,
            onDateSelected: (ParcelableDay) -> Unit
    ): DatePickerDialog {
        val calendar: Calendar = Calendar.getInstance()
        return DatePickerDialog(
                context,
                { _: DatePicker, year: Int, month: Int, day: Int ->
                    onDateSelected(ParcelableDay(day, month + 1, year))
                },
                date?.year ?: calendar.get(Calendar.YEAR),
                date?.let { it.month - 1 } ?: calendar.get(Calendar.MONTH),
                date?.dayOfMonth ?: calendar.get(Calendar.DAY_OF_MONTH)
        )
    }

    private fun setupListeners() {
        room.setOnClickListener {
            room_spinner.performClick()
        }

        start_date.setOnClickListener {
            startDatePickerDialog.show()
        }

        end_date.setOnClickListener {
            endDatePickerDialog.show()
        }

        colors.adapter = ColorsAdapter(availableColors.map { ColorItem(it, false) }, viewModel::onColorClick)

        add_customer.setOnClickListener { navigateToCustomerList() }

        edit_customer.setOnClickListener { navigateToCustomerList() }
    }

    private fun navigateToCustomerList() {
        activity?.also {
            navigator.customerList(
                    enterTransition = Slide(Gravity.END),
                    exitTransition = Slide(Gravity.END)
            )
                    .start(
                            it,
                            R.id.fragment_container,
                            true
                    )
        }
    }

    private fun submit() {
        viewModel.submit(
                name = name.text.toString(),
                adults = adult_count.text.toString(),
                children = children_count.text.toString(),
                babies = babies_count.text.toString(),
                notes = notes.text.toString(),
                roomName = room.text.toString()
        )
    }

    private fun pressBack() {
        activity?.also { it.onBackPressed() }
    }

    companion object {
        fun newInstance(
                selectedDay: ParcelableDay?,
                selectedRoom: Room?,
                reservationURL: String?
        ) = ReservationFragment().apply {
            val arguments = Bundle().apply {
                selectedDay?.let { putParcelable(SELECTED_DAY_KEY, it) }
                selectedRoom?.let { putParcelable(SELECTED_ROOM_KEY, it) }
                reservationURL?.let { putString(RESERVATION_URL_KEY, it) }
            }
            setArguments(arguments)
        }
    }

}
