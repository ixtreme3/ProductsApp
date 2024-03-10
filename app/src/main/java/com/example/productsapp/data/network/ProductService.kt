package com.example.productsapp.data.network

import com.example.productsapp.data.model.Product
import com.example.productsapp.data.model.ProductResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ProductService {
    @GET("products")
    suspend fun getProductList(
        @Query("skip") skip: Int,
        @Query("limit") limit: Int,
    ) : ProductResponse

    @GET("products/{id}")
    suspend fun getProductById(
        @Path("id") id: Int
    ): Product
}