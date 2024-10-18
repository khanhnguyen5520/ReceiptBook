package com.example.receiptbook.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.receiptbook.R
import com.example.receiptbook.database.InvoiceDatabase
import com.example.receiptbook.model.Invoice

class InvoiceAdapter(private val onItemClicked: (Invoice) -> Unit) :
    ListAdapter<Invoice, InvoiceAdapter.InvoiceViewHolder>(InvoiceDiffCallback) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): InvoiceViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_invoice, parent, false)
        return InvoiceViewHolder(view)
    }

    override fun onBindViewHolder(holder: InvoiceViewHolder, position: Int) {
        val invoice = getItem(position)
        if (invoice != null) {
            holder.bind(invoice)
        }
    }

    inner class InvoiceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val title = itemView.findViewById<TextView>(R.id.tvInvoiceTitle)
        private val money = itemView.findViewById<TextView>(R.id.tvInvoiceMoney)
        private val img = itemView.findViewById<ImageView>(R.id.imgInvoiceCategory)
        private val dao = InvoiceDatabase.getDatabase(itemView.context).invoiceDao()

        fun bind(invoice: Invoice) {
            money.text =
                String.format(invoice.money.toString()).reversed().chunked(3).joinToString(".")
                    .reversed()
            val category = dao.getCategoryById(invoice.category)
            title.text = if (invoice.title.isNullOrEmpty()) category.title else invoice.title
            img.setImageResource(category.avatar)
            itemView.setOnClickListener {
                onItemClicked(invoice)
            }
        }
    }

    companion object InvoiceDiffCallback : DiffUtil.ItemCallback<Invoice>() {
        override fun areItemsTheSame(oldItem: Invoice, newItem: Invoice): Boolean {
            Log.d("InvoiceAdapter", "areItemsTheSame: ${oldItem.id} ${newItem.id}")
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Invoice, newItem: Invoice): Boolean {
            return oldItem == newItem
        }
    }
}