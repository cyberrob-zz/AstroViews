package com.example.astroviews.database

import androidx.room.TypeConverter
import com.example.astroviews.IMAGE_DATE_FORMAT
import com.example.astroviews.util.toDate
import com.example.astroviews.util.toFormattedString
import java.text.SimpleDateFormat
import java.util.*

class Converters {

    private val dateFormat = SimpleDateFormat(IMAGE_DATE_FORMAT, Locale.getDefault())

    @TypeConverter
    fun dateStringToDate(value: String): Date? = value.toDate(dateFormat)

    @TypeConverter
    fun dateToDateString(date: Date): String = date.toFormattedString(dateFormat)

}