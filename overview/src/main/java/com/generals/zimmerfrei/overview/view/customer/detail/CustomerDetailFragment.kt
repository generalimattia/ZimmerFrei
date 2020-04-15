package com.generals.zimmerfrei.overview.view.customer.detail

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.generals.zimmerfrei.overview.R
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_customer_detail.*
import javax.inject.Inject

class CustomerDetailFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: CustomerDetailViewModel

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(
                this,
                viewModelFactory
        ).get(CustomerDetailViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_customer_detail, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpToolbar()
    }

    private fun setUpToolbar() {
        toolbar.setTitle(R.string.create_customer)
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp)
        toolbar.setNavigationOnClickListener {
            activity?.onBackPressed()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(url: String?) =
                CustomerDetailFragment().apply {
                    arguments = Bundle().apply {
                        putString(CUSTOMER_URL_ARG_KEY, url)
                    }
                }
    }
}

const val CUSTOMER_URL_ARG_KEY = "CustomerDetailFragment.CUSTOMER_URL_ARG_KEY"
