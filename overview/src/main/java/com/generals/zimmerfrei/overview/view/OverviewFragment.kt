package com.generals.zimmerfrei.overview.view

import android.animation.ObjectAnimator
import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.DatePicker
import androidx.core.animation.addListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.generals.zimmerfrei.model.Day
import com.generals.zimmerfrei.model.ParcelableDay
import com.generals.zimmerfrei.model.Room
import com.generals.zimmerfrei.model.RoomDay
import com.generals.zimmerfrei.navigator.Navigator
import com.generals.zimmerfrei.overview.R
import com.generals.zimmerfrei.overview.view.adapter.DaysAdapter
import com.generals.zimmerfrei.overview.view.adapter.ReservationsAdapter
import com.generals.zimmerfrei.overview.view.adapter.RoomsAdapter
import com.generals.zimmerfrei.overview.view.layout.SyncScroller
import com.generals.zimmerfrei.overview.viewmodel.OverviewViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_overview.*
import org.threeten.bp.LocalDate
import javax.inject.Inject

class OverviewFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var navigator: Navigator

    private lateinit var viewModel: OverviewViewModel

    private var datePickerDialog: DatePickerDialog? = null

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(
                this,
                viewModelFactory
        ).get(OverviewViewModel::class.java)
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? = inflater.inflate(
            R.layout.fragment_overview,
            container,
            false
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        setUpToolbar()

        SyncScroller().bindFirst(days_recycler_view)
                .bindSecond(plan)
                .sync()

        val syncScroller = SyncScroller().bindFirst(rooms_recycler_view)

        viewModel.selectedDate.observe(viewLifecycleOwner,
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

        viewModel.days.observe(viewLifecycleOwner,
                Observer { days: List<Day>? ->
                    days?.let {
                        days_recycler_view.adapter = DaysAdapter(it)
                    }
                })

        viewModel.rooms.observe(viewLifecycleOwner,
                Observer { rooms: List<Room>? ->
                    rooms?.let {
                        rooms_recycler_view.adapter = RoomsAdapter(it)
                    }
                })

        viewModel.reservations.observe(
                viewLifecycleOwner,
                Observer { roomDays: List<Pair<Room, List<RoomDay>>>? ->

                    roomDays?.let {
                        plan.adapter = ReservationsAdapter(
                                roomDays,
                                syncScroller
                        ) { day: RoomDay ->

                            activity?.let {
                                navigator.reservation(
                                        selectedDay = ParcelableDay(day.day),
                                        selectedRoom = day.room,
                                        reservationURL = day.reservationURL
                                )
                                        .startNewActivity(it)
                            }
                        }

                        ObjectAnimator.ofInt(progress, "progress", progress.progress, progress.max).apply {
                            duration = 1000
                            interpolator = AccelerateDecelerateInterpolator()
                            addListener(
                                    onStart = {
                                        progress.visibility = View.VISIBLE
                                    },
                                    onEnd = {
                                        rooms_recycler_view.visibility = View.VISIBLE
                                        plan.visibility = View.VISIBLE
                                        progress.visibility = View.GONE
                                        progress.progress = 0
                                    })
                        }.start()
                    }
                })

        viewModel.month.observe(viewLifecycleOwner,
                Observer { month: String? ->
                    month?.let {
                        month_and_year.text = it.capitalize()
                    }
                })

        viewModel.result.observe(viewLifecycleOwner, Observer { value: String? ->
            value?.also {
                Snackbar.make(root_view, it, Snackbar.LENGTH_LONG)
                        .show()
            }

        })

        add_fab.setOnClickListener {
            activity?.also {
                navigator.reservation()
                        .startNewActivity(it)
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

        viewModel.start()
    }

    private fun setUpToolbar() {
        toolbar.inflateMenu(R.menu.menu_overview)
        toolbar.menu.findItem(R.id.rooms)
                .setOnMenuItemClickListener { _: MenuItem? ->
                    activity?.also {
                        navigator.roomList(R.id.fragment_container)
                                .start(
                                        activity = it,
                                        containerViewId = R.id.fragment_container,
                                        addToBackStack = true
                                )
                    }
                    true
                }
    }

    companion object {
        fun newInstance() = OverviewFragment()
    }
}
