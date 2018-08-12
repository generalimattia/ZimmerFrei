package com.generals.zimmerfrei.reservation.view

import android.app.DatePickerDialog
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.TextView
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

    private val startDatePickerDialog: DatePickerDialog by lazy {
        val calendar: Calendar = Calendar.getInstance()
        DatePickerDialog(
            context,
            { _: DatePicker, year: Int, month: Int, day: Int ->

                start_date.setText(
                    DATE_FORMAT.format(
                        day.toString(),
                        (month + 1).toString(),
                        year.toString()
                    ),
                    TextView.BufferType.NORMAL
                )
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
    }

    private val endDatePickerDialog: DatePickerDialog by lazy {
        val calendar: Calendar = Calendar.getInstance()
        calendar.add(
            Calendar.DAY_OF_MONTH,
            1
        )
        val picker = DatePickerDialog(
            context,
            { _: DatePicker, year: Int, month: Int, day: Int ->

                end_date.setText(
                    DATE_FORMAT.format(
                        day.toString(),
                        (month + 1).toString(),
                        year.toString()
                    ),
                    TextView.BufferType.NORMAL
                )
            },
            calendar.get(Calendar.DAY_OF_MONTH),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.YEAR)
        )
        picker.datePicker.minDate = calendar.timeInMillis
        picker
    }

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

        viewModel.color.observe(this,
                                Observer { color: String? ->
                                    color?.let {
                                        color_view.setBackgroundColor(Color.parseColor(it))
                                    }
                                })

        start_date.setOnClickListener {
            startDatePickerDialog.show()
        }

        end_date.setOnClickListener {
            endDatePickerDialog.show()
        }

        color_container.setOnClickListener {
            viewModel.generateNewColor()
        }

        submit.setOnClickListener {
            submit()
        }

        if (savedInstanceState == null) {
            viewModel.start()
        }
    }

    private fun setUpToolbar() {
        toolbar.inflateMenu(R.menu.menu_save)
        toolbar.menu.findItem(R.id.save)
            .setOnMenuItemClickListener { _: MenuItem? ->
                submit()
                true
            }
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp)
        toolbar.setNavigationOnClickListener {
            activity?.onBackPressed()
        }
    }

    private fun submit() {
        viewModel.submit(
            name = name.text.toString(),
            startDate = start_date.text.toString(),
            endDate = end_date.text.toString(),
            adults = adult_count.text.toString(),
            children = children_count.text.toString(),
            babies = children_count.text.toString(),
            notes = notes.text.toString(),
            email = email.text.toString(),
            mobile = mobile.text.toString(),
            room = room.text.toString()
        )
    }

    companion object {
        fun newInstance() = ReservationFragment()

        private const val DATE_FORMAT = "%s/%s/%s"
    }

}
