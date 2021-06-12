package com.example.nasagallery.util

import android.graphics.Bitmap
import android.util.LruCache
import com.orhanobut.logger.Logger
import java.lang.ref.WeakReference

class MemoryCache(memoryClass: Int) {
    private val cache: LruCache<String, WeakReference<Bitmap>>
            by lazy { LruCache<String, WeakReference<Bitmap>>((memoryClass / 8) * 1024 * 1024) }


    fun put(key: String, value: Bitmap) {
        if (get(key) == null) {
            cache.put(key, WeakReference<Bitmap>(value))
            Logger.d("New key $key put.")
        }
    }

    fun get(key: String): Bitmap? =
        if (cache.get(key) == null) null
        else cache.get(key).get()


    fun clear() {
        cache.evictAll()
    }
}