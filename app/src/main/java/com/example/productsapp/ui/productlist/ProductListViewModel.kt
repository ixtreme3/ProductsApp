package com.example.productsapp.ui.productlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.productsapp.data.model.Product
import com.example.productsapp.data.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import kotlin.math.roundToInt

@HiltViewModel
class ProductListViewModel @Inject constructor(productRepository: ProductRepository) :
    ViewModel() {

    val productFlow: Flow<PagingData<Product>>

    init {
        productFlow = productRepository.getProducts().cachedIn(viewModelScope)
    }

    fun calcDiscountPrice(price: Double, discountPercentage: Double): Int {
        val priceWithDiscount = price * (1 - discountPercentage / 100)
        return priceWithDiscount.roundToInt()
    }
}
