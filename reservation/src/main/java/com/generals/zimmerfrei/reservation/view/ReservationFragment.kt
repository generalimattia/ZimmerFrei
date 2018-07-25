package com.generals.zimmerfrei.reservation.view

import android.app.DatePickerDialog
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker

import com.generals.zimmerfrei.reservation.R
import com.generals.zimmerfrei.reservation.viewmodel.ReservationViewModel
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_reservation.*
import javax.inject.Inject

class ReservationFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: ReservationViewModel

    private val startDatePickerDialog: DatePickerDialog by lazy {
        DatePickerDialog(context, { _: DatePicker, year: Int, month: Int, day: Int ->
            viewModel.setStartDate(year, month, day)
        }, 0, 0, 0)
    }

    private val endDatePickerDialog: DatePickerDialog by lazy {
        DatePickerDialog(context, { _: DatePicker, year: Int, month: Int, day: Int ->
            viewModel.setEndDate(year, month, day)
        }, 0, 0, 0)
    }

    override fun onAttach(context: Context?) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(ReservationViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_reservation, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        start_date_input_layout.setOnClickListener {
            startDatePickerDialog.show()
        }

        end_date_input_layout.setOnClickListener {
            endDatePickerDialog.show()
        }

        submit.setOnClickListener {
            viewModel.submit()
        }
    }

    companion object {
        fun newInstance() = ReservationFragment()
    }

}
