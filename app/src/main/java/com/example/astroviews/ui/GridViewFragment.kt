package com.example.astroviews.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.astroviews.KEY_NASA_IMAGE
import com.example.astroviews.R
import com.example.astroviews.databinding.FragmentGridViewListBinding
import com.example.astroviews.model.asNasaImage
import com.example.astroviews.util.ImageLoader
import com.example.astroviews.viewmodel.GridViewModel
import kotlinx.coroutines.flow.collectLatest
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class GridViewFragment : Fragment() {

    private var _binding: FragmentGridViewListBinding? = null

    private val binding get() = _binding!!

    private val imageLoader: ImageLoader by inject()

    private val pagingAdapter by lazy { NasaImageRecyclerViewAdapter(imageLoader) }

    private val gridViewModel: GridViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGridViewListBinding.inflate(inflater, container, false)

        pagingAdapter.itemClickListener = { clickedNasaImage ->
            findNavController().navigate(
                R.id.action_gridViewFragment_to_DetailFragment, bundleOf(
                    KEY_NASA_IMAGE to clickedNasaImage.asNasaImage()
                )
            )
        }

        with(binding.nasaImageList) {
            layoutManager = GridLayoutManager(context, 4)
            adapter = pagingAdapter
        }

        lifecycleScope.launchWhenCreated {
            gridViewModel.nasaImageFlow.collectLatest { data -> pagingAdapter.submitData(data) }
        }

        return binding.root
    }

}