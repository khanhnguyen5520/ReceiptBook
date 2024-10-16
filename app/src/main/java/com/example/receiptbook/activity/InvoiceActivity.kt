package com.example.receiptbook.activity

import android.app.ActivityOptions
import android.content.Intent
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowInsetsControllerCompat
import com.example.receiptbook.R
import com.example.receiptbook.databinding.ActivityInvoiceBinding

class InvoiceActivity : AppCompatActivity() {
    private lateinit var binding: ActivityInvoiceBinding

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInvoiceBinding.inflate(layoutInflater)
        setupStatusBar()
        setContentView(binding.root)
        binding.tvCancel.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        binding.tvRegularInvoice.setOnClickListener {
            val intent = Intent(this, RegularInvoiceActivity::class.java)
            val options = ActivityOptions.makeCustomAnimation(
                this,
                R.anim.slide_in_bottom,
                R.anim.fade_out
            )
            startActivity(intent, options.toBundle())
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun setupStatusBar() {
        window.statusBarColor = getColor(R.color.headerColor)

        val isNightMode = resources.configuration.uiMode and
                Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_NO

        WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars =
            isNightMode
    }
}