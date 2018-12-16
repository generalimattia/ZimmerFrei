package com.generals.zimmerfrei.room.detail.view

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.generals.zimmerfrei.common.extension.hideKeyboard
import com.generals.zimmerfrei.model.Room
import com.generals.zimmerfrei.room.R
import com.generals.zimmerfrei.room.detail.viewmodel.RoomDetailViewModel
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_room_detail.*
import javax.inject.Inject

class RoomDetailFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: RoomDetailViewModel

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
                .get(RoomDetailViewModel::class.java)
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? = inflater.inflate(
            R.layout.fragment_room_detail,
            container,
            false
    )


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setUpToolbar()

        viewModel.pressBack.observe(this,
                Observer { shouldPressBack: Boolean? ->
                    shouldPressBack?.let {
                        if (it) {
                            activity?.onBackPressed()
                        }
                    }
                })

        viewModel.room.observe(this,
                Observer { room: Room? ->
                    room?.let {

                        toolbar.title =
                                "${resources.getString(R.string.room)} ${room.name}"

                        name.setText(
                                room.name,
                                TextView.BufferType.EDITABLE
                        )
                        persons.setText(
                                room.personsCount.toString(),
                                TextView.BufferType.EDITABLE
                        )
                        double_bed.isChecked = room.isDouble
                        single_bed.isChecked = room.isSingle
                        handicap.isChecked = room.isHandicap
                        balcony.isChecked = room.hasBalcony
                    }
                })

        viewModel.errorOnName.observe(this,
                Observer { error: String? ->
                    error?.let {
                        room_input_layout.error = it
                    }
                })

        val room: Room? = arguments?.getParcelable(ROOM_BUNDLE_KEY)

        setUpListeners()

        if (savedInstanceState == null) {
            viewModel.start(room)
        }
    }

    private fun setUpListeners() {
        double_bed.setOnClickListener {
            it.hideKeyboard()
        }

        single_bed.setOnClickListener {
            it.hideKeyboard()
        }

        handicap.setOnClickListener {
            it.hideKeyboard()
        }

        balcony.setOnClickListener {
            it.hideKeyboard()
        }

        submit.setOnClickListener {
            submit()
        }
    }

    private fun setUpToolbar() {
        toolbar.inflateMenu(R.menu.menu_save)
        toolbar.menu.findItem(R.id.save)
                .setOnMenuItemClickListener { _: MenuItem? ->
                    submit()
                    true
                }
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp)
        toolbar.setNavigationOnClickListener {
            activity?.onBackPressed()
        }
    }

    private fun submit() {
        viewModel.submit(
                name.text.toString(),
                persons.text.toString(),
                double_bed.isChecked,
                single_bed.isChecked,
                handicap.isChecked,
                balcony.isChecked
        )
    }

    companion object {
        private const val ROOM_BUNDLE_KEY = "RoomDetailFragment.ROOM_BUNDLE_KEY"

        fun newInstance(room: Room?): RoomDetailFragment = RoomDetailFragment().apply {
            arguments = Bundle(1).apply {
                putParcelable(
                        ROOM_BUNDLE_KEY,
                        room
                )
            }
        }
    }

}
