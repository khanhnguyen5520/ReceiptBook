package com.example.receiptbook.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "invoice")
data class Invoice(
    @PrimaryKey(autoGenerate= true) val id: Long = 0,
    val name: String,
    val money: Int,
    val date: String,
    val type: String,
    val category: String,
    val isIncome: Boolean
)
