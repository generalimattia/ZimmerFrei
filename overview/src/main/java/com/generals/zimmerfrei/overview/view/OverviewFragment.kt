package com.generals.zimmerfrei.overview.view

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.generals.zimmerfrei.model.Day
import com.generals.zimmerfrei.model.Room
import com.generals.zimmerfrei.overview.R
import com.generals.zimmerfrei.overview.view.layout.SyncScroller
import com.generals.zimmerfrei.overview.viewmodel.OverviewViewModel
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_time_plan.*
import javax.inject.Inject

class OverviewFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: OverviewViewModel

    override fun onAttach(context: Context?) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(OverviewViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? = inflater.inflate(
        R.layout.fragment_time_plan, container, false
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        /*calendar.bind(mutableListOf())

        viewModel.days.observe(this, Observer { day: DayWithReservations? ->
            day?.let {
                calendar.update(it.day)
            }
        })*/

        viewModel.rooms.observe(this, Observer { rooms: List<Room>? ->
            rooms?.let {
                //rooms_list_view.bind(it)
                rooms_list_view.bind(List(30) { index: Int -> Room("Camera $index") })
            }
        })

        val days = MutableList(20) { _: Int -> Day() }

        SyncScroller().bindFirst(days_list_view.recyclerView)
            .bindSecond(plan.recyclerView)
            .sync()

        val syncScroller = SyncScroller().bindFirst(rooms_list_view.recyclerView)

        plan.bind(days, syncScroller)
        days_list_view.bind(days)



        add_fab.setOnClickListener {
            activity?.let {
                viewModel.onFABClick(it)
            }
        }

        if (savedInstanceState == null) {
            viewModel.start()
        }
    }


    companion object {
        fun newInstance() = OverviewFragment()
    }
}
