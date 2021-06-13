package com.example.astroviews.util

import android.content.Context
import android.os.Environment
import java.io.File

class FileCache(context: Context) {

    private var cachePath: File? = null

    init {
        cachePath = if (Environment.getExternalStorageState()
                .equals(Environment.MEDIA_MOUNTED)
        ) {
            context.getExternalFilesDir("ImageCache")
        } else {
            context.cacheDir
        }

        if (cachePath?.exists() == false) {
            cachePath?.mkdir()
        }
    }

    fun get(url: String): File = File(cachePath, url.md5())

    fun clear() {
        cachePath?.listFiles()?.forEach { file ->
            file.delete()
        }
    }
}