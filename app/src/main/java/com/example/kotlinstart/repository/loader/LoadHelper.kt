package com.example.kotlinstart.repository.loader

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.kotlinstart.dto.WeatherDTO
import com.example.kotlinstart.model.WeatherData

lateinit var weathers: WeatherData

fun getDetailWeather(city: String = "Москва") = when (city) {
    "Москва" -> WeatherData(
        city,
        "27°",
        "Солнечно",
        "Ощущается как 30°",
        lat = 55.755826,
        lon = 37.617299900000035
    )
    "Афины" -> WeatherData(
        city,
        "27°",
        "Солнечно",
        "Ощущается как 30°",
        lat = 37.9794500,
        lon = 23.7162200
    )
    "Сочи" -> WeatherData(
        city,
        "27°",
        "Солнечно",
        "Ощущается как 30°",
        lat = 43.5991700,
        lon = 39.7256900
    )
    "Лос-Анджелес" -> WeatherData(
        city,
        "27°",
        "Солнечно",
        "Ощущается как 30°",
        lat = 34.0522300,
        lon = -118.2436800
    )
    "Владикавказ" -> WeatherData(
        city,
        "27°",
        "Солнечно",
        "Ощущается как 30°",
        lat = 43.0366700,
        lon = 44.6677800
    )
    "Мурманск" -> WeatherData(
        city,
        "27°",
        "Солнечно",
        "Ощущается как 30°",
        lat = 68.9791700,
        lon = 33.0925100
    )
    "Таймырский" -> WeatherData(
        city,
        "7°",
        "Холодновато",
        "Ощущается как",
        lat = 80.610813,
        lon = 94.485730
    )
    else -> WeatherData(city)
}

@RequiresApi(Build.VERSION_CODES.N)
fun displayWeather(
    weatherDTO: WeatherDTO,
) = weathers.apply {
    textViewFeelsLike = "Ощущается как ${weatherDTO.fact?.feels_like.toString()}°C"
    weatherCondition = weatherDTO.fact?.condition.toString()
    degrees = "${weatherDTO.fact?.temp.toString()}°C"
}

@RequiresApi(Build.VERSION_CODES.N)
fun getWeatherFromServer(city: String, listener: Loader.OnWeatherListener) {
    weathers = getDetailWeather(city)
    Loader.loadWeather(listener, weathers.lat, weathers.lon)
}
