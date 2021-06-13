package com.example.astroviews.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "astro_image_record")
data class AstroImageRecord(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "_id")
    var _id: Int = 0,

    @ColumnInfo(name = "title")
    var title: String,

    @ColumnInfo(name = "url")
    var url: String,

    @ColumnInfo(name = "hdurl")
    var hdurl: String,

    @ColumnInfo(name = "media_type")
    var mediaType: String,

    @ColumnInfo(name = "description")
    var description: String,

    @ColumnInfo(name = "date")
    var date: Date,

    @ColumnInfo(name = "copyright")
    var copyright: String,

    @ColumnInfo(name = "apod_site")
    var apodSite: String,

    @ColumnInfo(name = "created")
    var created: Long = System.currentTimeMillis()

) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as AstroImageRecord

        if (_id != other._id) return false
        if (created != other.created) return false

        return true
    }

    override fun hashCode(): Int {
        var result = _id.hashCode()
        result = 31 * result + created.hashCode()
        return result
    }
}