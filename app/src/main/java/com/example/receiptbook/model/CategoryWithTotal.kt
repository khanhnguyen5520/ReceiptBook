package com.example.receiptbook.model

data class CategoryWithTotal(
    val id: Long,
    val avatar: Int,
    val title: String,
    val totalMoney: Long
)