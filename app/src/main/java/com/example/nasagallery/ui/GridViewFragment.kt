package com.example.nasagallery.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.nasagallery.R
import com.example.nasagallery.databinding.FragmentGridViewListBinding
import com.example.nasagallery.ui.placeholder.PlaceholderContent

/**
 * A fragment representing a list of Items.
 */
class GridViewFragment : Fragment() {

    private var _binding: FragmentGridViewListBinding? = null

    private val binding get() = _binding!!

    private var columnCount = 4

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGridViewListBinding.inflate(inflater, container, false)

        binding.buttonToSecond.setOnClickListener {
            findNavController().navigate(R.id.action_gridViewFragment_to_DetailFragment)
        }

        with(binding.nasaImageList) {
            layoutManager = when {
                columnCount <= 1 -> LinearLayoutManager(context)
                else -> GridLayoutManager(context, columnCount)
            }
            adapter = NasaImageRecyclerViewAdapter(PlaceholderContent.ITEMS)
        }
        return binding.root
    }

    companion object {

        // TODO: Customize parameter argument names
        const val ARG_COLUMN_COUNT = "column-count"

        // TODO: Customize parameter initialization
        @JvmStatic
        fun newInstance(columnCount: Int) =
            GridViewFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount)
                }
            }
    }
}