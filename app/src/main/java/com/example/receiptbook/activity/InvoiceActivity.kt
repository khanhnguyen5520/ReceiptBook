package com.example.receiptbook.activity

import android.app.ActivityOptions
import android.content.Intent
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowInsetsControllerCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.example.receiptbook.R
import com.example.receiptbook.adapter.CategoryAdapter
import com.example.receiptbook.adapter.InvoiceAdapter
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
        Log.e("kkk", "initRcvCategories: $categoryList", )
        categoryAdapter = CategoryAdapter(onItemClicked = ::OnItemClicked)
        categoryAdapter.submitList(categoryList)
        binding.rcvCategory.adapter = categoryAdapter
        binding.rcvCategory.layoutManager = GridLayoutManager(this, 4)
    }

    private fun OnItemClicked(category: Category) {
        TODO("Not yet implemented")
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