package com.example.productsapp.ui.productlist

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import com.example.productsapp.R
import com.example.productsapp.data.model.Product
import com.example.productsapp.databinding.FragmentProductListBinding
import com.example.productsapp.ui.adapters.ProductListAdapter
import com.google.android.material.button.MaterialButton
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

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

        binding.recyclerView.adapter = productListAdapter
        productListAdapter.callback = this


        requireActivity().findViewById<MaterialButton>(R.id.retryButton).setOnClickListener {
            productListAdapter.refresh()
        }

        viewLifecycleOwner.lifecycleScope.launch {
            productListAdapter.loadStateFlow.collectLatest { loadStates ->
                requireActivity().apply {
                    findViewById<ProgressBar>(R.id.progressBar).isVisible =
                        loadStates.refresh is LoadState.Loading
                    findViewById<MaterialButton>(R.id.retryButton).isVisible =
                        loadStates.refresh is LoadState.Error
                    findViewById<TextView>(R.id.errorMessage).isVisible =
                        loadStates.refresh is LoadState.Error
                }
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.productFlow.collectLatest { pagingData ->
                productListAdapter.submitData(pagingData)
            }
        }
    }

    override fun onItemClick(item: Product) {
        val action = ProductListFragmentDirections.actionProductListFragmentToProductFragment(item.id)
        findNavController().navigate(action)
    }

    override fun getDiscountPrice(price: Double, discountPercentage: Double): Int {
        return viewModel.calcDiscountPrice(price, discountPercentage)
    }
}