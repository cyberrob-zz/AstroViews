package com.example.nasagallery.database

import androidx.room.TypeConverter
import com.example.nasagallery.IMAGE_DATE_FORMAT
import com.example.nasagallery.util.toDate
import com.example.nasagallery.util.toFormattedString
import java.text.SimpleDateFormat
import java.util.*

class Converters {

    private val dateFormat = SimpleDateFormat(IMAGE_DATE_FORMAT, Locale.getDefault())

    @TypeConverter
    fun dateStringToDate(value: String): Date? = value.toDate(dateFormat)

    @TypeConverter
    fun dateToDateString(date: Date): String = date.toFormattedString(dateFormat)

}