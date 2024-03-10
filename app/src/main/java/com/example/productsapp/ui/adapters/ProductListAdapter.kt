package com.example.productsapp.ui.adapters

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.productsapp.R
import com.example.productsapp.data.model.Product
import com.example.productsapp.databinding.ItemProductBinding

class ProductListAdapter : PagingDataAdapter<Product, ProductListAdapter.ProductViewHolder>(ProductDiffCallback()) {
    var callback: Callback? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val item = getItem(position) ?: return
        holder.bind(item)
    }

    inner class ProductViewHolder(private val binding: ItemProductBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Product) {
            binding.apply {
                root.setOnClickListener{ callback?.onItemClick(item) }
                cardTitle.text = item.title
                cardDescription.text = item.description
                cardInitialPrice.text =
                    root.context.getString(R.string.price_placeholder, item.price.toString())
                cardInitialPrice.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                cardDiscountPrice.text = root.context.getString(
                    R.string.price_placeholder,
                    callback?.getDiscountPrice(item.price, item.discountPercentage).toString()
                )

                Glide.with(cardImage.context)
                    .load(item.thumbnail)
                    .placeholder(R.drawable.no_image)
                    .into(cardImage)
            }
        }
    }

    interface Callback {
        fun onItemClick(item: Product)
        fun getDiscountPrice(price: Double, discountPercentage: Double): Int
    }
}

class ProductDiffCallback : DiffUtil.ItemCallback<Product>() {
    override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
        return oldItem == newItem
    }
}
