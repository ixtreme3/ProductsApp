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
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.productsapp.R
import com.example.productsapp.data.model.Product
import com.example.productsapp.databinding.FragmentProductBinding
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.ShapeAppearanceModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ProductFragment : Fragment() {
    private lateinit var binding: FragmentProductBinding

    private val args: ProductFragmentArgs by navArgs()

    @Inject
    lateinit var viewModelAssistedFactory: ProductViewModel.Factory

    private val viewModel: ProductViewModel by viewModels {
        ProductViewModel.provideFactory(viewModelAssistedFactory, args.productId)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProductBinding.inflate(inflater, container, false)
        val progressBar = requireActivity().findViewById<ProgressBar>(R.id.progressBar)

        progressBar.visibility = View.VISIBLE
        binding.root.visibility = View.INVISIBLE

        setupObservers(progressBar)

        return binding.root
    }

    private fun setupObservers(progressBar: ProgressBar) {
        viewModel.errorState.observe(viewLifecycleOwner) { error ->
            displayErrorSnackbar(error)
            progressBar.visibility = View.GONE
            findNavController().navigateUp()
        }

        viewModel.product.observe(viewLifecycleOwner) { product ->
            progressBar.visibility = View.INVISIBLE
            binding.root.visibility = View.VISIBLE
            displayProductDetails(product)
        }
    }

    private fun displayErrorSnackbar(error: String) {
        Snackbar.make(binding.root, error, Snackbar.LENGTH_LONG).show()
    }

    private fun displayProductDetails(product: Product) {
        with(binding) {
            productBrand.text = product.brand
            productTitle.text = product.title
            productDiscountPrice.text = getString(
                R.string.price_placeholder,
                "${viewModel.calcDiscountPrice(product.price, product.discountPercentage)}"
            )
            originalProductPrice.apply {
                paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                text = getString(R.string.price_placeholder, product.price.toString())
            }
            discountLabel.text = getString(
                R.string.discount_label_placeholder,
                product.discountPercentage.toString()
            )
            stockIndicator.setBackgroundResource(if (product.stock >= 10) R.drawable.stock_green_dot_background else R.drawable.stock_red_dot_background)
            productStockCount.text = getString(R.string.stock_placeholder, product.stock.toString())
            productRating.text = product.rating.toString()
            productCategory.text = getString(R.string.category_placeholder, product.category)
            productDescription.text = product.description
            product.images.forEach { imageUrl ->
                createAndLoadImageView(imageUrl, viewFlipper)
            }
            purchaseButton.setOnClickListener { openBrowserWithText(product.title) }
        }
    }

    private fun createAndLoadImageView(imageUrl: String, parentLayout: ViewGroup) {
        if (imageUrl.contains("thumbnail")) return

        val imageView = ShapeableImageView(context)

        imageView.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )

        imageView.scaleType = ImageView.ScaleType.CENTER_CROP

        val cornerSize = requireContext().resources.getDimension(R.dimen.image_corner_size)
        val roundedCorner = CornerFamily.ROUNDED

        imageView.shapeAppearanceModel = ShapeAppearanceModel.builder()
            .setBottomLeftCorner(roundedCorner, cornerSize)
            .setBottomRightCorner(roundedCorner, cornerSize)
            .build()

        Glide.with(requireContext())
            .load(imageUrl)
            .apply(RequestOptions().centerCrop())
            .into(imageView)

        parentLayout.addView(imageView)
    }

    private fun openBrowserWithText(text: String) {
        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse("https://www.google.com/search?q=${Uri.encode(text)}")
        }

        if (intent.resolveActivity(requireContext().packageManager) != null) {
            startActivity(intent)
        } else {
            displayErrorSnackbar(getString(R.string.intent_resolve_error))
            Log.i(TAG, "Activity not resolved")
        }
    }

    companion object {
        const val TAG = "ProductFragment"
    }
}