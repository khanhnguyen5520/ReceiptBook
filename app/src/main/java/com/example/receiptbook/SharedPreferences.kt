package com.example.receiptbook

import android.content.Context
import android.content.SharedPreferences

object SharedPreferences {
    private lateinit var sharedPreferences: SharedPreferences
    fun init(context: Context) {
        sharedPreferences = context.getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
    }
}