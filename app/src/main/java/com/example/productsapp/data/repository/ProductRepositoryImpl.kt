package com.example.productsapp.data.repository

import com.example.productsapp.data.model.Product
import com.example.productsapp.data.network.ProductService
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(private val productService: ProductService) :
    ProductRepository {
    override suspend fun getProducts(skip: Int, limit: Int): List<Product> {
        return productService.getProductList(skip, limit).products
    }
}