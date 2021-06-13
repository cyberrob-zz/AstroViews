package com.example.astroviews.model

import android.os.Parcelable
import com.example.astroviews.IMAGE_DATE_FORMAT
import com.example.astroviews.database.entities.AstroImageRecord
import com.example.astroviews.util.toFormattedString
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.text.SimpleDateFormat
import java.util.*

@Parcelize
data class AstroImage(
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
    val url: String,
    @Expose(serialize = false, deserialize = false)
    val created: Long

) : Parcelable

fun AstroImage.asNasaImageRecord(): AstroImageRecord {
    return AstroImageRecord(
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

fun AstroImageRecord.asNasaImage(): AstroImage {
    return AstroImage(
        apodSite = this.apodSite,
        copyright = this.copyright,
        date = this.date.toFormattedString(
            dateFormat = SimpleDateFormat(
                IMAGE_DATE_FORMAT,
                Locale.getDefault()
            )
        ),
        description = this.description,
        hdurl = this.hdurl,
        mediaType = this.mediaType,
        title = this.title,
        url = this.url,
        created = this.created
    )
}
