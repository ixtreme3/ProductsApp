package com.example.productsapp.data.datasource

import android.net.http.HttpException
import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.productsapp.data.model.Product
import com.example.productsapp.data.network.ProductService
import java.io.IOException

class ProductPagingDataSource(private val productService: ProductService) :
    PagingSource<Int, Product>() {

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Product> {
        val position = params.key ?: 0
        val limit = params.loadSize
        val skip = position * limit

        return try {
            val response = productService.getProductList(skip, limit)
            val products = response.products
            LoadResult.Page(
                data = products,
                prevKey = if (position == 0) null else position - 1,
                nextKey = if (products.isEmpty()) null else position + 1
            )
        } catch (e: IOException) {
            LoadResult.Error(e)
        } catch (e: HttpException) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Product>): Int? {
        val anchorPosition = state.anchorPosition ?: return null
        val page = state.closestPageToPosition(anchorPosition) ?: return null
        return page.prevKey?.plus(1) ?: page.nextKey?.minus(1)
    }
}