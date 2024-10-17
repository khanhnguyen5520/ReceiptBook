package com.example.receiptbook.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.receiptbook.R
import com.example.receiptbook.model.Category

class CategoryAdapter(private val onItemClicked: (Category) -> Unit) :
    ListAdapter<Category, CategoryAdapter.CategoryViewHolder>(CategoryDiffCallback) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CategoryViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_category, parent, false)
        return CategoryViewHolder(view)
    }

    private var selectedPosition = RecyclerView.NO_POSITION

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = getItem(position)
        if (category != null) {
            val isSelected = position == selectedPosition
            holder.bind(category, isSelected)
        }
    }

    inner class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val title = itemView.findViewById<TextView>(R.id.categoryTitle)
        private val avatars = itemView.findViewById<ImageView>(R.id.categoryAvatar)
        private val background = itemView.findViewById<CardView>(R.id.itemBackground)

        fun bind(category: Category, isSelected: Boolean) {
            title.text = category.title
            avatars.setImageResource(category.avatar)
            val color = if (isSelected) ContextCompat.getColor(
                itemView.context,
                R.color.highlight
            ) else ContextCompat.getColor(
                itemView.context,
                R.color.cardView
            )
            background.setCardBackgroundColor(color)

            itemView.setOnClickListener {
                val previousPosition = selectedPosition
                selectedPosition = adapterPosition

                notifyItemChanged(previousPosition)
                notifyItemChanged(selectedPosition)

                onItemClicked(category)
            }
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