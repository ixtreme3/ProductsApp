package com.example.productsapp.data.repository

import com.example.productsapp.data.model.Product

interface ProductRepository {
    suspend fun getProducts(skip: Int, limit: Int) : List<Product>
}