package com.example.receiptbook.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "category")
data class Category(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val avatar: String,
    val title: String,
    val isShow: Boolean,
    val isEdit: Boolean,
    val isIncome: Boolean
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Category

        if (id != other.id) return false
        if (!avatar.contentEquals(other.avatar)) return false
        if (title != other.title) return false
        if (isShow != other.isShow) return false
        if (isEdit != other.isEdit) return false
        if (isIncome != other.isIncome) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + avatar.hashCode()
        result = 31 * result + title.hashCode()
        result = 31 * result + isShow.hashCode()
        result = 31 * result + isEdit.hashCode()
        result = 31 * result + isIncome.hashCode()
        return result
    }
}
