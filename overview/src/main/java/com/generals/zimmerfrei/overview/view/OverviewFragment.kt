package com.generals.zimmerfrei.overview.view

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.generals.zimmerfrei.overview.R
import com.generals.zimmerfrei.overview.model.Day
import com.generals.zimmerfrei.overview.viewmodel.OverviewViewModel
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_overview.*
import javax.inject.Inject

class OverviewFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: OverviewViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(OverviewViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? = inflater.inflate(
        R.layout.fragment_overview, container, false
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        viewModel.days.observe(this, android.arch.lifecycle.Observer { days: List<Day>? ->
            days?.let { calendar.bind(days) }
        })

        if (savedInstanceState == null) {
            viewModel.start()
        }
    }


    companion object {
        fun newInstance() = OverviewFragment()
    }
}
