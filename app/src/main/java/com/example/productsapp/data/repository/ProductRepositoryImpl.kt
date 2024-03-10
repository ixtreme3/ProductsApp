package com.example.productsapp.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.productsapp.data.datasource.ProductPagingDataSource
import com.example.productsapp.data.model.Product
import com.example.productsapp.data.network.ProductService
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(private val productService: ProductService) :
    ProductRepository {
    override fun getProducts() : Flow<PagingData<Product>> {
        val config = PagingConfig(
            initialLoadSize = 20,
            pageSize = 20,
            prefetchDistance = 10,
            enablePlaceholders = false
        )
        return Pager(
            config = config,
            pagingSourceFactory = { ProductPagingDataSource(productService) }
        ).flow
    }

    override suspend fun getProductById(productId: Int): Product {
        return productService.getProductById(productId)
    }
}