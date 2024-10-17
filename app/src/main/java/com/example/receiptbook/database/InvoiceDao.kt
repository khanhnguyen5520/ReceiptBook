package com.example.receiptbook.database

import androidx.room.*
import com.example.receiptbook.model.Category
import com.example.receiptbook.model.Invoice

@Dao
interface InvoiceDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertInvoice(invoice: Invoice): Long

    @Query("SELECT * FROM invoice WHERE id = :id")
    fun getInvoiceById(id: Long): Invoice?

    @Query("SELECT * FROM invoice")
    fun getAllInvoices(): List<Invoice>

    @Query("DELETE FROM invoice WHERE id = :id")
    fun deleteInvoiceById(id: Long)

    @Query("DELETE FROM invoice")
    fun deleteAllInvoices()

    @Update
    fun updateInvoice(invoice: Invoice)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertCategory(category: Category): Long

    @Update
    fun updateCategory(category: Category)

    @Query("SELECT * FROM category")
    fun getAllCategories(): List<Category>
}
