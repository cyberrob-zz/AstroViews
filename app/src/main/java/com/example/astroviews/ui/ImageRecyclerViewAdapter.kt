package com.example.astroviews.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.astroviews.CACHE_KEY_SEPARATOR
import com.example.astroviews.database.entities.AstroImageRecord
import com.example.astroviews.databinding.ItemNasaImageBinding
import com.example.astroviews.util.ImageLoader

typealias NasaImageClickListener = (AstroImageRecord) -> Unit

class NasaImageRecyclerViewAdapter(
    private val imageLoader: ImageLoader
) : PagingDataAdapter<AstroImageRecord, NasaImageRecyclerViewAdapter.ViewHolder>(NasaImageRecordDiff()) {

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

        fun bind(image: AstroImageRecord, imageLoader: ImageLoader) {
            binding.itemNumber.text = image.title
            imageLoader.loadIntoTarget(
                urlWithIdentifier = "${image.url}$CACHE_KEY_SEPARATOR${image.created}",
                target = binding.contentImage
            )
            binding.itemContainer.setOnClickListener {
                itemClickListener?.invoke(image)
            }
        }
    }

    class NasaImageRecordDiff : DiffUtil.ItemCallback<AstroImageRecord>() {
        override fun areItemsTheSame(
            oldItem: AstroImageRecord,
            newItem: AstroImageRecord
        ): Boolean {
            return oldItem._id == newItem._id
        }

        override fun areContentsTheSame(
            oldItem: AstroImageRecord,
            newItem: AstroImageRecord
        ): Boolean {
            return oldItem == newItem
        }

    }
}