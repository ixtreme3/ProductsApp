package com.example.productsapp.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.productsapp.data.model.Product
import com.example.productsapp.data.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.roundToInt

@HiltViewModel
class ProductListViewModel @Inject constructor(productRepository: ProductRepository) :
    ViewModel() {
    private val _productLiveData = MutableLiveData<List<Product>>()
    val productLiveData: LiveData<List<Product>> get() = _productLiveData

    init {
        viewModelScope.launch {
            _productLiveData.postValue(productRepository.getProducts(0, 100))
        }
    }

    fun calcDiscountPrice(price: Double, discountPercentage: Double): Int {
        val priceWithDiscount = price * (1 - discountPercentage / 100)
        return priceWithDiscount.roundToInt()
    }
}
