package com.generals.zimmerfrei.overview.view.customer

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.generals.zimmerfrei.common.extension.toColor
import com.generals.zimmerfrei.common.utils.buildDrawable
import com.generals.zimmerfrei.common.utils.randomColor
import com.generals.zimmerfrei.model.Customer
import com.generals.zimmerfrei.overview.R
import com.simplecityapps.recyclerview_fastscroll.views.FastScrollRecyclerView

class CustomerListAdapter(
        private val customers: List<Customer>,
        private val onCustomerClick: (Customer) -> Unit
) : RecyclerView.Adapter<CustomerViewHolder>(), FastScrollRecyclerView.SectionedAdapter {

    init {
        setHasStableIds(true)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomerViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_customer_name, parent, false)
        return CustomerViewHolder(view)
    }

    override fun onBindViewHolder(holder: CustomerViewHolder, position: Int) {
        holder.bind(customers[position], onCustomerClick)
    }

    override fun getSectionName(position: Int): String =
            customers[position].firstName.first().toString().toUpperCase()

    override fun getItemCount(): Int = customers.size
    override fun getItemId(position: Int): Long = customers[position].hashCode().toLong()
}

class CustomerViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val firstLetter: TextView = view.findViewById(R.id.first_letter)
    private val firstLetterBackground: ImageView = view.findViewById(R.id.first_letter_background)
    private val fullName: TextView = view.findViewById(R.id.name)

    fun bind(
            customer: Customer,
            onClick: (Customer) -> Unit
    ) {
        firstLetterBackground.background = buildDrawable(itemView.context, randomColor().toColor(), R.drawable.shape_circular_solid)
        firstLetter.text = customer.firstName.first().toString()
        fullName.text = "${customer.firstName} ${customer.lastName}"
        itemView.setOnClickListener { onClick(customer) }
    }
}