package com.example.receiptbook.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "invoice")
data class Invoice(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String?=null,
    val money: Long,
    val date: Long,
    val category: Long,
    val note: String? = null,
    val isIncome: Boolean
)
