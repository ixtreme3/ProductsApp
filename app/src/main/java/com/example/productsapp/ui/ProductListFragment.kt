package com.example.productsapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.viewModels
import com.example.productsapp.MainActivity
import com.example.productsapp.R
import com.example.productsapp.data.model.Product
import com.example.productsapp.databinding.FragmentProductListBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProductListFragment : Fragment(), ProductListAdapter.Callback {
    private val viewModel: ProductListViewModel by viewModels()
    private lateinit var binding: FragmentProductListBinding
    private val productListAdapter = ProductListAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProductListBinding.inflate(inflater, container, false)

        manageSpinnerVisibility(View.VISIBLE)

        binding.recyclerView.adapter = productListAdapter
        productListAdapter.callback = this

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.productLiveData.observe(viewLifecycleOwner) { newData ->
            productListAdapter.data = newData
            manageSpinnerVisibility(View.GONE)
        }
    }

    private fun manageSpinnerVisibility(visibility: Int) {
        val activity = activity as MainActivity
        val progressBar = activity.findViewById<ProgressBar>(R.id.spinner)
        progressBar.visibility = visibility
    }

    override fun onItemClick(item: Product) {
        TODO("Not yet implemented")
    }

    override fun getDiscountPrice(price: Double, discountPercentage: Double): Int {
        return viewModel.calcDiscountPrice(price, discountPercentage)
    }
}