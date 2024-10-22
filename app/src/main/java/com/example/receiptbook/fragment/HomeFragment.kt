package com.example.receiptbook.fragment

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.receiptbook.R
import com.example.receiptbook.activity.InvoiceDetailActivity
import com.example.receiptbook.adapter.InvoiceAdapter
import com.example.receiptbook.database.InvoiceDao
import com.example.receiptbook.database.InvoiceDatabase
import com.example.receiptbook.databinding.FragmentHomeBinding
import com.example.receiptbook.model.Invoice
import kotlin.math.absoluteValue

class HomeFragment : Fragment() {
    private lateinit var invoiceAdapter: InvoiceAdapter
    private lateinit var invoiceDao: InvoiceDao
    private lateinit var binding: FragmentHomeBinding
    private var totalExpense = 0f
    private var totalIncome = 0f
    private var totalBalance = 0f

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        binding.rcvInvoiceHome.layoutManager = LinearLayoutManager(requireContext())
        invoiceAdapter = InvoiceAdapter { invoice -> onClickInvoice(invoice) }
        binding.rcvInvoiceHome.adapter = invoiceAdapter
        invoiceDao = InvoiceDatabase.getDatabase(requireContext()).invoiceDao()
        loadInvoices()
        return binding.root
    }

    private fun loadInvoices() {
        invoiceDao.getLiveInvoices().observe(viewLifecycleOwner) { invoices ->
            invoiceAdapter.submitList(invoices)
            calculateTotalBalance(invoices)
        }
    }

    private fun calculateTotalBalance(invoices: List<Invoice>){
        invoices.forEach {
            if (!it.isIncome){
                totalExpense += it.money
            }else{
                totalIncome += it.money
            }
        }
        totalBalance = totalIncome + totalExpense
        binding.tvTotalIncome.text = String.format(totalIncome.toString())
        binding.tvTotalExpense.text = String.format(totalExpense.absoluteValue.toString())
        binding.tvLeftOver.text = String.format(totalBalance.toString())
    }

    private fun onClickInvoice(invoice: Invoice) {
        val intent = Intent(requireContext(), InvoiceDetailActivity::class.java).apply {
            putExtra("invoiceId", invoice.id)
        }
        val options = ActivityOptions.makeCustomAnimation(
            requireContext(),
            R.anim.slide_in_bottom,
            R.anim.fade_out
        )
        startActivity(intent, options.toBundle())
    }
}