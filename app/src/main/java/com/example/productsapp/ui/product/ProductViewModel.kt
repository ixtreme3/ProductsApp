package com.example.productsapp.ui.product

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.productsapp.data.model.Product
import com.example.productsapp.data.repository.ProductRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

class ProductViewModel @AssistedInject constructor(
    private val productRepository: ProductRepository,
    @Assisted private val productId: Int
) : ViewModel() {

    private val _product = MutableLiveData<Product>()
    val product: LiveData<Product> get() = _product

    private val _errorState = MutableLiveData<String>()
    val errorState: LiveData<String> get() = _errorState

    init {
        viewModelScope.launch {
            try {
                _product.value = productRepository.getProductById(productId)
            } catch (e: Exception) {
                _errorState.postValue(e.message)
                e.printStackTrace()
            }
        }
    }

    fun calcDiscountPrice(price: Double, discountPercentage: Double): Int {
        val priceWithDiscount = price * (1 - discountPercentage / 100)
        return priceWithDiscount.roundToInt()
    }

    @AssistedFactory
    interface Factory {
        fun create(productId: Int): ProductViewModel
    }

    @Suppress("UNCHECKED_CAST")
    companion object {
        fun provideFactory(
            assistedFactory: Factory,
            productId: Int
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return assistedFactory.create(productId) as T
            }
        }
    }
}