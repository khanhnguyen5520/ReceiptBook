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
import com.example.receiptbook.databinding.ActivityRegularInvoiceBinding

class RegularInvoiceActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegularInvoiceBinding
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegularInvoiceBinding.inflate(layoutInflater)
        setupStatusBar()
        setContentView(binding.root)
        binding.tbRegularInvoice.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.btnAddRegularInvoice.setOnClickListener {
            val intent = Intent(this, AddRegularInvoiceActivity::class.java)
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

        WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars = isNightMode
    }
}