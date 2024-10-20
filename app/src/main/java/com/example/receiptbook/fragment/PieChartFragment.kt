package com.example.receiptbook.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.receiptbook.R
import com.example.receiptbook.database.InvoiceDao
import com.example.receiptbook.database.InvoiceDatabase
import com.example.receiptbook.databinding.FragmentPieChartBinding
import com.example.receiptbook.model.CategoryWithTotal
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate
import java.util.Locale
import kotlin.math.absoluteValue

class PieChartFragment : Fragment() {
    private lateinit var invoiceDao: InvoiceDao
    private lateinit var categoryAdapter: ArrayAdapter<CategoryWithTotal>
    private lateinit var binding: FragmentPieChartBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPieChartBinding.inflate(inflater, container, false)
        invoiceDao = InvoiceDatabase.getDatabase(requireContext()).invoiceDao()
        invoiceDao.getCategoryWithInvoiceTotals().observe(viewLifecycleOwner) { categories ->
            val topCategories = categories.sortedByDescending { it.totalMoney.absoluteValue }.take(5)
            val totalMoney = categories.sumOf { it.totalMoney.absoluteValue }.toFloat()

            categoryAdapter = object : ArrayAdapter<CategoryWithTotal>(requireContext(), R.layout.item_category_total, topCategories) {
                override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                    val view = convertView ?: inflater.inflate(R.layout.item_category_total, parent, false)
                    val category = getItem(position)

                    val percentage = if (totalMoney > 0) {
                        (category?.totalMoney?.absoluteValue?.toFloat() ?: 0f) / totalMoney * 100
                    } else 0f

                    view.findViewById<ImageView>(R.id.categoryAvatar).setImageResource(category?.avatar ?: 0)
                    view.findViewById<TextView>(R.id.categoryTitle).text = category?.title ?: ""
                    view.findViewById<TextView>(R.id.categoryMoney).text = String.format(Locale.getDefault(), "%.2f%%", percentage)
                    return view
                }
            }
            binding.listTopCategories.adapter = categoryAdapter

            setupPieChart(categories)
        }

        return binding.root
    }

    private fun setupPieChart(categories: List<CategoryWithTotal>) {
        val pieEntries = ArrayList<PieEntry>()
        var totalMoney = 0f

        for (category in categories) {
            totalMoney += category.totalMoney
        }

        for (category in categories) {
            val percentage = (category.totalMoney / totalMoney) * 100
            pieEntries.add(PieEntry(percentage, category.title))
        }

        val pieDataSet = PieDataSet(pieEntries, "Categories")
        pieDataSet.colors = ColorTemplate.MATERIAL_COLORS.toList()
        pieDataSet.valueTextSize = 12f

        val pieData = PieData(pieDataSet)
        binding.pieChart.data = pieData
        binding.pieChart.description.isEnabled = false
        binding.pieChart.legend.isEnabled = false
        binding.pieChart.centerText = String.format(totalMoney.absoluteValue.toString())
        binding.pieChart.setCenterTextColor(ContextCompat.getColor(requireContext(), R.color.textColor))
        binding.pieChart.setHoleColor(ContextCompat.getColor(requireContext(), R.color.backgroundColor))
        binding.pieChart.animateY(1000)
        binding.pieChart.invalidate()
    }

    companion object {
        fun newInstance(): PieChartFragment {
            return PieChartFragment()
        }
    }
}