package com.example.receiptbook.activity

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.receiptbook.R
import com.example.receiptbook.databinding.ActivityMainBinding
import com.example.receiptbook.fragment.HomeFragment
import com.example.receiptbook.fragment.ChartFragment
import com.example.receiptbook.fragment.ReportFragment
import com.example.receiptbook.fragment.SettingFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        replaceFragment(HomeFragment())
        binding.bottomNavigationView.background = null
        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> replaceFragment(HomeFragment())
                R.id.shorts -> replaceFragment(ChartFragment())
                R.id.subscriptions -> replaceFragment(ReportFragment())
                R.id.library -> replaceFragment(SettingFragment())
            }
            true
        }

        binding.fab.setOnClickListener {
            val intent = Intent(this, InvoiceActivity::class.java)
            val options = ActivityOptions.makeCustomAnimation(
                this,
                R.anim.slide_in_bottom,
                R.anim.fade_out
            )
            startActivity(intent, options.toBundle())
        }
    }
    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager: FragmentManager = supportFragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout, fragment)
        fragmentTransaction.commit()
    }
}