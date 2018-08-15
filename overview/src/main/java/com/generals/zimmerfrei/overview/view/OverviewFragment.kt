package com.generals.zimmerfrei.overview.view

import android.app.DatePickerDialog
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import com.generals.zimmerfrei.model.Day
import com.generals.zimmerfrei.model.Room
import com.generals.zimmerfrei.model.RoomDay
import com.generals.zimmerfrei.overview.R
import com.generals.zimmerfrei.overview.view.layout.SyncScroller
import com.generals.zimmerfrei.overview.viewmodel.OverviewViewModel
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_time_plan.*
import org.threeten.bp.LocalDate
import javax.inject.Inject

class OverviewFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: OverviewViewModel

    private var datePickerDialog: DatePickerDialog? = null

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
            .get(OverviewViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? = inflater.inflate(
        R.layout.fragment_time_plan,
        container,
        false
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        setUpToolbar()

        viewModel.selectedDate.observe(this,
                                       Observer { date: LocalDate? ->
                                           date?.let {
                                               val localPicker: DatePickerDialog =
                                                   datePickerDialog.takeIf { it != null }
                                                           ?: DatePickerDialog(
                                                               context,
                                                               { _: DatePicker, year: Int, month: Int, day: Int ->
                                                                   viewModel.onNewDate(
                                                                       month,
                                                                       year
                                                                   )
                                                               },
                                                               it.year,
                                                               it.monthValue - 1,
                                                               it.dayOfMonth
                                                           )
                                               localPicker.datePicker.updateDate(
                                                   it.year,
                                                   it.monthValue - 1,
                                                   it.dayOfMonth
                                               )
                                               datePickerDialog = localPicker
                                           }
                                       })

        viewModel.rooms.observe(this,
                                Observer { rooms: List<Room>? ->
                                    rooms?.let {
                                        //rooms_list_view.bind(it)
                                        rooms_list_view.bind(List(30) { index: Int -> Room(id = index.toLong(), name = index.toString()) })
                                    }
                                })

        viewModel.days.observe(this,
                               Observer { days: List<Day>? ->
                                   days?.let {
                                       days_list_view.bind(it)
                                   }
                               })

        viewModel.month.observe(this,
                                Observer { month: String? ->
                                    month?.let {
                                        month_and_year.text = it.capitalize()
                                    }
                                })

        val days =
            MutableList(31) { _: Int -> Room() to MutableList(31) { _: Int -> RoomDay.EmptyDay() } }

        SyncScroller().bindFirst(days_list_view.recyclerView)
            .bindSecond(plan.recyclerView)
            .sync()

        val syncScroller = SyncScroller().bindFirst(rooms_list_view.recyclerView)

        plan.bind(
            days,
            syncScroller
        )

        add_fab.setOnClickListener {
            activity?.let {
                viewModel.onFABClick(it)
            }
        }

        month_and_year.setOnClickListener {
            datePickerDialog?.show()
        }

        previous_month.setOnClickListener {
            viewModel.onPreviousMonthClick()
        }

        next_month.setOnClickListener {
            viewModel.onNextMonthClick()
        }

        if (savedInstanceState == null) {
            viewModel.start()
        }
    }

    private fun setUpToolbar() {
        toolbar.inflateMenu(R.menu.menu_overview)
        toolbar.menu.findItem(R.id.rooms)
            .setOnMenuItemClickListener { _: MenuItem? ->
                activity?.let {
                    viewModel.onRoomsMenuItemClick(
                        it as AppCompatActivity,
                        R.id.fragment_container
                    )
                }
                true
            }
    }

    companion object {
        fun newInstance() = OverviewFragment()
    }
}
