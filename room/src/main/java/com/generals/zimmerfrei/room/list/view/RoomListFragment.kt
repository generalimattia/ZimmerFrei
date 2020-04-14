package com.generals.zimmerfrei.room.list.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.generals.zimmerfrei.model.Room
import com.generals.zimmerfrei.navigator.Navigator
import com.generals.zimmerfrei.room.R
import com.generals.zimmerfrei.room.list.view.adapter.RoomsAdapter
import com.generals.zimmerfrei.room.list.viewmodel.RoomListViewModel
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_room_list.*
import javax.inject.Inject

class RoomListFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var navigator: Navigator

    private lateinit var viewModel: RoomListViewModel

    private var containerViewId: Int = 0

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(
                this,
                viewModelFactory
        )
                .get(RoomListViewModel::class.java)
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? = inflater.inflate(
            R.layout.fragment_room_list,
            container,
            false
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        containerViewId = arguments?.getInt(CONTAINER_VIEW_BUNDLE_KEY) ?: 0

        setUpToolbar()

        viewModel.allRooms.observe(viewLifecycleOwner,
                Observer { rooms: List<Room>? ->
                    rooms?.let {
                        recycler_view.adapter = RoomsAdapter(
                                rooms = it,
                                onClickListener = { room: Room ->
                                    activity?.let {
                                        navigator.roomDetail(room)
                                                .start(
                                                        it,
                                                        containerViewId,
                                                        true
                                                )
                                    }
                                },
                                onDeleteClickListener = viewModel::onDeleteRoomClick
                        )
                    }
                })

        add_fab.setOnClickListener {
            activity?.let {
                navigator.roomDetail()
                        .start(
                                it,
                                containerViewId,
                                true
                        )
            }
        }

        if (savedInstanceState == null) {
            viewModel.start()
        }
    }

    private fun setUpToolbar() {
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp)
        toolbar.setNavigationOnClickListener {
            activity?.onBackPressed()
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
