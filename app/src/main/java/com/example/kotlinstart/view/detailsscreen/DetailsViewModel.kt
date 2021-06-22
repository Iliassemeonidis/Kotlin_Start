package com.example.kotlinstart.view.detailsscreen

import android.os.Build
import android.os.Handler
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kotlinstart.dto.WeatherDTO
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
        loadWeather()
    }


    @RequiresApi(Build.VERSION_CODES.N)
    private fun loadWeather() {
        try {
            val uri =
                URL("https://api.weather.yandex.ru/v2/forecast?lat=${liveDataForObservation.value?.lat}&lon=${liveDataForObservation.value?.lon}&lang=ru_RU")
            val handler = Handler()
            Thread {
                val urlConnection: HttpsURLConnection
                try {
                    urlConnection = uri.openConnection() as HttpsURLConnection
                    urlConnection.requestMethod = "GET"
                    urlConnection.addRequestProperty("X-Yandex-API-Key", YOUR_API_KEY)
                    urlConnection.readTimeout = 10000
                    val reader = BufferedReader(InputStreamReader(urlConnection.inputStream))
                    val weatherDTO: WeatherDTO =
                        Gson().fromJson(getLines(reader), WeatherDTO::class.java)
                    handler.post { displayWeather(weatherDTO) }
                } catch (e: Exception) {
                    Log.e("", "Fail connection", e)
                    e.printStackTrace()
                }

            }.start()
        } catch (e: MalformedURLException) {
            Log.e("", "Fail URI", e)
            e.printStackTrace()
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun getLines(reader: BufferedReader): String {
        return reader.lines().collect(Collectors.joining("\n"))
    }


    private fun displayWeather(weatherDTO: WeatherDTO){
        val weathers = repositoryImpl.getWeatherDataFromLocalStorage(city)
        weathers.textViewFeelsLike = "Ощущается как ${weatherDTO.fact?.feels_like.toString()}°C"
        weathers.weatherCondition = weatherDTO.fact?.condition.toString()
        weathers.degrees = "${weatherDTO.fact?.temp.toString()}°C"
        liveDataForObservation.value = weathers
    }


  companion  object {
        private const val YOUR_API_KEY = "1ba5eaad-01e7-421e-9cf7-29eaa4e7b477"
    }
}
