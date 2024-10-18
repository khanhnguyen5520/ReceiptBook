package com.example.receiptbook.activity

import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.example.receiptbook.R
import com.example.receiptbook.database.InvoiceDao
import com.example.receiptbook.database.InvoiceDatabase
import com.example.receiptbook.databinding.ActivityInvoiceDetailBinding
import com.example.receiptbook.model.Category
import com.example.receiptbook.model.Invoice
import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.math.absoluteValue

class InvoiceDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityInvoiceDetailBinding
    private lateinit var invoiceDao: InvoiceDao

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInvoiceDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupStatusBar()
        invoiceDao = InvoiceDatabase.getDatabase(this).invoiceDao()
        val invoiceId = intent.getLongExtra("invoiceId", -1)
        val invoice = invoiceDao.getInvoiceById(invoiceId)
        val category = invoiceDao.getCategoryById(invoice!!.category)

        binding.tvCancel.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.tvDeleteInvoice.setOnClickListener {
            invoiceDao.deleteInvoice(invoice)
            onBackPressedDispatcher.onBackPressed()
        }

        setView(invoice,category)
    }

    private fun setView(invoice: Invoice, category: Category) {
        binding.imgInvoiceCategory.setImageResource(category.avatar)
        binding.tvCategoryInvoice.text = category.title
        binding.tvType.text = if (invoice.isIncome) "Thu nhập" else "Chi tiêu"
        binding.tvMoney.text = String.format(invoice.money.absoluteValue.toString()).reversed().chunked(3).joinToString(".")
            .reversed()
        val format = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        binding.tvDate.text = format.format(invoice.date)
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