package com.example.productsapp.ui.product

import android.content.Intent
import android.graphics.Paint
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.productsapp.R
import com.example.productsapp.databinding.FragmentProductBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ProductFragment : Fragment() {
    private val args: ProductFragmentArgs by navArgs()

    @Inject
    lateinit var viewModelAssistedFactory: ProductViewModel.Factory
    private val viewModel: ProductViewModel by viewModels {
        ProductViewModel.provideFactory(viewModelAssistedFactory, args.productId)
    }

    private lateinit var binding: FragmentProductBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProductBinding.inflate(inflater, container, false)

        binding.root.visibility = View.INVISIBLE

        val progressBar = requireActivity().findViewById<ProgressBar>(R.id.progressBar)
        progressBar.visibility = View.VISIBLE

        viewModel.product.observe(viewLifecycleOwner) { product ->
            binding.apply {
                productBrand.text = product.brand

                productTitle.text = product.title

                productDiscountPrice.text = getString(
                    R.string.price_placeholder,
                    viewModel.calcDiscountPrice(product.price, product.discountPercentage)
                        .toString()
                )

                originalProductPrice.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                originalProductPrice.text = getString(
                    R.string.price_placeholder,
                    product.price.toString()
                )

                discountLabel.text = getString(
                    R.string.discount_label_placeholder,
                    product.discountPercentage.toString()
                )

                stockIndicator.setBackgroundResource(
                    if (product.stock >= 10) R.drawable.stock_green_dot_background
                    else R.drawable.stock_red_dot_background
                )

                productStockCount.text = getString(
                    R.string.stock_placeholder,
                    product.stock.toString()
                )

                productRating.text = product.rating.toString()

                productCategory.text = getString(
                    R.string.category_placeholder,
                    product.category
                )

                productDescription.text = product.description
                Glide.with(requireActivity())
                    .load(product.thumbnail)
                    .placeholder(R.drawable.no_image)
                    .into(productImage)

                binding.purchaseButton.setOnClickListener {
                    openBrowserWithText(product.title)
                }

                progressBar.visibility = View.INVISIBLE
                binding.root.visibility = View.VISIBLE
            }
        }

        return binding.root
    }

    private fun openBrowserWithText(text: String) {
        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse("https://www.google.com/search?q=${Uri.encode(text)}")
        }

        if (intent.resolveActivity(requireContext().packageManager) != null) {
            startActivity(intent)
        } else {
            Log.i(TAG, "activity not resolved")
        }
    }

    companion object {
        const val TAG = "ProductFragment"
    }
}