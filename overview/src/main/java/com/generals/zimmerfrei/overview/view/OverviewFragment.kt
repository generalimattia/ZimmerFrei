package com.generals.zimmerfrei.overview.view

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.generals.zimmerfrei.model.Day
import com.generals.zimmerfrei.overview.R
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
        })

        if (savedInstanceState == null) {
            viewModel.start()
        }*/

        plan.bind(MutableList(20) { _: Int -> Day() })

        add_fab.setOnClickListener {
            activity?.let {
                viewModel.onFABClick(it)
            }
        }
    }


    companion object {
        fun newInstance() = OverviewFragment()
    }
}
