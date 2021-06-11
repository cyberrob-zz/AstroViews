package com.example.nasagallery.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.nasagallery.TARGET_URL
import com.example.nasagallery.model.NasaImage
import com.google.gson.Gson
import com.orhanobut.logger.Logger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.IOException
import java.net.MalformedURLException
import java.net.URL


class FirstViewModel : BaseViewModel() {

    private var _error = MutableLiveData<Unit>()
    val error: LiveData<Unit>
        get() {
            return _error
        }

    private var jsonString: String? = null

    fun retrieveJson() {
        viewModelScope.launch(context = Dispatchers.IO) {

            var reader: BufferedReader? = null
            try {
                val url = URL(TARGET_URL)

                val buffer = StringBuffer()
                val content = url.openStream().bufferedReader().use {
                    reader = it
                    it.readText()
                }
                buffer.append(content)
//                reader = BufferedReader(InputStreamReader(url.openStream()))
//                var line: String? = null
//
//                while (reader.readLine().also { line = it } != null) {
//                    Logger.d("appending $line")
//                    buffer.append(line).appendLine()
//                }

                jsonString = buffer.toString()
            } catch (exception: IOException) {
                Logger.e(exception.toString())
            } catch (exception: MalformedURLException) {
                Logger.e(exception.toString())
            } finally {
                reader?.close()

                jsonDeserialize(jsonString)
            }
        }
    }

    // TODO: 2021/6/10 parse json file as gson object
    private fun jsonDeserialize(jsonString: String?) {
        jsonString ?: return

        val gson = Gson()
        val list = gson.fromJson(jsonString, Array<NasaImage>::class.java)
        Logger.d("Size: ${list.size}")
        list.takeLast(10).forEach { nasaImage ->
            Logger.d(nasaImage.title)
        }
    }

    // TODO: 2021/6/10 coroutine

    // TODO: 2021/6/10 post livedata back to UI and go to GridViewFragment
}