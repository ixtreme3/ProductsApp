package com.example.productsapp.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.productsapp.databinding.LoadStateBinding

class ProductLoadStateAdapter(private val retry: () -> Unit) :
    LoadStateAdapter<ProductLoadStateAdapter.ProductLoadStateViewHolder>() {

    override fun onBindViewHolder(
        holder: ProductLoadStateAdapter.ProductLoadStateViewHolder,
        loadState: LoadState
    ) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): ProductLoadStateAdapter.ProductLoadStateViewHolder {
        val binding = LoadStateBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductLoadStateViewHolder(binding, retry)
    }

    inner class ProductLoadStateViewHolder(
        private val binding: LoadStateBinding,
        private val retry: () -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(loadState: LoadState) {
            binding.apply {
                errorMessage.isVisible = loadState is LoadState.Error
                reloadButton.isVisible = loadState is LoadState.Error
                progressBar.isVisible = loadState is LoadState.Loading

                reloadButton.setOnClickListener {
                    retry()
                }
            }
        }
    }
}