package com.example.nasagallery.database

import androidx.room.TypeConverter
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class Converters {

    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    @TypeConverter
    fun dateStringToDate(value: String): Date? =
        try {
            dateFormat.parse(value)
        } catch (e: ParseException) {
            null
        }

    @TypeConverter
    fun dateToDateString(date: Date): String = dateFormat.format(date)

}