package com.example.nasagallery.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.nasagallery.TARGET_URL
import com.example.nasagallery.database.dao.TheDao
import com.example.nasagallery.model.NasaImage
import com.example.nasagallery.model.asNasaImageRecord
import com.google.gson.Gson
import com.orhanobut.logger.Logger
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.IOException
import java.net.MalformedURLException
import java.net.URL


class FirstViewModel(
    private val theDao: TheDao
) : BaseViewModel() {

    sealed class State(var message: String) {
        object Retrieving : State(message = "Working on retrieve data..")
        object Parsing : State(message = "Working on parse data..")
        object Saving : State(message = "Working on save data..")
        object Succeed : State(message = "Succeed to retrieve data..")
        object Failed : State(message = "Failed to retrieve data")
        object NoData : State(message = "No json string available.")
    }

    private var _state = MutableLiveData<State>()
    val state: LiveData<State>
        get() {
            return _state
        }

    fun retrieveData() {
        viewModelScope.launch(Dispatchers.IO) {

            _state.postValue(State.Retrieving)
            val jsonString = retrieveJsonAsync().await()

            if (jsonString.isEmpty()) {
                Logger.d("No json string available.")
                _state.postValue(State.NoData)
                return@launch
            } else {
                Logger.d("json string retrieved...")
            }

            _state.postValue(State.Parsing)
            val nasaImages = jsonDeserializeAsync(jsonString).await()

            if (theDao.getAllImages().isEmpty()) {
                _state.postValue(State.Saving)
                nasaImages.forEach { image ->
                    val id = theDao.insertImage(image.asNasaImageRecord())
                    Logger.d("..image $id inserted")
                }
            }

            Logger.d("Got ${theDao.getAllImages().size} records...")
            _state.postValue(State.Succeed.also {
                it.message = "Got ${theDao.getAllImages().size} records..."
            })
        }
    }


    private fun retrieveJsonAsync(): Deferred<String> {

        return viewModelScope.async(context = Dispatchers.IO) {

            var reader: BufferedReader? = null
            val buffer = StringBuffer()
            try {
                val url = URL(TARGET_URL)

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
                buffer.toString()
            } catch (exception: IOException) {
                Logger.e(exception.toString())
                _state.postValue(State.Failed.also { it.message = exception.toString() })
                ""
            } catch (exception: MalformedURLException) {
                Logger.e(exception.toString())
                _state.postValue(State.Failed.also { it.message = exception.toString() })
                ""
            } finally {
                reader?.close()

                return@async buffer.toString()
            }
        }
    }

    private fun jsonDeserializeAsync(jsonString: String?): Deferred<Array<NasaImage>> {
        return viewModelScope.async {

            jsonString ?: return@async emptyArray()

            return@async Gson().fromJson(jsonString, Array<NasaImage>::class.java)
        }
    }

    // TODO: 2021/6/10 coroutine

    // TODO: 2021/6/10 post livedata back to UI and go to GridViewFragment
}