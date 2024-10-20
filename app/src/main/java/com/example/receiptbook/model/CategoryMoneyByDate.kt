package com.example.receiptbook.model

data class CategoryMoneyByDate(
    val date: String,
    val categoryTitle: String, // This should match the SQL alias
    val totalMoney: Float
)
