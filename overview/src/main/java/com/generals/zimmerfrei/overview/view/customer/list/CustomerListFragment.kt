package com.generals.zimmerfrei.overview.view.customer.list

import android.content.Context
import android.os.Bundle
import android.transition.Slide
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.generals.zimmerfrei.model.Customer
import com.generals.zimmerfrei.navigator.Navigator
import com.generals.zimmerfrei.overview.R
import com.generals.zimmerfrei.overview.view.customer.CustomerListAdapter
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_customer_list.*
import javax.inject.Inject

class CustomerListFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var navigator: Navigator

    private lateinit var viewModel: CustomerListViewModel

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(
                this,
                viewModelFactory
        ).get(CustomerListViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_customer_list, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpToolbar()

        if (savedInstanceState == null) {
            viewModel.customers.observe(viewLifecycleOwner, Observer { values: List<Customer> ->
                customers.adapter = CustomerListAdapter(values, viewModel::onCustomerClick)
            })
        }

        add_fab.setOnClickListener {
            activity?.also {
                navigator.customerDetail(
                        enterTransition = Slide(Gravity.BOTTOM),
                        exitTransition = Slide(Gravity.BOTTOM)
                )
                        .start(
                                it,
                                R.id.fragment_container,
                                true
                        )
            }
        }

        viewModel.start()
    }

    private fun setUpToolbar() {
        toolbar.setTitle(R.string.select_customer)
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp)
        toolbar.setNavigationOnClickListener {
            activity?.onBackPressed()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = CustomerListFragment()
    }
}
