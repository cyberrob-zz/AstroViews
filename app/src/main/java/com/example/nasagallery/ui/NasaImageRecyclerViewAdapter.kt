package com.example.nasagallery.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.nasagallery.database.entities.NasaImageRecord
import com.example.nasagallery.databinding.ItemNasaImageBinding
import com.example.nasagallery.util.ImageLoader

typealias NasaImageClickListener = (NasaImageRecord) -> Unit

class NasaImageRecyclerViewAdapter(
    private val imageLoader: ImageLoader
) : PagingDataAdapter<NasaImageRecord, NasaImageRecyclerViewAdapter.ViewHolder>(NasaImageRecordDiff()) {

    var itemClickListener: NasaImageClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        ItemNasaImageBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let { imageRecord ->
            holder.bind(image = imageRecord, imageLoader = imageLoader)
        }
    }

    inner class ViewHolder(private val binding: ItemNasaImageBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(image: NasaImageRecord, imageLoader: ImageLoader) {
            binding.itemNumber.text = image.title
            imageLoader.loadIntoTarget(image.url, binding.contentImage)
            binding.itemContainer.setOnClickListener {
                itemClickListener?.invoke(image)
            }
        }
    }

    class NasaImageRecordDiff : DiffUtil.ItemCallback<NasaImageRecord>() {
        override fun areItemsTheSame(oldItem: NasaImageRecord, newItem: NasaImageRecord): Boolean {
            return oldItem._id == newItem._id
        }

        override fun areContentsTheSame(
            oldItem: NasaImageRecord,
            newItem: NasaImageRecord
        ): Boolean {
            return oldItem == newItem
        }

    }
}