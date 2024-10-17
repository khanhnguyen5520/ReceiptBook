package com.example.receiptbook.database

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.receiptbook.R
import com.example.receiptbook.model.Category
import com.example.receiptbook.model.Invoice
import java.io.ByteArrayOutputStream

@Database(entities = [Invoice::class, Category::class], version = 1, exportSchema = false)
@TypeConverters(InvoiceConverter::class)
abstract class InvoiceDatabase : RoomDatabase() {

    abstract fun invoiceDao(): InvoiceDao

    companion object {
        @Volatile
        private var INSTANCE: InvoiceDatabase? = null

        fun getDatabase(context: Context): InvoiceDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    InvoiceDatabase::class.java,
                    "invoice_database"
                )
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .addCallback(AppDatabaseCallback(context))
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private class AppDatabaseCallback(
            private val context: Context
        ) : Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                Thread {
                    INSTANCE?.let { database ->
                        populateDatabase(context, database.invoiceDao())
                    }
                }.start()
            }

            private fun populateDatabase(context: Context, invoiceDao: InvoiceDao) {
                val categories = listOf(
                    CategoryData("Ăn uống", R.drawable.ic_food),
                    CategoryData("Mua sắm", R.drawable.ic_cart),
                    CategoryData("Du lịch", R.drawable.ic_plane),
                    CategoryData("Xăng", R.drawable.ic_pump),
                    CategoryData("Sửa chữa", R.drawable.ic_tools)
                )
                categories.forEach { categoryData ->
                    val avatarByteArray = drawableToByteArray(context, categoryData.drawableRes)
                    val category = Category(
                        avatar = "1111",
                        title = categoryData.name,
                        isShow = true,
                        isEdit = false,
                        isIncome = false
                    )
                    val insertResult = invoiceDao.insertCategory(category)
                    Log.d(
                        "InvoiceDatabase",
                        "Category Insert Result: $insertResult for ${categoryData.name}"
                    )
                }
            }

            // Helper function to convert drawable resource to byte array
            private fun drawableToByteArray(context: Context, drawableResId: Int): ByteArray {
                val drawable = ContextCompat.getDrawable(context, drawableResId) as BitmapDrawable
                val stream = ByteArrayOutputStream()
                drawable.bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
                return stream.toByteArray()
            }

            // Data class for category data
            private data class CategoryData(val name: String, val drawableRes: Int)
        }
    }
}
