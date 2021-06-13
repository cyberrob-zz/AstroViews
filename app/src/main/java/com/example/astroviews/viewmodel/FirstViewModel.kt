package com.example.astroviews.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.astroviews.TARGET_URL
import com.example.astroviews.database.dao.TheDao
import com.example.astroviews.model.AstroImage
import com.example.astroviews.model.asNasaImageRecord
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
        object Retrieving : State(message = "..Working on retrieve data..")
        object Parsing : State(message = "..Working on parse data..")
        object Saving : State(message = "..Working on save data..")
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
            if (theDao.getAllImages().isNotEmpty()) {
                Logger.d("Got ${theDao.getAllImages().size} records...")
                _state.postValue(State.Succeed.also {
                    it.message = "Got ${theDao.getAllImages().size} records..."
                })
                return@launch
            }

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
                var inserted = 0
                nasaImages.forEach { image ->
                    theDao.insertImage(image.asNasaImageRecord()).run {
                        if (this == -1L)
                            return@run

                        inserted++
                        if (inserted != 0 && inserted % 500 == 0) {
                            _state.postValue(State.Saving.also {
                                it.message = "..$inserted record inserted"
                            })
                        }
                    }
                }
            }

            Logger.d("Got ${theDao.getAllImages().size} records...")
            _state.postValue(State.Succeed.also {
                it.message = "Total ${theDao.getAllImages().size} records..."
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

    private fun jsonDeserializeAsync(jsonString: String?): Deferred<Array<AstroImage>> {
        return viewModelScope.async {

            jsonString ?: return@async emptyArray()

            return@async Gson().fromJson(jsonString, Array<AstroImage>::class.java)
        }
    }
}