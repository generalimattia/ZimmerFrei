package com.generals.zimmerfrei.room.detail.view

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
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
    ): View? {
        return inflater.inflate(
            R.layout.fragment_room_detail,
            container,
            false
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setUpToolbar()
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

    private fun submit() {}

    companion object {
        fun newInstance(): RoomDetailFragment =
            RoomDetailFragment()
    }

}
