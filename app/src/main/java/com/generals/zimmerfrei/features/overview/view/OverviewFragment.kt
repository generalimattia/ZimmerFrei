package com.generals.zimmerfrei.features.overview.view


import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.generals.zimmerfrei.R
import com.generals.zimmerfrei.ZimmerFreiApplication
import com.generals.zimmerfrei.features.overview.viewmodel.OverviewViewModel
import com.generals.overview.model.Day
import kotlinx.android.synthetic.main.fragment_overview.*
import javax.inject.Inject

class OverviewFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: OverviewViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context?.let { ZimmerFreiApplication.applicationComponent(it).inject(this) }
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(OverviewViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? = inflater.inflate(R.layout.fragment_overview,
                                                                                     container,
                                                                                     false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        viewModel.days.observe(this, android.arch.lifecycle.Observer { days: List<Day>? ->
            days?.let { calendar.bind(days) }
        })

        if(savedInstanceState == null) {
            viewModel.start()
        }
    }


    companion object {
        fun newInstance() = OverviewFragment()
    }
}
