package com.example.receiptbook.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.receiptbook.R
import com.example.receiptbook.model.Category

class CategoryAdapter(private val onItemClicked: (Category) -> Unit)
    :ListAdapter<Category, CategoryAdapter.CategoryViewHolder>(CategoryDiffCallback) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CategoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_category, parent, false)
        return CategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = getItem(position)
        if (category!= null){
            holder.bind(category)
            holder.itemView.setOnClickListener {
                onItemClicked(category)
            }
        }
    }

    inner class CategoryViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        private val title = itemView.findViewById<TextView>(R.id.categoryTitle)
        private val avatars = itemView.findViewById<TextView>(R.id.categoryAvatar)
        fun bind(category: Category){
            title.text = category.title
            avatars.text = category.title.first().toString()
        }
    }
    companion object CategoryDiffCallback : DiffUtil.ItemCallback<Category>() {
        override fun areItemsTheSame(oldItem: Category, newItem: Category): Boolean {
            Log.d("NoteAdapter", "areItemsTheSame: ${oldItem.id} ${newItem.id}")
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Category, newItem: Category): Boolean {
            return oldItem == newItem

        }
    }
}