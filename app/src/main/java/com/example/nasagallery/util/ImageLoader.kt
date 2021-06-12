package com.example.nasagallery.util

import android.app.ActivityManager
import android.content.Context
import android.util.LruCache
import android.widget.ImageView
import com.example.nasagallery.R
import com.orhanobut.logger.Logger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ImageLoader(context: Context) {

    private val memoryClass =
        (context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager).memoryClass

    private val memoryCache: MemoryCache by lazy { MemoryCache(memoryClass) }

    private val fileCache: FileCache by lazy { FileCache(context) }

    private val imageViews = LruCache<ImageView, String>(memoryClass / 8 * 1024 * 1024)


    fun loadIntoTarget(url: String, target: ImageView) {
        if (imageViews.get(target) == null) {
            imageViews.put(target, url)
        }

        if (memoryCache.get(url) != null) {
            target.setImageBitmap(memoryCache.get(url))
        } else {
            target.setImageResource(R.drawable.ic_baseline_image_24)
            queueImageRequest(url, target)
        }
    }

    private fun queueImageRequest(url: String, target: ImageView) {
        GlobalScope.launch {

            withContext(context = Dispatchers.IO) {

                if (hasCached(target)) {
                    return@withContext
                }

                val targetFileCache = fileCache.get(url)
                val fileCachedBitmap = targetFileCache.toBitmapAsync().await()

                val foundInFileCache = (fileCachedBitmap != null)

                val bitmap =
                    // Check fileCache first
                    if (fileCachedBitmap != null) {
                        Logger.d("Found bitmap from fileCache")
                        fileCache.get(url).toBitmapAsync().await()
                    }
                    // Get from remote
                    else {
                        url.toBitmapAsync().await()
                    }

                if (bitmap != null) {
                    if (foundInFileCache.not()) {
                        val saved = bitmap.toFileAsync(targetFileCache).await()
                        if (saved) {
                            Logger.d("Save bitmap into fileCache..")
                        }
                    }

                    Logger.d("Save bitmap into memoryCache..")
                    memoryCache.put(url, bitmap)

                    withContext(context = Dispatchers.Main) {
                        target.setImageBitmap(bitmap)
                    }
                }
            }
        }
    }

    private fun hasCached(target: ImageView): Boolean {
        return imageViews.get(target) == null
    }

}