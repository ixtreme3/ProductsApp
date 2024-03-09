package com.example.productsapp.ui

import android.annotation.SuppressLint
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.productsapp.R
import com.example.productsapp.data.model.Product
import com.example.productsapp.databinding.ItemProductBinding

class ProductListAdapter : RecyclerView.Adapter<ProductListAdapter.ProductViewHolder>() {
    var callback: Callback? = null

    var data = listOf<Product>()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(data[position])
    }

    inner class ProductViewHolder(private val binding: ItemProductBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Product) {
            binding.apply {
                cardTitle.text = item.title
                cardDescription.text = item.description
                cardInitialPrice.text =
                    root.context.getString(R.string.card_init_price, item.price.toInt())
                cardInitialPrice.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                cardDiscountPrice.text = root.context.getString(
                    R.string.card_price_with_discount,
                    callback?.getDiscountPrice(item.price, item.discountPercentage)
                )

                cardImage.setImageResource(R.drawable.no_image)
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