package com.example.kotlinstart.repository.loader

import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.kotlinstart.BuildConfig
import com.example.kotlinstart.dto.WeatherDTO
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
        val handler = Handler()
        try {
            val uri =
                URL("https://api.weather.yandex.ru/v2/informers?lat=${lat}&lon=${lon}&lang=ru_RU")
            Thread {

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
                    handler.post { listener.onFailed(Throwable(e.message)) }
                }
            }.start()
        } catch (e: MalformedURLException) {
            Log.e("URLException", e.message, e)
            e.printStackTrace()
            handler.post { listener.onFailed(Throwable(e.message)) }
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun getLines(reader: BufferedReader): String {
        return reader.lines().collect(Collectors.joining("\n"))
    }

    interface OnWeatherListener {
        fun onLoaded(weatherDTO: WeatherDTO)
        fun onFailed(error: Throwable)
    }
}