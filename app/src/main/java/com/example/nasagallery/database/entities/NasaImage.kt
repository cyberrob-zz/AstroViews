package com.example.nasagallery.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "nasa_image")
data class NasaImageRecord(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "_id")
    var _id: Int,

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
    var apodSite: String
)