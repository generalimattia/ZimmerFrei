package com.generals.zimmerfrei.room.list.view

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.support.annotation.IdRes
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.generals.zimmerfrei.model.Room
import com.generals.zimmerfrei.room.R

import com.generals.zimmerfrei.room.list.view.adapter.RoomsAdapter
import com.generals.zimmerfrei.room.list.viewmodel.RoomListViewModel
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_room_list.*
import javax.inject.Inject

class RoomListFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: RoomListViewModel

    private var containerViewId: Int = 0

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
            .get(RoomListViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(
            R.layout.fragment_room_list,
            container,
            false
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        containerViewId = arguments?.getInt(CONTAINER_VIEW_BUNDLE_KEY) ?: 0

        viewModel.allRooms.observe(this,
                                   Observer { rooms: List<Room>? ->
                                       rooms?.let {
                                           recycler_view.adapter = RoomsAdapter(it)
                                       }
                                   })

        add_fab.setOnClickListener {
            activity?.let {
                viewModel.onFABClick(
                    it as AppCompatActivity,
                    containerViewId
                )
            }
        }

        if (savedInstanceState == null) {
            viewModel.start()
        }
    }


    companion object {
        fun newInstance(@IdRes containerViewId: Int) = RoomListFragment().apply {
            arguments = Bundle(1).apply {
                putInt(
                    CONTAINER_VIEW_BUNDLE_KEY,
                    containerViewId
                )
            }
        }

        private const val CONTAINER_VIEW_BUNDLE_KEY = "RoomListFragment.CONTAINER_VIEW_BUNDLE_KEY"
    }
}
