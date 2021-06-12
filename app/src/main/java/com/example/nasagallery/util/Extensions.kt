package com.example.nasagallery.util

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.orhanobut.logger.Logger
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import java.io.*
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL

fun String.toBitmapAsync(): Deferred<Bitmap?> {
    return GlobalScope.async(context = Dispatchers.IO) {
        try {
            val imageUrl = URL(this@toBitmapAsync)
            (imageUrl.openConnection() as HttpURLConnection).also {
                it.connectTimeout = 30000
                it.readTimeout = 30000
                it.instanceFollowRedirects = true
            }.run {
//                val inputStream = BufferedInputStream(inputStream)
                val options = BitmapFactory.Options().also { it.inJustDecodeBounds = true }
//                BitmapFactory.decodeStream(inputStream, null, options)
//                val bitmapWidth = options.outWidth
//                val bitmapHeight = options.outHeight
//                Logger.d("decoded width: $bitmapWidth, height: $bitmapHeight")

                options.inJustDecodeBounds = false
//                val screenWidth: Int = Resources.getSystem().displayMetrics.widthPixels
//                val scaleFactor = min(bitmapWidth / screenWidth, bitmapHeight / screenWidth)
//                Logger.d("scaleFactor: $scaleFactor")
//                if (scaleFactor > 1) {
//                    options.inSampleSize = scaleFactor
//                }
                val bitmap = BitmapFactory.decodeStream(
                    inputStream,
                    null,
                    options
                )
                Logger.d("bitmap: ${bitmap?.byteCount}")
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