package com.example.receiptbook.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.receiptbook.fragment.LineChartFragment
import com.example.receiptbook.fragment.PieChartFragment
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.PieData

class ChartPagerAdapter(
    fragment: Fragment
) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> PieChartFragment.newInstance()
            1 -> LineChartFragment.newInstance()
            else -> throw IllegalStateException("Unexpected position: $position")
        }
    }
}
