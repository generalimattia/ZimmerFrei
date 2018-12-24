package com.generals.zimmerfrei.reservation.view

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.method.LinkMovementMethod
import android.text.util.Linkify
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.TextView
import com.generals.zimmerfrei.model.ParcelableDay
import com.generals.zimmerfrei.model.ParcelableRoomDay
import com.generals.zimmerfrei.reservation.R
import com.generals.zimmerfrei.reservation.viewmodel.ReservationViewModel
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_reservation.*
import java.util.*
import javax.inject.Inject

class ReservationFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: ReservationViewModel

    private lateinit var startDatePickerDialog: DatePickerDialog
    private lateinit var endDatePickerDialog: DatePickerDialog

    override fun onAttach(context: Context?) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(
                this,
                viewModelFactory
        )
                .get(ReservationViewModel::class.java)
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

        viewModel.startDate.observe(this,
                Observer { nullableDate: ParcelableDay? ->
                    startDatePickerDialog = buildStartDatePickerDialog(nullableDate)
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

        viewModel.endDate.observe(this,
                Observer { nullableDate: ParcelableDay? ->
                    endDatePickerDialog = buildEndDatePickerDialog(nullableDate)
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

        viewModel.name.observe(this,
                Observer { nullableValue: String? ->
                    nullableValue?.let {
                        name.setText(it, TextView.BufferType.NORMAL)
                    }
                })

        viewModel.email.observe(this,
                Observer { nullableValue: String? ->
                    nullableValue?.let {
                        email.setText(it, TextView.BufferType.NORMAL)
                    }
                })

        viewModel.mobile.observe(this,
                Observer { nullableValue: String? ->
                    nullableValue?.let {
                        mobile.setText(it, TextView.BufferType.NORMAL)
                    }
                })

        viewModel.adultsCount.observe(this,
                Observer { nullableValue: String? ->
                    nullableValue?.let {
                        adult_count.setText(it, TextView.BufferType.NORMAL)
                    }
                })

        viewModel.childrenCount.observe(this,
                Observer { nullableValue: String? ->
                    nullableValue?.let {
                        children_count.setText(it, TextView.BufferType.NORMAL)
                    }
                })

        viewModel.babiesCount.observe(this,
                Observer { nullableValue: String? ->
                    nullableValue?.let {
                        babies_count.setText(it, TextView.BufferType.NORMAL)
                    }
                })

        viewModel.color.observe(this,
                Observer { color: String? ->
                    color?.let {
                        color_view.setBackgroundColor(Color.parseColor(it))
                    }
                })

        viewModel.notes.observe(this,
                Observer { nullableValue: String? ->
                    nullableValue?.let {
                        notes.setText(it, TextView.BufferType.NORMAL)
                    }
                })

        viewModel.pressBack.observe(
                this,
                Observer { shouldPressBack: Boolean? ->
                    shouldPressBack?.let {
                        if (it) {
                            activity?.onBackPressed()
                        }
                    }
                })

        viewModel.roomError.observe(this,
                Observer { error: String? ->
                    error?.let {
                        room_input_layout.error = it
                    }
                })

        viewModel.startDateError.observe(this,
                Observer { error: String? ->
                    error?.let {
                        start_date_input_layout.error = it
                    }
                })

        viewModel.endDateError.observe(this,
                Observer { error: String? ->
                    error?.let {
                        end_date_input_layout.error = it
                    }
                })

        viewModel.nameError.observe(this,
                Observer { error: String? ->
                    error?.let {
                        name_input_layout.error = it
                    }
                })

        viewModel.rooms.observe(this,
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

        viewModel.selectedRoom.observe(this,
                Observer { nullableRoom: String? ->
                    nullableRoom?.let {
                        room.setText(it, TextView.BufferType.EDITABLE)
                    }
                }
        )

        viewModel.preselectedRoom.observe(this,
                Observer { selection: Int? ->
                    selection?.let {
                        room_spinner.setSelection(it)
                    }
                })

        viewModel.isEditing.observe(this,
                Observer { isEditing: Boolean? ->
                    isEditing?.let {
                        toolbar.menu.findItem(R.id.delete).isVisible = it
                    }
                })

        setupListeners()

        if (savedInstanceState == null) {
            viewModel.start(arguments?.getParcelable(RESERVATION))
        }
    }

    private fun setUpToolbar() {
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

    private fun buildDeleteReservationAlertDialog() =
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
                    }.let { it.create() }

    private fun buildStartDatePickerDialog(startDate: ParcelableDay?): DatePickerDialog {
        val calendar: Calendar = Calendar.getInstance()
        return DatePickerDialog(
                context,
                { _: DatePicker, year: Int, month: Int, day: Int ->

                    viewModel.onStartDateSelected(ParcelableDay(day, month + 1, year))

                    start_date.setText(
                            formatDateForTextView(day, month + 1, year),
                            TextView.BufferType.NORMAL
                    )
                },
                startDate?.year ?: calendar.get(Calendar.YEAR),
                startDate?.let { it.month - 1 } ?: calendar.get(Calendar.MONTH),
                startDate?.dayOfMonth ?: calendar.get(Calendar.DAY_OF_MONTH)
        )
    }

    private fun buildEndDatePickerDialog(endDate: ParcelableDay?): DatePickerDialog {
        val calendar: Calendar = Calendar.getInstance()
        return DatePickerDialog(
                context,
                { _: DatePicker, year: Int, month: Int, day: Int ->

                    viewModel.onEndDateSelected(ParcelableDay(day, month + 1, year))

                    end_date.setText(
                            formatDateForTextView(day, month + 1, year),
                            TextView.BufferType.NORMAL
                    )
                },
                endDate?.year ?: calendar.get(Calendar.YEAR),
                endDate?.let { it.month - 1 } ?: calendar.get(Calendar.MONTH),
                endDate?.dayOfMonth ?: calendar.get(Calendar.DAY_OF_MONTH)
        )
    }

    private fun formatDateForTextView(day: Int, month: Int, year: Int): String =
            DATE_FORMAT.format(
                    day.toString(),
                    month.toString(),
                    year.toString()
            )

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

        color_container.setOnClickListener {
            viewModel.generateNewColor()
        }

        action_email.setOnClickListener {
            activity?.let {
                viewModel.onSendEmailClick(email.text.toString(), it)
            }
        }

        action_dial.setOnClickListener {
            activity?.let {
                viewModel.onDialMobileClick(mobile.text.toString(), it)
            }
        }

        submit.setOnClickListener {
            submit()
        }
    }

    private fun submit() {
        viewModel.submit(
                name = name.text.toString(),
                adults = adult_count.text.toString(),
                children = children_count.text.toString(),
                babies = children_count.text.toString(),
                notes = notes.text.toString(),
                email = email.text.toString(),
                mobile = mobile.text.toString(),
                roomName = room.text.toString()
        )
    }

    companion object {
        fun newInstance(reservation: ParcelableRoomDay?) = ReservationFragment().apply {
            val arguments = Bundle().apply {
                reservation?.let { putParcelable(RESERVATION, reservation) }
            }
            setArguments(arguments)
        }

        private const val DATE_FORMAT = "%s/%s/%s"
    }

}
