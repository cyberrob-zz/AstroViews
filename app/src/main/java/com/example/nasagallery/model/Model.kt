package com.example.nasagallery.model

import android.os.Parcelable
import com.example.nasagallery.database.entities.NasaImageRecord
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.text.SimpleDateFormat
import java.util.*

@Parcelize
data class NasaImage(
    @SerializedName("apod_site")
    val apodSite: String,
    @SerializedName("copyright")
    val copyright: String,
    @SerializedName("date")
    val date: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("hdurl")
    val hdurl: String,
    @SerializedName("media_type")
    val mediaType: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("url")
    val url: String
) : Parcelable

fun NasaImage.asNasaImageRecord(): NasaImageRecord {
    return NasaImageRecord(
        _id = this.hashCode(),
        title = this.title,
        url = this.url,
        hdurl = this.hdurl,
        mediaType = this.mediaType,
        description = this.description,
        date =
        SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(this.date) ?: Date(),
        copyright = this.copyright,
        apodSite = this.apodSite
    )
}
