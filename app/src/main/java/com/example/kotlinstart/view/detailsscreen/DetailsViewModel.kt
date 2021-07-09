package com.example.kotlinstart.view.detailsscreen

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kotlinstart.dto.WeatherDTO
import com.example.kotlinstart.model.AppState
import com.example.kotlinstart.repository.detailsrepository.RepositoryDetailsImpl
import com.example.kotlinstart.repository.loader.displayWeather
import com.google.gson.Gson
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException

internal class DetailsViewModel(
    private val liveDataForObservation: MutableLiveData<AppState> = MutableLiveData(),
    private val repositoryImpl: RepositoryDetailsImpl = RepositoryDetailsImpl()
) : ViewModel() {

    private lateinit var city: String
    private val callback =
        object : Callback {

            override fun onResponse(call: Call, response: Response) {
                val serverResponse: String? = response.body()?.string()
                if (response.isSuccessful && serverResponse != null) {
                    /*try {*/
                    liveDataForObservation.postValue(
                        AppState.Success(
                            displayWeather(
                                Gson().fromJson(
                                    serverResponse,
                                    WeatherDTO::class.java
                                )
                            )
                        )
                    )
                    /*} catch () {
                    liveDataForObservation.value = AppState.Error(error)
                    }*/
                } else {
                    liveDataForObservation.//value = AppState.Error(Throwable("Ответ неуспешен"))
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                liveDataForObservation.//value = AppState.Error(Throwable(e.message))
            }
        }

    fun getLiveData() = liveDataForObservation

    fun getWeatherData(city: String, isOnline: Boolean = true) {
        this.city = city
        liveDataForObservation.value = AppState.Loading
        if (isOnline) {
            repositoryImpl.getWeatherDataFromServer(city, callback)
        } else {
            repositoryImpl.getWeatherDataFromLocalStorage(city /*onLoaderListener*/)
        }
    }
}
