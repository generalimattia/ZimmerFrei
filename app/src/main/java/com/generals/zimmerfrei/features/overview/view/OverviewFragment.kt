package com.generals.zimmerfrei.features.overview.view


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.generals.zimmerfrei.R
import com.generals.zimmerfrei.model.Day
import kotlinx.android.synthetic.main.fragment_overview.*
import java.text.SimpleDateFormat
import java.util.*

class OverviewFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? = inflater.inflate(R.layout.fragment_overview,
                                                                                     container,
                                                                                     false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val calendarHaolder = Calendar.getInstance()
        val numOfDays = calendarHaolder.getActualMaximum(Calendar.DAY_OF_MONTH)
        val dateFormat = SimpleDateFormat("EEEE", Locale.getDefault())
        val days: List<Day> = MutableList(numOfDays, { index: Int ->
            calendarHaolder.set(Calendar.DAY_OF_MONTH, index)
            Day(dateFormat.format(calendarHaolder.time))
        })
        calendar.bind(days)
    }


    companion object {
        fun newInstance() = OverviewFragment()
    }
}
