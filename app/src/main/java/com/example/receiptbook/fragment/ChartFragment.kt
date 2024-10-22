package com.example.receiptbook.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.receiptbook.R
import com.example.receiptbook.adapter.ChartPagerAdapter
import com.example.receiptbook.adapter.MonthAdapter
import com.example.receiptbook.database.InvoiceDao
import com.example.receiptbook.database.InvoiceDatabase
import com.example.receiptbook.databinding.FragmentChartBinding
import com.example.receiptbook.model.CategoryWithTotal
import java.util.Calendar
import java.util.Locale
import kotlin.math.absoluteValue

class ChartFragment : Fragment() {
    private lateinit var monthAdapter: MonthAdapter
    private lateinit var invoiceDao: InvoiceDao
    private lateinit var categoryAdapter: ArrayAdapter<CategoryWithTotal>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentChartBinding.inflate(inflater, container, false)
        invoiceDao = InvoiceDatabase.getDatabase(requireContext()).invoiceDao()

        categoryAdapter = object :
            ArrayAdapter<CategoryWithTotal>(requireContext(), R.layout.item_category_total) {
            private var totalMoney: Double = 0.0

            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view =
                    convertView ?: inflater.inflate(R.layout.item_category_total, parent, false)
                val category = getItem(position)

                category?.let {
                    view.findViewById<CardView>(R.id.itemBackground).setCardBackgroundColor(it.color)
                    view.findViewById<ImageView>(R.id.categoryAvatar)
                        .setImageResource(it.avatar)
                    view.findViewById<TextView>(R.id.categoryTitle).text = it.title
                    val categoryMoney = it.totalMoney.absoluteValue
                    view.findViewById<TextView>(R.id.categoryMoney).text = String.format(categoryMoney.toString())

                    val percent = if (totalMoney > 0) (categoryMoney / totalMoney) * 100 else 0.0
                    view.findViewById<TextView>(R.id.categoryPercent).text = String.format(Locale.getDefault(), "%.1f%%", percent)

                    val progressCategory = view.findViewById<SeekBar>(R.id.pbCategory)
                    progressCategory.progress = percent.toInt()
                    progressCategory.setOnTouchListener { _, _ -> true }
                }
                return view
            }

            override fun addAll(collection: MutableCollection<out CategoryWithTotal>) {
                super.addAll(collection)
                totalMoney = collection.sumOf { it.totalMoney.absoluteValue }.toDouble()
            }
        }

        binding.listCategory.adapter = categoryAdapter
        invoiceDao.getCategoryWithInvoiceTotals().observe(viewLifecycleOwner) { categories ->
            if (categories.isNullOrEmpty()) {
                binding.lbNoData.visibility = View.VISIBLE
                binding.lbData.visibility = View.GONE
            } else {
                binding.lbNoData.visibility = View.GONE
                binding.lbData.visibility = View.VISIBLE
                categoryAdapter.clear()
                categoryAdapter.addAll(categories)
            }
        }

        val pagerAdapter = ChartPagerAdapter(this)
        binding.chartViewPager.adapter = pagerAdapter
        binding.dotIndicator.attachTo(binding.chartViewPager)

        monthAdapter = MonthAdapter(getMonthsList())
        binding.recyclerViewMonths.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = monthAdapter
        }
        return binding.root
    }

    private fun getMonthsList(): List<String> {
        val monthList = mutableListOf<String>()
        val calendar = Calendar.getInstance()
        val currentMonth = calendar.get(Calendar.MONTH)
        val year = calendar.get(Calendar.YEAR)

        for (i in 0..currentMonth) {
            val month = String.format(Locale.getDefault(),"thg %01d %04d", i+1 , year)
            monthList.add(month)
        }
        return monthList
    }
}