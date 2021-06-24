package com.example.kotlinstart.view.detailsscreen

import android.os.Build
import android.os.Handler
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kotlinstart.dto.WeatherDTO
import com.example.kotlinstart.explore.Loader
import com.example.kotlinstart.model.WeatherData
import com.example.kotlinstart.repository.detailsrepository.RepositoryImplDetails
import com.google.gson.Gson
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.MalformedURLException
import java.net.URL
import java.util.stream.Collectors
import javax.net.ssl.HttpsURLConnection

internal class DetailsViewModel(
    private val liveDataForObservation: MutableLiveData<WeatherData> = MutableLiveData(),
    private val repositoryImpl: RepositoryImplDetails = RepositoryImplDetails()
) : ViewModel() {

    lateinit var city: String

    fun subscribe(): LiveData<WeatherData> {
        return liveDataForObservation
    }

    fun setCityData(city: String) {
        this.city = city
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun getWeatherDataByCityName() {
        createWeatherData()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun createWeatherData() {
        liveDataForObservation.value = repositoryImpl.getWeatherDataFromLocalStorage(city)
        Loader(repositoryImpl, liveDataForObservation, city).loadWeather()
    }
}
