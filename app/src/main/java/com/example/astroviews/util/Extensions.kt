package com.example.astroviews.util

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.TypedValue
import com.orhanobut.logger.Logger
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import java.io.*
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

fun String.toBitmapAsync(requiredWidth: Int, requiredHeight: Int): Deferred<Bitmap?> {
    return GlobalScope.async(context = Dispatchers.IO) {
        try {
            val imageUrl = URL(this@toBitmapAsync)
            (imageUrl.openConnection() as HttpURLConnection).also {
                it.connectTimeout = 30000
                it.readTimeout = 30000
                it.instanceFollowRedirects = true
            }.run {

                val outputStream = ByteArrayOutputStream()
                val bytesCopied = inputStream.copyTo(outputStream)
                Logger.d("$bytesCopied byte of image copied to output stream. ")

                val options = BitmapFactory.Options().apply { inJustDecodeBounds = true }
                BitmapFactory.decodeStream(
                    ByteArrayInputStream(outputStream.toByteArray()),
                    null,
                    options
                )

                val inSampleSize =
                    calculateInSampleSize(
                        options,
                        requiredWidth.toPx().toInt(),
                        requiredHeight.toPx().toInt()
                    )
                Logger.d("sample size: $inSampleSize")

                // Decode bitmap with inSampleSize
                options.inSampleSize = inSampleSize
                options.inJustDecodeBounds = false

                val bitmap = BitmapFactory.decodeStream(
                    ByteArrayInputStream(outputStream.toByteArray()),
                    null,
                    options
                )
                Logger.d("sampled bitmap size: ${bitmap?.byteCount}")
                bitmap
            }
        } catch (e: MalformedURLException) {
            Logger.e(e.toString())
            null
        } catch (e: IOException) {
            Logger.e(e.toString())
            null
        }
    }
}

fun File.toBitmapAsync(): Deferred<Bitmap?> {
    return GlobalScope.async(context = Dispatchers.IO) async@{
        try {
            BitmapFactory.decodeStream(FileInputStream(this@toBitmapAsync))
        } catch (e: FileNotFoundException) {
            Logger.d("File not found: $e")
            null
        }
    }
}

fun Bitmap.toFileAsync(file: File): Deferred<Boolean> {
    return GlobalScope.async(context = Dispatchers.IO) {
        try {
            val outputStream = FileOutputStream(file)
            this@toFileAsync.compress(Bitmap.CompressFormat.JPEG, 75, outputStream)
            outputStream.close()
            true
        } catch (e: Exception) {
            Logger.d("Failed to save bitmap as file: $e")
            false
        }
    }
}

fun String.toDate(dateFormat: SimpleDateFormat) = try {
    dateFormat.parse(this)
} catch (e: ParseException) {
    null
}

fun Date.toFormattedString(dateFormat: SimpleDateFormat): String = dateFormat.format(this)

fun Int.toPx() = TypedValue.applyDimension(
    TypedValue.COMPLEX_UNIT_DIP,
    this.toFloat(),
    Resources.getSystem().displayMetrics
)
