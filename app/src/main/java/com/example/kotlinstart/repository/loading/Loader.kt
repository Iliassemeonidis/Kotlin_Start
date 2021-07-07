package com.example.kotlinstart.repository.loading

import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import com.example.kotlinstart.BuildConfig
import com.example.kotlinstart.dto.WeatherDTO
import com.example.kotlinstart.model.WeatherData
import com.example.kotlinstart.repository.detailsrepository.RepositoryDetailsImpl
import com.google.gson.Gson
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.MalformedURLException
import java.net.URL
import java.util.stream.Collectors
import javax.net.ssl.HttpsURLConnection

object Loader {

    @RequiresApi(Build.VERSION_CODES.N)
    fun loadWeather(
        listener: OnWeatherListener,
        lat: Double,
        lon: Double,
    ) {
        try {
            val uri =
                URL("https://api.weather.yandex.ru/v2/informers?lat=${lat}&lon=${lon}&lang=ru_RU")
            Thread {
                Looper.prepare()
                val handler = Handler()
                val urlConnection: HttpsURLConnection
                try {
                    urlConnection = uri.openConnection() as HttpsURLConnection
                    urlConnection.requestMethod = "GET"
                    urlConnection.addRequestProperty(
                        "X-Yandex-API-Key",
                        BuildConfig.WEATHER_API_KEY
                    )
                    urlConnection.readTimeout = 10000
                    val reader = BufferedReader(InputStreamReader(urlConnection.inputStream))
                    val weatherDTO: WeatherDTO =
                        Gson().fromJson(getLines(reader), WeatherDTO::class.java)
                    handler.post { listener.onLoaded(weatherDTO) }

                } catch (e: Exception) {
                    Log.e("ConnectException", e.message, e)
                    e.printStackTrace()
                }
                Looper.loop()
            }.start()
        } catch (e: MalformedURLException) {
            Log.e("URLException", e.message, e)
            e.printStackTrace()
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun getLines(reader: BufferedReader): String {
        return reader.lines().collect(Collectors.joining("\n"))
    }

    interface OnWeatherListener {
        fun onLoaded(weatherDTO: WeatherDTO)
        fun onFulled(error: Throwable)
    }
}