package com.example.receiptbook.activity

import android.app.ActivityOptions
import android.app.Dialog
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowInsetsControllerCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.example.receiptbook.R
import com.example.receiptbook.adapter.CategoryAdapter
import com.example.receiptbook.database.InvoiceDao
import com.example.receiptbook.database.InvoiceDatabase
import com.example.receiptbook.databinding.ActivityInvoiceBinding
import com.example.receiptbook.model.Category
import com.example.receiptbook.model.Invoice

class InvoiceActivity : AppCompatActivity() {
    private lateinit var binding: ActivityInvoiceBinding
    private lateinit var dao: InvoiceDao
    private lateinit var categoryAdapter: CategoryAdapter
    private var categoryList = listOf<Category>()

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInvoiceBinding.inflate(layoutInflater)
        setupStatusBar()
        setContentView(binding.root)

        dao = InvoiceDatabase.getDatabase(applicationContext).invoiceDao()
        initRcvCategories()

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

    private fun initRcvCategories() {
        categoryList = dao.getAllCategories()
        categoryAdapter = CategoryAdapter(onItemClicked = ::bottomSheetInvoice)
        categoryAdapter.submitList(categoryList)
        binding.rcvCategory.adapter = categoryAdapter
        binding.rcvCategory.layoutManager = GridLayoutManager(this, 4)
    }

    private fun bottomSheetInvoice(category: Category) {
        val invoiceDialog = Dialog(this)
        invoiceDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        invoiceDialog.setContentView(R.layout.bottomsheet_invoice)
        invoiceDialog.show()
        invoiceDialog.window!!.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            (resources.displayMetrics.heightPixels * 0.5).toInt()
        )
        invoiceDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        invoiceDialog.window!!.setGravity(Gravity.BOTTOM)

        val tvMoney = invoiceDialog.findViewById<TextView>(R.id.tvMoney)

        listOf(R.id.button0, R.id.button1, R.id.button2, R.id.button3, R.id.button4,
            R.id.button5, R.id.button6, R.id.button7, R.id.button8, R.id.button9).forEach { id ->
            invoiceDialog.findViewById<Button>(id).setOnClickListener {
                var currentText = tvMoney.text.toString()
                val parts = currentText.split(",")
                val integerPart = parts[0].replace(".", "")
                val hasComma = parts.size > 1
                val newDigit = (it as Button).text.toString()

                if (integerPart.length >= 10 && !hasComma) {
                    currentText = currentText.dropLast(1) + newDigit
                } else if (hasComma && parts[1].length >= 2) {
                    currentText = currentText.dropLast(1) + newDigit
                } else {
                    currentText += newDigit
                }
                updatePreviewMoney(currentText, tvMoney)
            }
        }

        invoiceDialog.findViewById<Button>(R.id.buttonDel).setOnClickListener {
            val currentText = tvMoney.text.toString()
            if (currentText.isNotEmpty()) {
                updatePreviewMoney(currentText.dropLast(1), tvMoney)
            }
        }

        invoiceDialog.findViewById<Button>(R.id.buttonComma).setOnClickListener {
            val currentText = tvMoney.text.toString()
            if (!currentText.contains(",")) {
                val newText = if (currentText.isEmpty()) "0," else "$currentText,"
                updatePreviewMoney(newText, tvMoney)
            }
        }

        invoiceDialog.findViewById<Button>(R.id.btnAddInvoice).setOnClickListener {
            var money = (tvMoney.text.toString().replace(".", "").split(",")[0]).toLong()
            if (!category.isIncome) {
                money = -money
            }
            val invoice = Invoice(money = money, isIncome = false, date = System.currentTimeMillis(), category = category.id)
            dao.insertInvoice(invoice)
            finish()
        }
    }

    private fun updatePreviewMoney(text: String, previewMoney: TextView) {
        val parts = text.split(",")
        val integerPart =
            parts[0].replace(".", "").reversed().chunked(3).joinToString(".").reversed()
        val decimalPart = if (parts.size > 1) ",${parts[1]}" else ""
        previewMoney.text = String.format("%s%s", integerPart, decimalPart)
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