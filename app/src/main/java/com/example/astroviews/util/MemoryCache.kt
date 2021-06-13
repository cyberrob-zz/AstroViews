package com.example.astroviews.util

import android.graphics.Bitmap
import android.util.LruCache
import com.orhanobut.logger.Logger

class MemoryCache {

    private val cacheSize by lazy {
        val maxMemory = (Runtime.getRuntime().maxMemory() / 1024).toInt()
        Logger.d("maxMemory: $maxMemory")
        maxMemory / 8
    }

    private var cache: LruCache<String, Bitmap> = object :
        LruCache<String, Bitmap>(cacheSize) {
        override fun sizeOf(key: String, value: Bitmap): Int {
            return value.byteCount / 1024
        }
    }

    fun put(key: String, value: Bitmap) {
        if (get(key) == null) {
            cache.put(key, value)
            Logger.d("New key $key put.")
        }
    }

    fun get(key: String): Bitmap? =
        if (cache.get(key) == null) null
        else cache.get(key)


    fun clear() {
        cache.evictAll()
    }
}