package com.example.receiptbook.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.receiptbook.R
import com.example.receiptbook.adapter.CategoryAdapter
import com.example.receiptbook.adapter.ChartPagerAdapter
import com.example.receiptbook.database.InvoiceDao
import com.example.receiptbook.database.InvoiceDatabase
import com.example.receiptbook.databinding.FragmentChartBinding
import com.example.receiptbook.model.CategoryWithTotal
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate

class ChartFragment : Fragment() {
    private lateinit var invoiceDao: InvoiceDao
    private lateinit var categoryAdapter: ArrayAdapter<CategoryWithTotal>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentChartBinding.inflate(inflater, container, false)
        invoiceDao = InvoiceDatabase.getDatabase(requireContext()).invoiceDao()

        // Set up category list adapter
        categoryAdapter = object :
            ArrayAdapter<CategoryWithTotal>(requireContext(), R.layout.item_category_total) {
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view =
                    convertView ?: inflater.inflate(R.layout.item_category_total, parent, false)
                val category = getItem(position)

                view.findViewById<ImageView>(R.id.categoryAvatar)
                    .setImageResource(category?.avatar ?: 0)
                view.findViewById<TextView>(R.id.categoryTitle).text = category?.title ?: ""
                view.findViewById<TextView>(R.id.categoryMoney).text =
                    category?.totalMoney?.toString() ?: "0"

                return view
            }
        }

        binding.listCategory.adapter = categoryAdapter
        invoiceDao.getCategoryWithInvoiceTotals().observe(viewLifecycleOwner) { categories ->
            categoryAdapter.clear()
            if (categories != null) {
                categoryAdapter.addAll(categories)
            }
        }
        binding.dotIndicator.dotsColor = R.color.dotUnSelectedColor
        binding.dotIndicator.selectedDotColor = R.color.dotSelectedColor

        val pagerAdapter = ChartPagerAdapter(this)
        binding.chartViewPager.adapter = pagerAdapter
        binding.dotIndicator.setViewPager2(binding.chartViewPager)

        return binding.root
    }
}