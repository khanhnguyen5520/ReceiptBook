package com.example.receiptbook.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.receiptbook.model.Category
import com.example.receiptbook.model.CategoryMoneyByDate
import com.example.receiptbook.model.CategoryWithTotal
import com.example.receiptbook.model.Invoice

@Dao
interface InvoiceDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertInvoice(invoice: Invoice): Long

    @Query("SELECT * FROM invoice WHERE id = :id")
    fun getInvoiceById(id: Long): Invoice?

    @Query("SELECT * FROM invoice")
    fun getAllInvoices(): List<Invoice>

    @Query("SELECT * FROM invoice")
    fun getLiveInvoices(): LiveData<List<Invoice>>

    @Query("DELETE FROM invoice WHERE id = :id")
    fun deleteInvoiceById(id: Long)

    @Delete
    fun deleteInvoice(invoice: Invoice)

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

    @Query("SELECT * FROM category WHERE id = :id")
    fun getCategoryById(id: Long): Category

    @Query("""
    SELECT category.id, category.avatar, category.title, SUM(invoice.money) AS totalMoney
    FROM category 
    JOIN invoice ON category.id = invoice.category
    GROUP BY category.id
""")
    fun getCategoryWithInvoiceTotals(): LiveData<List<CategoryWithTotal>>

    @Query("""
    SELECT invoice.date AS date, category.title AS categoryTitle, SUM(invoice.money) AS totalMoney
    FROM invoice
    JOIN category ON invoice.category = category.id
    WHERE strftime('%m', invoice.date) = :selectedMonth 
    GROUP BY invoice.date, category.id
    ORDER BY invoice.date
""")
    fun getCategoryMoneyByDate(selectedMonth: String): LiveData<List<CategoryMoneyByDate>>


}