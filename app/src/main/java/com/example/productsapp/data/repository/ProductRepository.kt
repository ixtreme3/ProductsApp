package com.example.productsapp.data.repository

import androidx.paging.PagingData
import com.example.productsapp.data.model.Product
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    fun getProducts() : Flow<PagingData<Product>>

    suspend fun getProductById(productId: Int) : Product
}