package com.example.receiptbook.fragment

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.receiptbook.database.InvoiceDao
import com.example.receiptbook.database.InvoiceDatabase
import com.example.receiptbook.databinding.FragmentLineChartBinding
import com.example.receiptbook.model.CategoryMoneyByDate
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.Random

class LineChartFragment : Fragment() {
    private lateinit var invoiceDao: InvoiceDao
    private lateinit var binding: FragmentLineChartBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLineChartBinding.inflate(inflater, container, false)

        // Initialize InvoiceDao
        invoiceDao = InvoiceDatabase.getDatabase(requireContext()).invoiceDao()

        // Load data for the selected month (e.g., October)
        val selectedMonth = "10" // October
        invoiceDao.getCategoryMoneyByDate(selectedMonth).observe(viewLifecycleOwner) { data ->
            setupLineChart(data)
        }

        return binding.root
    }

    private fun setupLineChart(data: List<CategoryMoneyByDate>) {
        val lineDataSets = mutableListOf<ILineDataSet>()
        val categoryMap = mutableMapOf<String, MutableList<Entry>>()

        // Organize the data by category and date
        data.forEach { record ->
            val dateInMillis = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(record.date)?.time ?: 0L
            val entry = Entry(dateInMillis.toFloat(), record.totalMoney)

            if (categoryMap.containsKey(record.categoryTitle)) {
                categoryMap[record.categoryTitle]?.add(entry)
            } else {
                categoryMap[record.categoryTitle] = mutableListOf(entry)
            }
        }

        // Create LineDataSets for each category
        categoryMap.forEach { (category, entries) ->
            val lineDataSet = LineDataSet(entries, category)
            lineDataSet.color = getRandomColor() // Set unique color for each category
            lineDataSet.valueTextColor = Color.BLACK
            lineDataSet.valueTextSize = 10f
            lineDataSets.add(lineDataSet)
        }

        // Set the data for the chart
        val lineData = LineData(lineDataSets)
        binding.lineChart.data = lineData
        binding.lineChart.xAxis.valueFormatter = DateValueFormatter() // Format x-axis as date
        binding.lineChart.xAxis.granularity = 1f // One day intervals
        binding.lineChart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        binding.lineChart.axisLeft.axisMinimum = 0f
        binding.lineChart.animateY(1000)
        binding.lineChart.invalidate() // Refresh the chart
    }

    private fun getRandomColor(): Int {
        val rnd = Random()
        return Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))
    }

    companion object {
        fun newInstance(): LineChartFragment {
            return LineChartFragment()
        }
    }
}

class DateValueFormatter : ValueFormatter() {
    private val dateFormat = SimpleDateFormat("dd/MM", Locale.getDefault())

    override fun getFormattedValue(value: Float): String {
        return dateFormat.format(Date(value.toLong()))
    }
}
