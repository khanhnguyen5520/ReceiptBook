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
                    CategoryData("Sửa chữa", R.drawable.ic_tools),
                    CategoryData("Sắc đẹp", R.drawable.ic_spa),
                    CategoryData("Thể thao", R.drawable.ic_swimming),
                    CategoryData("Quần áo", R.drawable.ic_clothes),
                    CategoryData("Rượu", R.drawable.ic_wine),
                    CategoryData("Thú cưng", R.drawable.ic_pet),
                    CategoryData("Nh ở", R.drawable.ic_paint),
                    CategoryData("Quà tặng", R.drawable.ic_gift_box),
                    CategoryData("Vé số", R.drawable.ic_billiard)
                )
                categories.forEach { categoryData ->
                    val category = Category(
                        avatar = categoryData.drawableRes,
                        title = categoryData.name,
                        isShow = true,
                        isEdit = false,
                        isIncome = false
                    )
                    invoiceDao.insertCategory(category)
                }
            }

            // Data class for category data
            private data class CategoryData(val name: String, val drawableRes: Int)
        }
    }
}
