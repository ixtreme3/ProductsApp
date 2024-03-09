package com.example.productsapp.data.network

import com.example.productsapp.data.model.ProductResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ProductService {
    @GET("products")
    suspend fun getProductList(
        @Query("skip") skip: Int = 0,
        @Query("limit") limit: Int = 20,
    ) : ProductResponse
}