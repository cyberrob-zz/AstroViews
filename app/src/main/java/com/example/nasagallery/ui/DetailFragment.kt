package com.example.nasagallery.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.nasagallery.KEY_NASA_IMAGE
import com.example.nasagallery.R
import com.example.nasagallery.databinding.FragmentDetailBinding
import com.example.nasagallery.model.NasaImage
import com.example.nasagallery.util.ImageLoader
import org.koin.android.ext.android.inject

class DetailFragment : Fragment() {

    private val imageLoader: ImageLoader by inject()

    private var _binding: FragmentDetailBinding? = null

    private var _nasaImage: NasaImage? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        readFromArgument()
    }

    private fun readFromArgument() {
        arguments?.getParcelable<NasaImage>(KEY_NASA_IMAGE)?.let {
            _nasaImage = it
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonSecond.setOnClickListener {
            findNavController().navigate(R.id.action_SecondFragment_to_gridViewFragment)
        }

        _nasaImage?.run safeImage@{
            with(binding) {
                imageLoader.loadIntoTarget(this@safeImage.hdurl, nasaHdImageView)
                this.dateTextView.text = this@safeImage.date
                this.titleTextView.text = this@safeImage.title
                this.copyrightTextView.text = this@safeImage.copyright
                this.descriptionTextView.text = this@safeImage.description
            }
        } ?: kotlin.run {
            with(binding) {
                nasaHdImageView.setImageResource(R.drawable.ic_baseline_image_24)
                this.dateTextView.text = "n/a"
                this.titleTextView.text = "n/a"
                this.copyrightTextView.text = "n/a"
                this.descriptionTextView.text = "n/a"
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}