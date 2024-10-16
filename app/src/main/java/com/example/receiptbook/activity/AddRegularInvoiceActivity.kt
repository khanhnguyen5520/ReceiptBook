package com.example.receiptbook.activity

import android.app.Dialog
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
import com.example.receiptbook.R
import com.example.receiptbook.databinding.ActivityAddRegularInvoiceBinding

class AddRegularInvoiceActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddRegularInvoiceBinding

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupStatusBar()
        binding = ActivityAddRegularInvoiceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnPaymentFrequency.setOnClickListener {
            showPaymentFrequencyDialog()
        }

        binding.btnPaymentRepeat.setOnClickListener {
            showPaymentRepeatDialog()
        }

        binding.btnTypePayment.setOnClickListener {
            showPaymentTypeDialog()
        }

        binding.btnPaymentMoney.setOnClickListener {
            showPaymentMoneyDialog()
        }

        binding.tvAddInvoice.setOnClickListener {
            addRegularInvoice()
        }
        binding.tvCancelAddInvoice.setOnClickListener {
            finish()
        }
    }

    private fun addRegularInvoice() {
        when {
            binding.edtPaymentName.text.isNullOrEmpty() -> binding.edtPaymentName.error = "Vui lòng nhập tên thanh toán"
            binding.btnPaymentMoney.text.isNullOrEmpty() -> binding.btnPaymentMoney.error = "Vui lòng nhập số tiền"
            binding.btnCategoryPayment.text.isNullOrEmpty() -> binding.btnCategoryPayment.error = "Vui lòng chọn loại thanh toán"
            else -> finish()
        }
    }

    private fun showPaymentMoneyDialog() {
        val moneyDialog = Dialog(this)
        moneyDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        moneyDialog.setContentView(R.layout.number_keyboard)
        moneyDialog.show()
        moneyDialog.window!!.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        moneyDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        moneyDialog.window!!.setGravity(Gravity.BOTTOM)

        val previewMoney = moneyDialog.findViewById<TextView>(R.id.previewMoney)
        moneyDialog.findViewById<Button>(R.id.btnClear).setOnClickListener {
            previewMoney.text = null
        }

        moneyDialog.findViewById<TextView>(R.id.btnEnterMoney).setOnClickListener {
            binding.btnPaymentMoney.text = previewMoney.text.toString()
            moneyDialog.dismiss()
        }

        moneyDialog.findViewById<Button>(R.id.closeDialog).setOnClickListener {
            moneyDialog.dismiss()
        }

        listOf(R.id.button0, R.id.button1, R.id.button2, R.id.button3, R.id.button4,
            R.id.button5, R.id.button6, R.id.button7, R.id.button8, R.id.button9).forEach { id ->
            moneyDialog.findViewById<Button>(id).setOnClickListener {
                var currentText = previewMoney.text.toString()
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
                updatePreviewMoney(currentText, previewMoney)
            }
        }

        moneyDialog.findViewById<Button>(R.id.buttonDel).setOnClickListener {
            val currentText = previewMoney.text.toString()
            if (currentText.isNotEmpty()) {
                updatePreviewMoney(currentText.dropLast(1), previewMoney)
            }
        }

        moneyDialog.findViewById<Button>(R.id.buttonComma).setOnClickListener {
            val currentText = previewMoney.text.toString()
            if (!currentText.contains(",")) {
                val newText = if (currentText.isEmpty()) "0," else "$currentText,"
                updatePreviewMoney(newText, previewMoney)
            }
        }
    }

    private fun updatePreviewMoney(text: String, previewMoney: TextView) {
        val parts = text.split(",")
        val integerPart =
            parts[0].replace(".", "").reversed().chunked(3).joinToString(".").reversed()
        val decimalPart = if (parts.size > 1) ",${parts[1]}" else ""
        previewMoney.text = "$integerPart$decimalPart"
    }

    private fun setupBottomDialog(layoutId: Int, gravity: Int = Gravity.BOTTOM): Dialog {
        val dialog = Dialog(this).apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setContentView(layoutId)
            window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            window!!.setGravity(gravity)
            show()
        }
        return dialog
    }

    private fun showOptionDialog(layoutId: Int, buttonIds: List<Int>, onSelected: (String) -> Unit) {
        val dialog = setupBottomDialog(layoutId)
        buttonIds.forEach { id ->
            dialog.findViewById<TextView>(id).setOnClickListener {
                onSelected((it as TextView).text.toString())
                dialog.dismiss()
            }
        }
    }

    private fun showPaymentTypeDialog() {
        showOptionDialog(R.layout.bottom_menu_payment_type, listOf(R.id.income, R.id.expense)) { selected ->
            binding.btnTypePayment.text = selected
        }
    }

    private fun showPaymentRepeatDialog() {
        showOptionDialog(R.layout.bottom_menu_payment_repeat, listOf(R.id.noLimit, R.id.repeat3, R.id.repeat6, R.id.repeat12, R.id.repeat18, R.id.repeat24)) { selected ->
            binding.btnPaymentRepeat.text = selected
        }
    }

    private fun showPaymentFrequencyDialog() {
        showOptionDialog(R.layout.bottom_menu_payment_frequency, listOf(R.id.daily, R.id.weekly, R.id.week2, R.id.monthly, R.id.month2, R.id.month3, R.id.month4, R.id.month6, R.id.yearly)) { selected ->
            binding.btnPaymentFrequency.text = selected
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