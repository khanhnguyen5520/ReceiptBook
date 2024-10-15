package com.example.receiptbook.activity

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import android.view.Window
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.receiptbook.R
import com.example.receiptbook.databinding.ActivityAddRegularInvoiceBinding

class AddRegularInvoiceActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddRegularInvoiceBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAddRegularInvoiceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnPaymentFrequency.setOnClickListener {
            showBottomDialog()
        }
    }

    private fun showBottomDialog() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.bottom_menu_payment_frequency)
        dialog.show()
        dialog.window!!.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            (resources.displayMetrics.heightPixels * 0.6).toInt()
        )
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window!!.setGravity(Gravity.BOTTOM)

        val textViewIds = listOf(
            R.id.daily, R.id.weekly, R.id.week2, R.id.monthly,
            R.id.month2, R.id.month3, R.id.month4, R.id.month6, R.id.yearly
        )
        textViewIds.forEach { id ->
            dialog.findViewById<TextView>(id).setOnClickListener { textView ->
                val selectedText = (textView as TextView).text.toString()
                binding.btnPaymentFrequency.text = selectedText
                dialog.dismiss()
            }
        }
    }
}